package com.violet.article.domain.dto;

import com.violet.common.domain.dto.DTOTemplate;
import com.violet.common.exception.ParameterException;
import lombok.Data;

@Data
public class CollectGroupAndArticleFormDTO implements DTOTemplate {
    private Long groupId;
    private Long articleId;

    @Override
    public void verify() {
        if (groupId == null || groupId < 1) throw new ParameterException("分组是必须要的");
        if (articleId == null || articleId < 1) throw new ParameterException("异常文章");
    }
}
