package com.violet.gateway.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.violet.common.exception.UserLoginException;
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

    public static Long parseToken(String token) {
        if (token == null || token.trim().length() == 0) {
            throw new UserLoginException("没有 Token");
        }

        JWT jwt = JWTUtil.parseToken(token);

        // 校验 token 签证是否一致
        if (!jwt.setKey(KEY.getBytes()).verify()) {
            throw new UserLoginException("异常 Token");
        }

        // 校验 token 是否过期
        if (!jwt.validate(0)) {
            throw new UserLoginException("签证过期");
        }

        // 校验数据
        try {

            return Long.valueOf(jwt.getPayload("userID").toString());

        } catch (Exception e) {throw new UserLoginException("无效数据");}
    }
}
