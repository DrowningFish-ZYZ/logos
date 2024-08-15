package com.violet.api.config;

import com.violet.common.utils.UserContext;
import feign.Client;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;

public class DefaultConfig {
    @Bean
    public Logger.Level feignLogLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public RequestInterceptor feignRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                Long userid = UserContext.getUserId();
                if (userid != null) {
                    template.header("userid", userid.toString());
                }
            }
        };
    }

//    @Bean
//    public Client feignClient() {
//        return new OkHttpClient();
//    }
}
