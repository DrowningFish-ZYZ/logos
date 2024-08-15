package com.violet.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum UserGenderStatus {
    MALE(0, "男性"),
    FEMALE(1, "女性");

    @EnumValue
    int value;
    String desc;

    UserGenderStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static UserGenderStatus of(int value) {
        if (value == 0) {
            return MALE;
        }
        if (value == 1) {
            return FEMALE;
        }
        throw new RuntimeException("账户状态错误");
    }
}
