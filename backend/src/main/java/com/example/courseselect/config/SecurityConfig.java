package com.example.courseselect.config;

import com.example.courseselect.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 安全配置
 * 配置接口权限规则、JWT 无状态认证、密码加密策略
 */
@Configuration // 安全配置类
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter; // JWT 认证过滤器

    @Bean
        /** 配置 Spring Security 过滤链 */
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                        .csrf(csrf -> csrf.disable()) // 禁用 CSRF（无状态 JWT 不需要）
                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 无状态会话
            .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/auth/**").permitAll() // 登录/注册接口放行
                                .requestMatchers(HttpMethod.GET, "/api/courses/**").permitAll() // 课程查询公开
                                .requestMatchers("/api/admin/**").hasRole("ADMIN") // 管理员接口
                                .requestMatchers("/api/teacher/**").hasAnyRole("TEACHER", "ADMIN") // 教师接口
                                .anyRequest().authenticated() // 其余接口需登录
            )
                        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JWT 过滤器在用户名密码认证前

        return http.build();
    }

    @Bean
        /** BCrypt 密码加密器 */
    public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder(); // 使用 BCrypt 加密
    }

    @Bean
        /** 认证管理器 */
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
