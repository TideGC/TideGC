package com.example.employee.config.openfeign;

import feign.RequestInterceptor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.example.employee.http")
public class OpenFeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new JwtFeignRequestInterceptor();
    }

}
