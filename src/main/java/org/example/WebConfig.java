package org.example;

import org.example.interceptor.Base64HeaderInterceptor;
import org.example.interceptor.RoleCheckingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Base64HeaderInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new RoleCheckingInterceptor()).addPathPatterns("/**");
    }
}