package com.violet.api.domain.vo;

import lombok.Data;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String headPortrait;
    private String desc;
    private Integer gender;
    private Integer status;
}
