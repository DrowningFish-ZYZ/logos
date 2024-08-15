package com.violet.user.domain.vo;

import lombok.Data;

@Data
public class UserLoggedVO {
    private Long id;
    private String username;
    private String phone;
    private String desc;
    private Integer gender;
}
