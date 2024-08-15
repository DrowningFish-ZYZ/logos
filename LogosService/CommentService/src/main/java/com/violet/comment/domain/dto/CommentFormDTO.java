package com.violet.comment.domain.dto;

import com.violet.common.domain.dto.DTOTemplate;
import com.violet.common.exception.ParameterException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("评论提交实体类")
public class CommentFormDTO implements DTOTemplate {

    @ApiModelProperty(value = "评论内容", required = true)
    private String content;
    @ApiModelProperty(value = "文章ID", required = true)
    private Long articleId;

    @Override
    public void verify() {
        if (content == null || content.trim().isEmpty()) throw new ParameterException("评论内容不能为空");
        if (articleId == null || articleId < 1) throw new ParameterException("异常文章");
    }
}
