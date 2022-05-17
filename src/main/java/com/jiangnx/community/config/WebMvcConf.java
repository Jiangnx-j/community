package com.jiangnx.community.config;

import com.jiangnx.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConf implements WebMvcConfigurer {

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Bean
    public LoginTicketInterceptor loginTicketInterceptor(){
        return new LoginTicketInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor).
                excludePathPatterns("/*/*.css","/*/*.js","/*/*.png","/*/*.jpg","/*/*.jpeg");
    }
}
