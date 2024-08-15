package com.violet.user.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户登录表单实体")
public class UserLoginFormDTO {
    @ApiModelProperty(value = "手机号", required = true)
    private String phone;
    @ApiModelProperty(value = "验证码", required = false)
    private String code;
    @ApiModelProperty(value = "密码", required = false)
    private String password;
}
