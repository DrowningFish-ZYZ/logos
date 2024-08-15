package com.violet.article.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("collectGroup_and_article")
public class CollectGroupAndArticle {
    private Long id;
    private Long groupId;
    private Long articleId;
    private LocalDateTime createTime;
}
