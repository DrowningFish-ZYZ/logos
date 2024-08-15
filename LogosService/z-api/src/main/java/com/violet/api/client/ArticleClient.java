package com.violet.api.client;

import com.violet.common.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("article-service")
public interface ArticleClient {

    @GetMapping("/api/articles/{id}")
    Result getArticleById(@PathVariable Long id);

    @PutMapping("/api/articles/{id}")
    Result commentIncreaseById(@PathVariable Long id);

}
