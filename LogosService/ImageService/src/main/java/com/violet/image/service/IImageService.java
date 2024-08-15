package com.violet.image.service;

import com.violet.common.domain.Result;
import com.violet.common.domain.QueryMatch;
import org.springframework.web.multipart.MultipartFile;

public interface IImageService {
    /**
     * TODO 该操作需要用户事先登录
     * 将用户上传的图片按分组保存
     * @param name 图片名【唯一】
     * @param groupID 图片所属分组
     * @param file 保存的图片
     * @return {data: null, message: '创建成功', code: ResponseCode.OK.code}
     */
    Result createImage(String name, Long groupID, MultipartFile file);

    /**
     * TODO 该操作需要用户事先登录
     * 根据登录用户ID与图片分组获取其所有图片
     * @param groupID 图片分组
     * @return {data: [所属分组图片VO], message: '获取成功', code: ResponseCode.OK.code}
     */
    Result getImagesByGroupID(Long groupID);

    /**
     * TODO 该操作需要用户事先登录
     * 根据图片 ID 删除对应图片
     * @param id 图片ID
     * @return new Result(null, "删除成功", ResponseCode.OK.code)
     */
    Result deleteByID(Long id);

    /**
     * TODO 该操作需要用户事先登录
     * 返回对应查询条件的图片 VO
     * @param query 查询条件
     * @return {data: [所属分组图片VO], message: '获取成功', code: ResponseCode.OK.code}
     */
    Result getImagesByQuery(QueryMatch query);

    /**
     * TODO 该操作需要用户事先登录
     * 根据图片分组删除其下的所有图片
     * @param groupID 图片分组
     */
    void deleteByGroupId(Long groupID);
}
