package com.ateupeonding.contentservice.configuration;

import io.minio.MinioClient;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioClientConfiguration {

    @Autowired
    private MinioClientConfigurationProperties properties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .endpoint(HttpUrl.get(properties.getUrl()))
                .build();
    }

}
