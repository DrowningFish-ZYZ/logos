package com.violet.article.domain.vo;

import com.violet.api.domain.vo.ArticleVO;
import com.violet.api.domain.vo.UserVO;
import com.violet.common.domain.PageData;
import lombok.Data;

import java.util.List;

@Data
public class UserArticleVO {
    private UserVO user;                            // 用户的信息
    private Integer allArticleCount;                // 发布的所有文章数
    private Integer allArticleCollectCount;         // 发布的所有文章的收藏数
    private PageData data;                          // 符合条件的文章
}
