package com.violet.article.controller;

import com.violet.article.domain.dto.PublishArticleFormDTO;
import com.violet.article.service.IPublishArticleService;
import com.violet.common.domain.QueryMatchPage;
import com.violet.common.domain.Result;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/publishArticles")
@RequiredArgsConstructor
public class PublishArticleController {

    private final IPublishArticleService publishArticleService;

    @ApiOperation("获取用户对应查询条件的发布文章")
    @GetMapping
    public Result getPublishArticles(QueryMatchPage queryMatchPage) {
        return publishArticleService.getPublishArticles(queryMatchPage);
    }

    @ApiOperation("获取当前用户的指定ID的文章")
    @GetMapping("{id}")
    public Result getPublishArticleById(@PathVariable Long id) {
        return publishArticleService.getPublishArticlesById(id);
    }

    @ApiOperation("当前登录用户发布文章")
    @PostMapping
    public Result createPublishArticle(@Validated @RequestBody PublishArticleFormDTO dto) {
        return publishArticleService.createPublishArticle(dto);
    }

    @ApiOperation("修改当前用户的指定ID文章")
    @PutMapping("{id}")
    public Result updatePublishArticleById(@Validated @RequestBody PublishArticleFormDTO dto, @PathVariable Long id) {
        return publishArticleService.updatePublishArticleById(dto, id);
    }

    @ApiOperation("删除发布的文章")
    @DeleteMapping("{id}")
    public Result deletePublishArticleById(@PathVariable Long id) {
        return publishArticleService.deletePublishArticleById(id);
    }

}
