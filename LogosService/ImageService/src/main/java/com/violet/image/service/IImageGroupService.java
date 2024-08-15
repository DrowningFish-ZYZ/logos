package com.violet.image.service;

import com.violet.common.domain.GroupFormDTO;
import com.violet.common.domain.Result;

public interface IImageGroupService {
    /**
     * TODO 该操作需要用户事先登录
     * 创建一个图片分组
     * @param dto 分组创建实体
     * @return  new Result(null, "创建成功", ResponseCode.OK.code);
     */
    Result createImageGroup(GroupFormDTO dto);

    /**
     * TODO 该操作需要用户事先登录
     * 获取当前登录用户的所有图片分组信息
     * @return new Result(ImageGroupVOS, "获取成功", ResponseCode.OK.code)
     */
    Result getAllImageGroup();

    /**
     * TODO 该操作需要用户事先登录
     * 根据图片分组 ID 去修改对应的分组信息
     * @param id 图片分组 ID
     * @param dto 要修改的信息
     * @return new Result(null, "修改成功", ResponseCode.OK.code);
     */
    Result updateImageGroup(Long id, GroupFormDTO dto);

    /**
     * TODO 该操作需要用户事先登录
     * 根据图片分组 ID 删除对应的分组数据，该操作会删除分组下的所有数据
     * @param id 分组 ID
     * @return new Result(null, "删除成功", ResponseCode.OK.code)
     */
    Result deleteImageGroupById(Long id);

    /**
     * TODO 该操作需要用户事先登录
     * 获取当前登录用户的对应ID图片分组信息
     * @return new Result(ImageGroupVOS, "获取成功", ResponseCode.OK.code)
     */
    Result getGroupById(Long id);
}
