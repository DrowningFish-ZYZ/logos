package com.violet.article.service;

import com.violet.article.domain.dto.CollectArticleFormDTO;
import com.violet.article.domain.dto.CollectGroupAndArticleFormDTO;
import com.violet.common.domain.QueryMatchPage;
import com.violet.common.domain.Result;

public interface ICollectArticleService {
    Result createCollectArticle(CollectGroupAndArticleFormDTO dto);

    Result isCollectArticle(Long articleId);

    Result deleteCollectArticle(CollectArticleFormDTO dto);

    Result getCollectArticles(QueryMatchPage query);

    Result deleteCollectArticleById(Long articleId);
}
