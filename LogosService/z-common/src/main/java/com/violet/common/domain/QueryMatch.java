package com.violet.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "查询条件实体")
public class QueryMatch {
    @ApiModelProperty(value = "对应分组", required = false)
    private Long groupId;
    @ApiModelProperty(value = "排序方式：{createTime, }", required = false)
    private String sort;
    @ApiModelProperty(value = "按和标题查询", required = false)
    private String keyword;
}
