package com.violet.common.interceptors;

import com.violet.common.utils.UserContext;
import org.springframework.web.servlet.HandlerInterceptor;

public class UserIDInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler) throws Exception {
        // 请求从 Header 中获取 用户 ID 数据并缓存到本地线程
        String userid = request.getHeader("userid");
        // 如果数据存在就缓存
        if (userid != null && !userid.isEmpty()) {
            UserContext.setUserId(Long.parseLong(userid));
        }
        return true;
    }

    @Override
    public void afterCompletion(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求结束、释放用户数据
        UserContext.removeUserId();
    }
}
