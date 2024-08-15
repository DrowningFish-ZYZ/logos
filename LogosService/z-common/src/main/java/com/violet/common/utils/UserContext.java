package com.violet.common.utils;

import com.violet.common.exception.UserLoginException;

public class UserContext {

    private static final ThreadLocal<Long> tl = new ThreadLocal<>();

    /**
     * 保存当前登录用户 ID 到 ThreadLocal
     * @param userId 用户 ID
     */
    public static void setUserId(Long userId) {
        tl.set(userId);
    }

    /**
     * 获取当前登录用户的 ID
     * @return 用户 ID
     */
    public static Long getUserId() {
        return tl.get();
    }

    /**
     * 移除当前登录用户 ID
     */
    public static void removeUserId() {
        tl.remove();
    }

    /**
     * 如果当前用户未登录则返回 true
     */
    public static boolean notLoggedIn() {
        return tl.get() == null;
    }

    /**
     * 校验用户是否登录
     */
    public static void verify() {
        if (notLoggedIn()) throw new UserLoginException("用户未登录");
    }
}
