package com.example.courseselect.security;

/**
 * JWT 认证过滤器
 * 从请求头中提取 JWT 令牌并验证，将认证信息注入 Spring Security 上下文
 */

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { // JWT 认证过滤器

        private final JwtTokenProvider jwtTokenProvider; // JWT 工具类
        private final UserDetailsServiceImpl userDetailsService; // 用户详情服务

    @Override
        /** 每次请求执行：从请求头提取 Token 并设置认证信息 */
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
                String token = getTokenFromRequest(request); // 从请求头提取 Token

                // 如果 Token 存在且有效，设置认证上下文
                        String username = jwtTokenProvider.getUsernameFromToken(token); // 从 Token 解析用户名
                        SecurityUser securityUser = (SecurityUser) userDetailsService.loadUserByUsername(username); // 加载用户信息

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
