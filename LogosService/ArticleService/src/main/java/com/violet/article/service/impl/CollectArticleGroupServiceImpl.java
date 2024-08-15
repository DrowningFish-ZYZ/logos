package com.violet.article.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.violet.article.domain.po.CollectArticleGroup;
import com.violet.article.domain.po.CollectGroupAndArticle;
import com.violet.article.mapper.CollectArticleGroupMapper;
import com.violet.article.mapper.CollectGroupAndArticleMapper;
import com.violet.article.service.ICollectArticleGroupService;
import com.violet.common.domain.GroupFormDTO;
import com.violet.common.domain.GroupVO;
import com.violet.common.domain.Result;
import com.violet.common.enums.ResponseCode;
import com.violet.common.exception.ParameterException;
import com.violet.common.utils.UserContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@GlobalTransactional
@Transactional
public class CollectArticleGroupServiceImpl extends ServiceImpl<CollectArticleGroupMapper, CollectArticleGroup>
        implements ICollectArticleGroupService {

    private final CollectGroupAndArticleMapper collectGroupAndArticleMapper;

    @Override
    public Result createGroup(GroupFormDTO dto) {
        // 校验用户和数据
        UserContext.verify();
        dto.verify();
        // 创建数据
        CollectArticleGroup group = new CollectArticleGroup();
        group.setName(dto.getName());
        group.setDesc(dto.getDesc());
        group.setCreateTime(LocalDateTime.now());
        group.setUserId(UserContext.getUserId());
        // 入库
        this.save(group);
        // 响应
        return Result.Builder.buildCreateResult();
    }

    @Override
    public Result getGroups() {
        // 校验用户
        UserContext.verify();
        // 获取当前用户的所有分组数据
        List<CollectArticleGroup> list = this.lambdaQuery()
                .eq(CollectArticleGroup::getUserId, UserContext.getUserId())
                .list();
        // 封装为 VO
        List<GroupVO> vos = BeanUtil.copyToList(list, GroupVO.class);
        for (GroupVO vo : vos) {
            Integer count = collectGroupAndArticleMapper.selectCount(
                    new LambdaQueryWrapper<CollectGroupAndArticle>()
                            .eq(CollectGroupAndArticle::getGroupId, vo.getId())
            );
            vo.setCount(count);
        }
        // 返回数据
        return Result.Builder.buildGetResult(vos);
    }

    @Override
    public Result getGroupById(Long id) {
        // 校验用户
        UserContext.verify();
        // 获取对应 ID 的分组
        CollectArticleGroup group = this.getById(id);
        if (group == null) throw new ParameterException("错误的分组");
        // 封装为 VO
        GroupVO vo = BeanUtil.copyProperties(group, GroupVO.class);
        // 返回数据
        return Result.Builder.buildGetResult(vo);
    }

    @Override
    public Result updateGroupById(Long id, GroupFormDTO dto) {
        // 校验用户和数据
        UserContext.verify();
        dto.verify();
        // 根据 ID 获取分组
        CollectArticleGroup group = this.getById(id);
        if (group == null) throw new ParameterException("错误的分组");
        // 分组名不能重复
        CollectArticleGroup one = this.lambdaQuery()
                .ne(CollectArticleGroup::getId, group.getId())
                .eq(CollectArticleGroup::getUserId, UserContext.getUserId())
                .eq(CollectArticleGroup::getName, dto.getName())
                .one();
        if (one != null) throw new ParameterException("分组名不能重复");
        // 重置分组数据
        group.setName(dto.getName());
        group.setDesc(dto.getDesc());
        // 入库
        this.updateById(group);
        // 返回
        return Result.Builder.buildUpdateResult();
    }
}
