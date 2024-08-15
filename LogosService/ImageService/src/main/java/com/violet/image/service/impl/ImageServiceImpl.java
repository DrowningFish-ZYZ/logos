package com.violet.image.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.violet.common.domain.Result;
import com.violet.common.enums.ResponseCode;
import com.violet.common.exception.ParameterException;
import com.violet.common.exception.ServiceException;
import com.violet.common.exception.UserLoginException;
import com.violet.common.utils.UserContext;
import com.violet.image.config.UploadConfig;
import com.violet.image.domain.po.Image;
import com.violet.image.domain.po.ImageGroup;
import com.violet.common.domain.QueryMatch;
import com.violet.common.domain.SortOrder;
import com.violet.image.domain.vo.ImageGroupVO;
import com.violet.image.domain.vo.ImageVO;
import com.violet.image.mapper.ImageGroupMapper;
import com.violet.image.mapper.ImageMapper;
import com.violet.image.service.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements IImageService {

    private final ImageMapper imageMapper;
    private final UploadConfig uploadConfig;
    private final ImageGroupMapper imageGroupMapper;

    @Override
    public Result createImage(String name, Long groupID, MultipartFile file) {
        // 校验用户
        UserContext.verify();
        // 校验数据
        if (name == null || name.isEmpty() || groupID == null || file == null || file.isEmpty()) {
            throw new ParameterException("参数不能为空");
        }
        // 1.图片名不能重复
        Image one = this.lambdaQuery()
            .eq(Image::getUserId, UserContext.getUserId())
            .eq(Image::getName, name)
            .one();
        if (one != null) throw new ParameterException("图片名不能重复");
        // 2.根据 GroupID 获取 group
        ImageGroup group = imageGroupMapper.selectOne(
            new LambdaQueryWrapper<ImageGroup>()
                .eq(ImageGroup::getId, groupID)
                .eq(ImageGroup::getUserId, UserContext.getUserId())
        );
        if (group == null) throw new ParameterException("错误的分组");
        // 2.保存图片到本地
        String fileName = UUID.randomUUID().toString();
        String suffix = FileNameUtil.getSuffix(file.getOriginalFilename());
        try {
            file.transferTo(new File(uploadConfig.getDirectory()+ "/" + fileName + "." + suffix));
        } catch (IOException e) {throw new ServiceException("服务异常、请重试");}
        // 3.存入数据库
        Image image = new Image();
        image.setSrc(uploadConfig.getHttp() + "/" + fileName + "." + suffix);
        image.setName(name);
        image.setImageGroupId(group.getId());
        image.setCreateTime(LocalDateTime.now());
        image.setUserId(UserContext.getUserId());
        this.save(image);
        // 4.返回详情
        return Result.Builder.buildCreateResult();
    }

    @Override
    public Result getImagesByGroupID(Long groupID) {
        UserContext.verify();
        // 1.获取当前用户的对应 groupID imageGroup
        ImageGroup imageGroup = imageGroupMapper.selectOne(
            new LambdaQueryWrapper<ImageGroup>()
                .eq(ImageGroup::getUserId, UserContext.getUserId())
                .eq(ImageGroup::getId, groupID)
        );
        if (imageGroup == null) throw new ParameterException("分组不存在");
        // 2.再根据 imageGroup 获取对应的 图片
        List<Image> list = this.lambdaQuery()
            .eq(Image::getImageGroupId, imageGroup.getId())
            .list();
        // 3.封装为 VO
        List<ImageVO> vos = BeanUtil.copyToList(list, ImageVO.class);
        // 4.返回数据
        return Result.Builder.buildGetResult(vos);
    }

    @Override
    public Result deleteByID(Long id) {
        // 1.校验用户
        UserContext.verify();
        // 2.根据 ID 获取图片
        Image image = this.lambdaQuery()
            .eq(Image::getUserId, UserContext.getUserId())
            .eq(Image::getId, id)
            .one();
        if (image == null) throw new ParameterException("异常图片数据");
        // 3.删除本地文件
        String[] split = image.getSrc().split("/");
        String imageName = split[split.length - 1];
        new File(uploadConfig.getDirectory() + "/" + imageName).delete();
        // 4.删除数据库信息
        this.lambdaUpdate().eq(Image::getId, id).remove();
        // 5.返回详情
        return Result.Builder.buildDeleteResult();
    }

    @Override
    public Result getImagesByQuery(QueryMatch query) {
        // 1.校验用户
        UserContext.verify();
        // 2.创建查询条件
        LambdaQueryWrapper<Image> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Image::getUserId, UserContext.getUserId());
        // 2.1按分组筛选
        if (query.getGroupId() != null && query.getGroupId() != 0) {
            wrapper.eq(Image::getImageGroupId, query.getGroupId());
        }
        // 2.2按关键字筛选
        if (query.getKeyword() != null && !query.getKeyword().isEmpty()) {
            wrapper.like(Image::getName, query.getKeyword());
        }
        // 2.3是否排序
        if (SortOrder.CREATE_TIME.equals(query.getSort())) {
            wrapper.orderByDesc(Image::getCreateTime);
        }
        // 2.4根据 wrapper 获取数据
        List<Image> images = imageMapper.selectList(wrapper);
        // 3.封装为 VO，并返回
        List<ImageVO> vos = BeanUtil.copyToList(images, ImageVO.class);
        return Result.Builder.buildGetResult(vos);
    }

    @Override
    public void deleteByGroupId(Long groupID) {
        List<Image> images = this.lambdaQuery()
                .eq(Image::getImageGroupId, groupID)
                .list();
        for (Image image : images) this.deleteByID(image.getId());
    }
}
