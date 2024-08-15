package com.violet.article.service;

import com.violet.common.domain.GroupFormDTO;
import com.violet.common.domain.Result;

public interface IPublishArticleGroupService {
    /**
     * TODO 该操作需要登录
     * 为当前用户创建分组
     * @param dto 分组信息
     * @return new Result(null, "创建成功", ResponseCode.OK.code);
     */
    Result createGroup(GroupFormDTO dto);

    /**
     * TODO 该操作需要登录
     * 获取当前登录用户的所有分组
     * @return new Result(vos, "获取成功", ResponseCode.OK.code);
     */
    Result getAllGroups();

    /**
     * TODO 该操作需要登录
     * 根据分组 ID 获取当前登录用户的分组
     * @param id 分组 ID
     * @return new Result(vo, "获取成功", ResponseCode.OK.code);
     */
    Result getGroupById(Long id);

    /**
     * TODO 该操作需要登录
     * 根据 ID 修改分组信息
     * @param dto 要修改的信息
     * @param id 具体修改的分组 ID
     * @return new Result(null, "修改成功", ResponseCode.OK.code);
     */
    Result updateGroupById(GroupFormDTO dto, Long id);

    Result deletePublishArticleGroupById(Long id);
}
