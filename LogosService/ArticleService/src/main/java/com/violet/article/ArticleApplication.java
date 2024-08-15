package com.violet.article;

import com.violet.api.config.DefaultConfig;
import com.violet.common.config.MVCConfig;
import com.violet.common.config.MybatisPlusConfig;
import org.modelmapper.ModelMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({MVCConfig.class, MybatisPlusConfig.class})
@MapperScan("com.violet.article.mapper")
@SpringBootApplication
@EnableFeignClients(value = "com.violet.api.client", defaultConfiguration = DefaultConfig.class)
public class ArticleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
