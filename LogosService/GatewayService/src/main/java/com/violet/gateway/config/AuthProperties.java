package com.violet.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "logos.auth")
public class AuthProperties {
    private List<String> includePaths;        // 哪些路由需要登录才能访问
    private List<String> excludePaths;        // 哪些路由不需要登录也能访问
}
