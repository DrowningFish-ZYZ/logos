package com.violet.api.client;

import com.violet.common.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("comment-service")
public interface CommentClient {

    @DeleteMapping("/api/comments/{articleId}")
    Result deleteCommentByArticleId(@PathVariable Long articleId);

}
