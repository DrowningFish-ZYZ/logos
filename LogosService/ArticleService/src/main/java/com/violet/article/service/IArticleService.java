package com.violet.article.service;

import com.violet.common.domain.QueryMatchPage;
import com.violet.common.domain.Result;

public interface IArticleService {
    Result getArticleById(Long id);

    Result getArticles(QueryMatchPage query);

    Result commentIncreaseById(Long id);

    Result getArticleByUserId(Long userId, QueryMatchPage query);
}
