package com.violet.user.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户登录成功后返回的数据
 */
@Data
public class UserLoginVO {
    private Long id;
    private String token;
    private String username;
    private String headPortrait;
    private String phone;
    private String desc;
}
