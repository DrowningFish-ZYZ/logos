package com.violet.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.violet.api.domain.vo.UserVO;
import com.violet.common.domain.Result;
import com.violet.common.exception.ParameterException;
import com.violet.common.exception.UserLoginException;
import com.violet.common.utils.UserContext;
import com.violet.user.domain.dto.UserEditFormDTO;
import com.violet.user.domain.dto.UserLoginFormDTO;
import com.violet.user.domain.dto.UserRegisterDTO;
import com.violet.user.domain.po.User;
import com.violet.user.domain.vo.UserEditVO;
import com.violet.user.domain.vo.UserLoginVO;
import com.violet.user.enums.UserGenderStatus;
import com.violet.user.enums.UserStatus;
import com.violet.user.mapper.UserMapper;
import com.violet.user.service.IUserService;
import com.violet.user.utils.JWTUtils;
import com.violet.user.utils.SmsCodeUtils;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@GlobalTransactional
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements IUserService {

    private final StringRedisTemplate redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final SmsCodeUtils smsCodeUtils;

    @Override
    public UserLoginVO login(UserLoginFormDTO dto) {
        // 1.根据手机号去查询用户
        User user = this.lambdaQuery().eq(User::getPhone, dto.getPhone()).one();
        if (user == null) {
            throw new UserLoginException("账户或密码错误");
        }
        // 2.校验是否被禁用
        if (user.getStatus() == UserStatus.FROZEN) {
            throw new UserLoginException("用户被冻结");
        }
        // 如果存在密码就校验密码
        if (!dto.getPassword().trim().isEmpty()) {
            if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                throw new UserLoginException("账户或密码错误");
            }
        // 不存在密码就校验 code
        } else if (!dto.getCode().trim().isEmpty()) {
            String code = redisTemplate.opsForValue().get("code:" + dto.getPhone());
            if (code == null || !code.equals(dto.getCode())) {
                throw new ParameterException("验证码错误");
            }
        } else {
            // 都不存在则报错
            throw new ParameterException("请至少选择一种登录方式");
        }
        // 4.生成 Token
        String token = JWTUtils.createToken(user.getId());
        UserLoginVO vo = BeanUtil.copyProperties(user, UserLoginVO.class);
        vo.setToken(token);
        return vo;
    }

    @Override
    public Result getUserById(Long id) {
        User user = this.lambdaQuery().eq(User::getId, id).one();
        if (user == null) throw new ParameterException("用户不存在");
        UserVO vo = BeanUtil.copyProperties(user, UserVO.class);
        vo.setGender(user.getGender().getValue());
        vo.setStatus(user.getStatus().getValue());
        return Result.Builder.buildGetResult(vo);
    }

    @Override
    public Result getLoggedUser() {
        UserContext.verify();
        User user = this.lambdaQuery()
            .eq(User::getId, UserContext.getUserId())
            .one();
        UserEditVO vo = BeanUtil.copyProperties(user, UserEditVO.class);
        return Result.Builder.buildGetResult(vo);
    }

    @Override
    public Result getUsersByIds(Set<Long> ids) {
        // 获取用户列表
        List<User> list = this.lambdaQuery()
            .in(User::getId, ids)
            .list();
        // 转化为 VO
        List<UserVO> vos = BeanUtil.copyToList(list, UserVO.class);
        // 返回数据
        return Result.Builder.buildGetResult(vos);
    }

    @Override
    public Result updateLoggedUser(UserEditFormDTO dto) {
        // 校验用户和数据
        UserContext.verify();
        dto.verify();
        // 修改后的用户名不能重复
        User one = this.lambdaQuery()
            .ne(User::getId, UserContext.getUserId())
            .eq(User::getUsername, dto.getUsername())
            .one();
        if (one != null) throw new ParameterException("该用户名已经被注册");
        // 重置数据
        User user = this.lambdaQuery()
            .eq(User::getId, UserContext.getUserId())
            .one();
        user.setUsername(dto.getUsername());
        if (UserGenderStatus.MALE.getValue() == dto.getGender()) user.setGender(UserGenderStatus.MALE);
        else if (UserGenderStatus.FEMALE.getValue() == dto.getGender()) user.setGender(UserGenderStatus.FEMALE);
        user.setDesc(dto.getDesc());
        user.setHeadPortrait(dto.getHeadPortrait());
        // 是否修改密码
        if (dto.getOldPwd() != null && !dto.getOldPwd().isEmpty()) {
            // 旧密码不一致
            if (!passwordEncoder.matches(dto.getOldPwd(), user.getPassword())) throw new ParameterException("旧密码错误");
            if (dto.getNewPwd() == null || dto.getNewPwd().isEmpty()) throw new ParameterException("新密码不能为空");
            user.setPassword(passwordEncoder.encode(dto.getNewPwd()));
        }
        // 入库
        this.updateById(user);
        // 返回数据
        return Result.Builder.buildUpdateResult();
    }

    @Override
    public Result register(UserRegisterDTO dto) {
        // 校验数据
        dto.verify();
        // 查看当前用户名和手机是否被注册
        User user = this.lambdaQuery().eq(User::getPhone, dto.getPhone()).one();
        if (user != null) throw new ParameterException("当前手机号已经注册过");
        user = this.lambdaQuery().eq(User::getUsername, dto.getUsername()).one();
        if (user != null) throw new ParameterException("当前用户名已被注册");
        // 从 Redis 中获取验证码
        String code = redisTemplate.opsForValue().get("code:" + dto.getPhone());
        if (code == null) throw new ParameterException("验证码错误");
        // 没有问题入库
        user = new User();
        user.setPhone(dto.getPhone());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setDesc(dto.getInfo());
        user.setCreateTime(LocalDateTime.now());
        user.setStatus(UserStatus.NORMAL);
        if (UserGenderStatus.MALE.getValue() == dto.getGender()) user.setGender(UserGenderStatus.MALE);
        else user.setGender(UserGenderStatus.FEMALE);
        this.save(user);
        // 返回详情
        return Result.Builder.buildCreateResult();
    }

    @Override
    public Result getCode(String phone) {
        // 判断当前手机号码是否正确
        if (!UserRegisterDTO.validateMobileNumber(phone.trim())) throw new ParameterException("错误的手机号码");
        // 如果 Redis 当中的验证码还存在则不发送
        if (redisTemplate.opsForValue().get("code:" + phone) != null) throw new ParameterException("验证码未失效、请勿重复发送");
        // 创建随机验证码，存入 Redis 设置 60 秒过期
        String code = smsCodeUtils.getCode();
        redisTemplate.opsForValue().set("code:" + phone, code, 60, TimeUnit.SECONDS);
        // 对用户发送验证码
        smsCodeUtils.sendSms(code, phone);
        // 返回详情
        return Result.Builder.buildGetResult(null);
    }

    @Override
    public Result getLoginCode(String phone) {
        // 判断当前手机号码是否正确
        if (!UserRegisterDTO.validateMobileNumber(phone.trim())) throw new ParameterException("错误的手机号码");
        // 当前手机号不存在数据库中
        if (this.lambdaQuery().eq(User::getPhone, phone).one() == null) throw new ParameterException("该手机号未注册, 请先注册");
        // 如果 Redis 当中的验证码还存在则不发送
        if (redisTemplate.opsForValue().get("code:" + phone) != null) throw new ParameterException("验证码未失效、请勿重复发送");
        // 创建随机验证码，存入 Redis 设置 60 秒过期
        String code = smsCodeUtils.getCode();
        redisTemplate.opsForValue().set("code:" + phone, code, 60, TimeUnit.SECONDS);
        // 对用户发送验证码
        smsCodeUtils.sendSms(code, phone);
        // 返回详情
        return Result.Builder.buildGetResult(null);
    }

}
