package com.violet.user.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtils {
    // 密钥
    private static final String KEY = "Violet12306";

    public static String createToken(Long userID) {
        Map<String, Object> payload = new HashMap<String, Object>();
        DateTime now = DateTime.now();
        DateTime expTime = now.offsetNew(DateField.HOUR, 24);

        payload.put("userID", userID);                          // 负载数据
        payload.put(JWTPayload.ISSUED_AT, now);                 // 签发时间
        payload.put(JWTPayload.EXPIRES_AT, expTime);            // 过期时间
        payload.put(JWTPayload.NOT_BEFORE, now);                // 生效时间

        return JWTUtil.createToken(payload, KEY.getBytes());
    }
}
