package com.violet.api.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleVO {
    private Long id;
    private UserVO user;
    private String title;
    private String desc;
    private String content;
    private Integer collectCount;
    private Integer commentCount;
    private LocalDateTime createTime;
    private Long publishArticleGroupId;
}
