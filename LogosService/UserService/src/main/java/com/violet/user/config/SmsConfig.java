package com.violet.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "logos.sms")
public class SmsConfig {
    private String regionId;
    private String accessKeyId;
    private String secret;
    private String signName;
    private String templateCode;
}
