package com.violet.user.domain.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户注册视图
 */
@Data
public class UserRegisterVO {
    private String phone;
    private String code;
    private String password;
    private String username;
    private String desc;
    private String gender;
    private MultipartFile headPortrait;
}
