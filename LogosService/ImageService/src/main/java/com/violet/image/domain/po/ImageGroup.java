package com.violet.image.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("image_group")
public class ImageGroup {
    /**
     * 分组 ID
     */
    private Long id;

    /**
     * 当前图片分组是属于哪一个用户的
     */
    private Long userId;

    /**
     * 组名
     */
    private String name;

    /**
     * 分组描述
     */
    @TableField("`desc`")
    private String desc;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
