package com.violet.article.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CollectArticleGroup {
    private Long id;
    private Long userId;
    private String name;
    @TableField("`desc`")
    private String desc;
    private LocalDateTime createTime;
}
