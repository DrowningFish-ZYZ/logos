package com.violet.article.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("article")
public class Article {
    /**
     * 文章 ID
     */
    private Long id;

    /**
     * 文章作者 ID
     */
    private Long authorId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章简介
     */
    @TableField("`desc`")
    private String desc;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章收藏数
     */
    private Integer collectCount;

    /**
     * 文章评论数
     */
    private Integer commentCount;

    /**
     * 创建日期
     */
    private LocalDateTime createTime;

    /**
     * 分组 ID
     */
    private Long publishArticleGroupId;
}
