package com.violet.article.controller;

import com.violet.article.domain.dto.CollectArticleFormDTO;
import com.violet.article.domain.dto.CollectGroupAndArticleFormDTO;
import com.violet.article.service.ICollectArticleService;
import com.violet.common.domain.QueryMatchPage;
import com.violet.common.domain.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/collectArticles")
@RequiredArgsConstructor
public class CollectArticleController {

    private final ICollectArticleService collectArticleService;

    @ApiOperation("创建一个收藏")
    @PostMapping
    public Result createCollectArticle(@Validated @RequestBody CollectGroupAndArticleFormDTO dto) {
        return collectArticleService.createCollectArticle(dto);
    }

    @ApiOperation("查看当前用户是否收藏过一篇文章")
    @GetMapping
    public Result isCollectArticle(@RequestParam("articleId") Long articleId) {
        return collectArticleService.isCollectArticle(articleId);
    }

    @ApiOperation("根据条件获取收藏文章")
    @GetMapping("/list")
    public Result getCollectArticles(@Validated QueryMatchPage query) {
        return collectArticleService.getCollectArticles(query);
    }

    @ApiOperation("取消收藏")
    @DeleteMapping
    public Result deleteCollectArticle(@Validated @RequestBody CollectArticleFormDTO dto) {
        return collectArticleService.deleteCollectArticle(dto);
    }

    @ApiOperation("根据ID取消收藏")
    @DeleteMapping("{articleId}")
    public Result deleteCollectArticle(@PathVariable Long articleId) {
        return collectArticleService.deleteCollectArticleById(articleId);
    }

}
