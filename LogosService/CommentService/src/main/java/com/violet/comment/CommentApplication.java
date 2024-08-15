package com.violet.comment;

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
@MapperScan("com.violet.comment.mapper")
@SpringBootApplication
@EnableFeignClients(value = "com.violet.api.client", defaultConfiguration = DefaultConfig.class)
public class CommentApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommentApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
