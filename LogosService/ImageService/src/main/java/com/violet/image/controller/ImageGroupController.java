package com.violet.image.controller;

import com.violet.common.domain.GroupFormDTO;
import com.violet.common.domain.Result;
import com.violet.common.enums.ResponseCode;
import com.violet.image.service.impl.ImageGroupServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/imageGroups")
@RequiredArgsConstructor
public class ImageGroupController {

    private final ImageGroupServiceImpl imageGroupService;

    @ApiOperation("创建分组接口")
    @PostMapping
    public Result createGroup(@RequestBody @Validated GroupFormDTO dto) {
        return imageGroupService.createImageGroup(dto);
    }

    @ApiOperation("获取登录用户的所有图片分组数据")
    @GetMapping
    public Result getAllGroup() {
        return imageGroupService.getAllImageGroup();
    }

    @ApiOperation("根据ID获取对应分组数据")
    @GetMapping("/{id}")
    public Result getGroupById(@PathVariable Long id) {
        return imageGroupService.getGroupById(id);
    }

    @ApiOperation("根据ID修改分组的数据")
    @PutMapping("/{id}")
    public Result updateGroup(@RequestBody @Validated GroupFormDTO dto, @PathVariable Long id) {
        return imageGroupService.updateImageGroup(id, dto);
    }

    @ApiOperation("删除对应分组及其下的所有图片")
    @DeleteMapping("/{id}")
    public Result deleteGroup(@PathVariable Long id) {
        return imageGroupService.deleteImageGroupById(id);
    }

}
