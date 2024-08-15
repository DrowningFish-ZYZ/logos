package com.violet.common.domain;

import com.violet.common.domain.dto.DTOTemplate;
import com.violet.common.exception.ParameterException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GroupFormDTO implements DTOTemplate {

    @ApiModelProperty(value = "组名", required = true)
    private String name;
    @ApiModelProperty(value = "描述", required = false)
    private String desc;

    public void verify() {
        if (name == null || name.trim().isEmpty() || name.trim().length() < 3)
            throw new ParameterException("组名不能为空, 且至少 3 个字");
        if (desc == null || desc.trim().isEmpty() || desc.trim().length() < 3)
            throw new ParameterException("描述不能为空, 且至少 3 个字");
    }

}
