package com.violet.comment.service;

import com.violet.comment.domain.dto.CommentFormDTO;
import com.violet.common.domain.Result;

public interface ICommentService {
    Result createComment(CommentFormDTO dto);

    Result getCommentsByArticleId(Long articleId);

    Result deleteCommentByArticleId(Long articleId);
}
