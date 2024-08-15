package com.violet.article.service;

import com.violet.article.domain.dto.PublishArticleFormDTO;
import com.violet.common.domain.QueryMatchPage;
import com.violet.common.domain.Result;

public interface IPublishArticleService {
    /**
     * TODO 该操作需要登录
     * 根据查询条件获取登录用户的对应文章
     * @param queryMatchPage 匹配条件
     * @return new Result(data, "获取成功", ResponseCode.OK.code);
     */
    Result getPublishArticles(QueryMatchPage queryMatchPage);

    /**
     * TODO 该操作需要登录
     * 创建一篇文章信息
     * @param dto 要创建的文章内容
     * @return new Result(null, "发布成功", ResponseCode.OK.code);
     */
    Result createPublishArticle(PublishArticleFormDTO dto);

    /**
     * TODO 该操作需要登录
     * 根据 ID 获取一篇文章信息
     * @param id 文章 ID
     * @return new Result(vo, "获取成功", ResponseCode.OK.code);
     */
    Result getPublishArticlesById(Long id);

    /**
     * TODO 该操作需要登录
     * 根据 ID 修改一篇文章、文章名不能重复
     * @param dto 要修改的内容
     * @param id 具体修改那篇文章的 ID
     * @return new Result(null, "修改成功", ResponseCode.OK.code);
     */
    Result updatePublishArticleById(PublishArticleFormDTO dto, Long id);

    /**
     * TODO 该操作需要登录
     * 该操作会删除文章和对应的文章评论还有收藏
     * @param id 文章 ID
     * @return
     */
    Result deletePublishArticleById(Long id);
}
