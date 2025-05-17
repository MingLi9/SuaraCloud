package com.SuaraCloud.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/users")
                .addPathPatterns("/users/**") // Apply to all API endpoints
                .excludePathPatterns("/users/token/**") // Exclude tokenId to userId functionality
                .excludePathPatterns("/users/health"); // Exclude health check
    }
}