package com.violet.user.domain.dto;

import com.violet.common.domain.dto.DTOTemplate;
import com.violet.common.exception.ParameterException;
import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class UserRegisterDTO implements DTOTemplate {

    private String phone;
    private String username;
    private String password;
    private String code;
    private String info;
    private Integer gender;

    @Override
    public void verify() {
        if (!validateMobileNumber(phone)) throw new ParameterException("手机号码不正确");
        if (username == null || username.trim().isEmpty()) throw new ParameterException("用户名不能为空");
        if (password == null || password.trim().isEmpty()) throw new ParameterException("密码不能为空");
        if (code == null || code.trim().isEmpty()) throw new ParameterException("验证码不能为空");
        if (gender == null || gender < 0 || gender > 1) throw new ParameterException("错误的性别");
    }

    public static boolean validateMobileNumber(String mobileNumber) {
        // 手机号码正则表达式
        String regex = "^1[3-9]\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mobileNumber);
        return matcher.matches();
    }
}
