package com.violet.image.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "logos.upload")
@Component
@Data
public class UploadConfig {
    private String directory;
    private String http;
}
