package com.violet.gateway.filters;

import com.violet.common.exception.UserLoginException;
import com.violet.gateway.config.AuthProperties;
import com.violet.gateway.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/***
 * 全局用户拦截
 */
@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.判断当前路由是否需要用户登录后才能使用
        ServerHttpRequest request = exchange.getRequest();

        if (isExclude(request.getPath().toString())) {
            // 不需要拦截、放行
            return chain.filter(exchange);
        }

        // 2.如果是需要登录的路由、获取 token
        String token = request.getHeaders().getFirst("Authorization");
        // 解析并校验 token
        Long userID = null;
        try {
            userID = JWTUtils.parseToken(token);
        } catch (UserLoginException e) {
            // 解析失败、不予请求、设置响应状态码 401
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            System.out.println(e);
            return response.setComplete();
        }

        // 3.将数据缓存在头信息、转发给对应路由
        String userinfo = userID.toString();
        ServerWebExchange newExchange = exchange.mutate()
                .request(builder -> builder.header("userid", userinfo))
                .build();
        return chain.filter(newExchange);
    }

    /**
     * 判断路径是不是在 excludePaths 当中
     * @param path
     * @return 如果在返回 true、不在返回 false
     */
    private boolean isExclude(String path) {
        for (String excludePath : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(excludePath, path))
                return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
