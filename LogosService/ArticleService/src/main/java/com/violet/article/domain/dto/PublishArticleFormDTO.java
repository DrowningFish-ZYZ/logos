package com.violet.article.domain.dto;

import com.violet.common.domain.dto.DTOTemplate;
import com.violet.common.exception.ParameterException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "发布文章提交实体")
public class PublishArticleFormDTO implements DTOTemplate {
    @ApiModelProperty(value = "标题", required = true)
    private String title;
    @ApiModelProperty(value = "内容", required = true)
    private String content;
    @ApiModelProperty(value = "描述", required = true)
    private String desc;
    @ApiModelProperty(value = "分组", required = true)
    private Long publishArticleGroupId;

    @Override
    public void verify() {
        if (title == null || title.trim().isEmpty()) throw new ParameterException("标题不能为空");
        if (content == null || content.trim().isEmpty()) throw new ParameterException("内容不能为空");
        if (desc == null || desc.trim().isEmpty()) throw new ParameterException("描述不能为空");
        if (publishArticleGroupId == null || publishArticleGroupId < 1) throw new ParameterException("异常的分组");
    }
}
