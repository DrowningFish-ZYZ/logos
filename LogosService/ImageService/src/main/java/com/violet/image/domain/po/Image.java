package com.violet.image.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("image")
public class Image {
    /**
     * 图片 ID
     */
    private Long id;

    /**
     * 图片名
     */
    private String name;

    /**
     * 图片路径
     */
    private String src;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 图片分组 ID
     */
    private Long imageGroupId;

    private Long userId;
}
