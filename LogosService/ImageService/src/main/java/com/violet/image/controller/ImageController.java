package com.violet.image.controller;

import com.violet.common.domain.Result;
import com.violet.common.domain.QueryMatch;
import com.violet.image.service.IImageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final IImageService imageService;

    @ApiOperation("创建图片接口")
    @PostMapping
    public Result createImage(
        @RequestParam("name") String name,
        @RequestParam("groupID") Long groupID,
        @RequestParam("file") MultipartFile file
    ) {
        return imageService.createImage(name, groupID, file);
    }

    @ApiOperation("获取图片")
    @GetMapping
    public Result getImages(@Validated QueryMatch query) {
        return imageService.getImagesByQuery(query);
    }

    @ApiOperation("根据图片ID删除图片")
    @DeleteMapping("/{id}")
    public Result deleteImage(@PathVariable Long id) {
        return imageService.deleteByID(id);
    }

}
