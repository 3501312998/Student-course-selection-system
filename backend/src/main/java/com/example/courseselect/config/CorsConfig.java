package com.example.courseselect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * 跨域配置
 * 允许前端跨域请求后端 API，开发环境前后端分离部署时必需
 */
@Configuration // 标记为配置类
public class CorsConfig {

    @Bean
        /** 创建 CORS 过滤器，允许前端跨域访问 */
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOriginPatterns(Arrays.asList("*")); // 允许所有来源
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 允许的 HTTP 方法
                config.setAllowedHeaders(Arrays.asList("*")); // 允许所有请求头
                config.setAllowCredentials(true); // 允许携带 Cookie
                config.setMaxAge(3600L); // 预检请求缓存时间 1 小时

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config); // 对所有路径生效
        return new CorsFilter(source);
    }
}
