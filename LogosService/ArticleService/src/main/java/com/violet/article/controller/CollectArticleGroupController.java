package com.violet.article.controller;

import com.violet.article.service.ICollectArticleGroupService;
import com.violet.common.domain.GroupFormDTO;
import com.violet.common.domain.Result;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/collectArticleGroups")
@RequiredArgsConstructor
public class CollectArticleGroupController {

    private final ICollectArticleGroupService collectArticleGroupService;

    @ApiOperation("获取当前用户的所有收藏分组")
    @GetMapping
    public Result getCollectArticleGroups() {
        return collectArticleGroupService.getGroups();
    }

    @ApiOperation("根据ID获取单个分组")
    @GetMapping("{id}")
    public Result getCollectArticleGroupById(@PathVariable Long id) {
        return collectArticleGroupService.getGroupById(id);
    }

    @ApiOperation("创建登录用户收藏分组")
    @PostMapping
    public Result createCollectArticleGroup(@Validated @RequestBody GroupFormDTO dto) {
        return collectArticleGroupService.createGroup(dto);
    }

    @ApiOperation("根据 ID 修改分组信息")
    @PutMapping("{id}")
    public Result updateCollectArticleGroupById(@PathVariable Long id, @RequestBody GroupFormDTO dto) {
        return collectArticleGroupService.updateGroupById(id, dto);
    }

}
