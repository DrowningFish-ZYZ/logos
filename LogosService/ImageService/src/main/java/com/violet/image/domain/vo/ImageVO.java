package com.violet.image.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImageVO {
    private Long id;
    private String src;
    private String name;
    private LocalDateTime createTime;
}
