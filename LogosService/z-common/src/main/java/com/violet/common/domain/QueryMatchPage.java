package com.violet.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "带分页的查询条件实体")
public class QueryMatchPage extends QueryMatch {
    @ApiModelProperty(value = "当前页", required = false)
    private Integer page = 1;
    @ApiModelProperty(value = "每页数量", required = false)
    private Integer size = 20;
}
