package com.violet.comment.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.violet.common.domain.Result;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaffeineConfig {

    /**
     * 评论缓存
     * 最大容量：10000
     * 初始容量：100
     */
    @Bean
    public Cache<Long, Result> commentCache() {
        return Caffeine.newBuilder()
            .maximumSize(10000)
            .initialCapacity(100)
            .build();
    }

}
