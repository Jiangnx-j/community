package com.jiangnx.community.config;

import com.jiangnx.community.interceptor.LoginRequiredInterceptor;
import com.jiangnx.community.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConf implements WebMvcConfigurer {

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor).
                excludePathPatterns("/*/*.css","/*/*.js","/*/*.png","/*/*.jpg","/*/*.jpeg");

        registry.addInterceptor(loginRequiredInterceptor).
                excludePathPatterns("/*/*.css","/*/*.js","/*/*.png","/*/*.jpg","/*/*.jpeg");
    }
}
