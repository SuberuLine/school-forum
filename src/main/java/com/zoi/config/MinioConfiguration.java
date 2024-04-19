package com.zoi.config;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MinioConfiguration {

    @Value("${spring.minio.endpoint}")
    String endpoint;

    @Value("${spring.minio.user}")
    String user;

    @Value("${spring.minio.password}")
    String password;

    @Bean
    public MinioClient minioClient() {
        log.info("initialize minio client...");
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(user, password)
                .build();
    }

}
