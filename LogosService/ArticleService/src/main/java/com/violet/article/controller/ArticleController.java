package com.violet.article.controller;

import com.violet.article.domain.po.Article;
import com.violet.article.service.IArticleService;
import com.violet.common.domain.QueryMatchPage;
import com.violet.common.domain.Result;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final IArticleService articleService;

    // TODO 该操作不需要登录
    @ApiOperation("根据条件获取对应文章")
    @GetMapping
    public Result getArticles(@Validated QueryMatchPage query) {
        return articleService.getArticles(query);
    }

    // TODO 该操作不需要登录
    @ApiOperation("根据ID获取文章数据")
    @GetMapping("{id}")
    public Result getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    // TODO 该操作不需要登录
    @ApiOperation("获取对应用户的信息和文章")
    @GetMapping("/user/{userId}")
    public Result getArticleByUserId(@PathVariable Long userId, @Validated QueryMatchPage query) {
        return articleService.getArticleByUserId(userId, query);
    }

    // TODO 该操作需要登录
    @ApiOperation("让某个文章的评论数 + 1")
    @PutMapping("{id}")
    public Result commentIncreaseById(@PathVariable Long id) {
        return articleService.commentIncreaseById(id);
    }

}
