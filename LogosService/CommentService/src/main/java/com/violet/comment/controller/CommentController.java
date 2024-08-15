package com.violet.comment.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.violet.comment.domain.dto.CommentFormDTO;
import com.violet.comment.service.ICommentService;
import com.violet.common.domain.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService commentService;
    private final Cache<Long, Result> cache;

    @ApiOperation("创建评论")
    @PostMapping
    public Result createComment(@Validated @RequestBody CommentFormDTO dto) {
        return commentService.createComment(dto);
    }

    // TODO 不需要登录
    @ApiOperation("获取某一篇文章的所有评论")
    @GetMapping("/list/{articleId}")
    public Result getCommentsByArticleId(@PathVariable("articleId") Long articleId) {
        // 如果 JVM 缓存不存在、则去 Redis 缓存中查找、最后再去 Mysql
        return cache.get(
            articleId,
            commentService::getCommentsByArticleId
        );
    }

    @ApiOperation("根据文章ID删除评论")
    @DeleteMapping("{articleId}")
    public Result deleteCommentByArticleId(@PathVariable("articleId") Long articleId) {
        return commentService.deleteCommentByArticleId(articleId);
    }

}
