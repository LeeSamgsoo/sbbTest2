package com.example.demo;

import com.example.demo.user.UserInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private UserInterceptor userInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 인터셉터를 적용할 URL 경로 설정
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/article/create/**", "article/modify/**", "article/delete/**")
                .excludePathPatterns("/css/**", "/js/**")
        ;
    }
}

