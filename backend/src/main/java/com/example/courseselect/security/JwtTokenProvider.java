package com.example.courseselect.security;

/**
 * JWT 令牌工具类
 * 负责生成、解析、校验 JWT 令牌，管理用户登录状态
 */

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider { // JWT 令牌工具类

        private final SecretKey secretKey; // 签名密钥
        private final long expiration;      // 过期时间（毫秒）

        /** 构造函数，从配置文件读取密钥和过期时间 */
    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration) {
                this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // 生成 HMAC-SHA 密钥
        this.expiration = expiration;
    }

        /** 生成 JWT 令牌，包含用户 ID、用户名、角色 */
    public String generateToken(Long userId, String username, String role) {
                Date now = new Date(); // 当前时间
                Date expiryDate = new Date(now.getTime() + expiration); // 过期时间

                // 构建 JWT，主题为用户 ID，携带用户名和角色声明
                                .subject(userId.toString()) // 主题：用户 ID
                                .claim("username", username) // 自定义声明：用户名
                                .claim("role", role) // 自定义声明：角色
                                .issuedAt(now) // 签发时间
                                .expiration(expiryDate) // 过期时间
                                .signWith(secretKey) // 使用 HMAC-SHA 签名
                                .compact(); // 生成令牌字符串
    }

        /** 从令牌中解析用户 ID */
    public Long getUserIdFromToken(String token) {
        Claims claims =             parseToken(token); // 尝试解析
        return Long.parseLong(claims.getSubject());
    }

        /** 从令牌中解析用户名 */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("username", String.class);
    }

        /** 从令牌中解析角色 */
    public String getRoleFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("role", String.class);
    }

        /** 验证令牌是否有效 */
    public boolean validateToken(String token) {
                try {
            parseToken(token);
                        return true;
                } catch (JwtException | IllegalArgumentException e) {
                        return false; // 解析失败则无效
        }
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
