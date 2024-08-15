package com.violet.image.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.violet.common.domain.GroupFormDTO;
import com.violet.common.domain.Result;
import com.violet.common.exception.ParameterException;
import com.violet.common.utils.UserContext;
import com.violet.image.domain.po.Image;
import com.violet.image.domain.po.ImageGroup;
import com.violet.image.domain.vo.ImageGroupVO;
import com.violet.image.mapper.ImageGroupMapper;
import com.violet.image.mapper.ImageMapper;
import com.violet.image.service.IImageGroupService;
import com.violet.image.service.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageGroupServiceImpl extends ServiceImpl<ImageGroupMapper, ImageGroup>
        implements IImageGroupService {

    private final ImageMapper imageMapper;
    private final IImageService imageService;

    @Override
    public Result createImageGroup(GroupFormDTO dto) {
        // 1.校验用户和数据
        UserContext.verify();
        dto.verify();
        // 2.当前分组是否存在，如果存在则不能新建
        ImageGroup one = this.lambdaQuery()
            .eq(ImageGroup::getUserId, UserContext.getUserId())
            .eq(ImageGroup::getName, dto.getName())
            .one();
        if (one != null) throw new ParameterException("分组已经存在");
        // 2.新建分组
        ImageGroup imageGroup = new ImageGroup();
        imageGroup.setUserId(UserContext.getUserId());
        imageGroup.setCreateTime(LocalDateTime.now());
        imageGroup.setName(dto.getName());
        imageGroup.setDesc(dto.getDesc());
        // 4.存入数据库
        this.save(imageGroup);
        // 5.返回详情
        return Result.Builder.buildCreateResult();
    }

    @Override
    public Result getAllImageGroup() {
        // 1.校验用户是否登录
        UserContext.verify();
        // 2.根据用户 ID 获取其所有分组数据
        List<ImageGroup> data = this.lambdaQuery()
            .eq(ImageGroup::getUserId, UserContext.getUserId())
            .list();
        // 3.将分组数据封装为 VO
        List<ImageGroupVO> vos = BeanUtil.copyToList(data, ImageGroupVO.class);
        for (ImageGroupVO vo : vos) {
            Integer count = imageMapper.selectCount(
                new LambdaQueryWrapper<Image>()
                    .eq(Image::getImageGroupId, vo.getId())
            );
            vo.setCount(count);
        }
        // 4.返回数据
        return Result.Builder.buildGetResult(vos);
    }

    @Override
    public Result updateImageGroup(Long id, GroupFormDTO dto) {
        // 1.校验用户和数据
        UserContext.verify();
        dto.verify();
        // 2.根据用户 ID 和分组 ID 获取对应分组
        ImageGroup imageGroup = this.lambdaQuery()
                .eq(ImageGroup::getId, id)
                .eq(ImageGroup::getUserId, UserContext.getUserId())
                .one();
        // 3.校验是否存在
        if (imageGroup == null) throw new ParameterException("分组不存在");
        // 修改后的分组名不能重复
        ImageGroup one = this.lambdaQuery()
                .ne(ImageGroup::getId, id)
                .eq(ImageGroup::getName, dto.getName())
                .eq(ImageGroup::getUserId, UserContext.getUserId())
                .one();
        if (one != null) throw new ParameterException("分组名不能重复");
        // 4.重置分组数据
        imageGroup.setName(dto.getName());
        imageGroup.setDesc(dto.getDesc());
        // 5.入库
        this.updateById(imageGroup);
        // 6.返回信息
        return Result.Builder.buildUpdateResult();
    }

    @Override
    public Result deleteImageGroupById(Long id) {
        // 1.校验用户是否登录
        UserContext.verify();
        // 2.根据分组 ID 获取对应分组
        ImageGroup group = this.lambdaQuery()
                .eq(ImageGroup::getUserId, UserContext.getUserId())
                .eq(ImageGroup::getId, id)
                .one();
        if (group == null) throw new ParameterException("错误的分组");
        // 3.根据分组删除其下的所有图片
        imageService.deleteByGroupId(group.getId());
        // 4.再删除分组本身
        this.removeById(group.getId());
        // 5.返回详情
        return Result.Builder.buildDeleteResult();
    }

    @Override
    public Result getGroupById(Long id) {
        // 校验用户
        UserContext.verify();
        // 获取分组
        ImageGroup one = this.lambdaQuery().eq(ImageGroup::getId, id).one();
        if (one == null) throw new ParameterException("异常的分组");
        // 封装 VO
        ImageGroupVO vo = BeanUtil.copyProperties(one, ImageGroupVO.class);
        // 返回数据
        return Result.Builder.buildGetResult(vo);
    }
}
