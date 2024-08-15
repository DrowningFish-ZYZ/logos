package com.violet.article.domain.dto;

import com.violet.common.domain.dto.DTOTemplate;
import com.violet.common.exception.ParameterException;
import lombok.Data;

@Data
public class CollectArticleFormDTO implements DTOTemplate {

    private Long groupId;
    private Long articleId;

    @Override
    public void verify() {
        if (groupId == null || articleId == null) throw new ParameterException("异常收藏");
    }
}
