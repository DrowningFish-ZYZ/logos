package com.violet.article.controller;

import com.violet.article.service.IPublishArticleGroupService;
import com.violet.common.domain.GroupFormDTO;
import com.violet.common.domain.Result;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/publishArticleGroups")
@RequiredArgsConstructor
public class PublishArticleGroupController {

    private final IPublishArticleGroupService publishArticleGroupService;

    @ApiOperation("获取登录用户发布文章分组数据")
    @GetMapping
    public Result getAllPublishArticleGroups() {
        return publishArticleGroupService.getAllGroups();
    }

    @ApiOperation("根据 ID 获取单个分组")
    @GetMapping("/{id}")
    public Result getPublishArticleGroupById(@PathVariable Long id) {
        return publishArticleGroupService.getGroupById(id);
    }

    @ApiOperation("创建登录用户发布文章分组")
    @PostMapping
    public Result createPublishArticleGroup(@Validated @RequestBody GroupFormDTO dto) {
        return publishArticleGroupService.createGroup(dto);
    }

    @ApiOperation("根据 ID 修改文章分组")
    @PutMapping("/{id}")
    public Result updatePublishArticleGroup(@PathVariable Long id, @Validated @RequestBody GroupFormDTO dto) {
        return publishArticleGroupService.updateGroupById(dto, id);
    }

    @ApiOperation("删除对应分组及下面所有数据")
    @DeleteMapping("{id}")
    public Result deletePublishArticleGroupById(@PathVariable Long id) {
        return publishArticleGroupService.deletePublishArticleGroupById(id);
    }

}
