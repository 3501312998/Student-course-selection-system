package com.example.courseselect.dto;

/**
 * 登录响应参数
 * 封装登录成功后返回给前端的令牌和用户基本信息
 */

import lombok.Data;

@Data
public class LoginResponse { // 登录响应参数
        private String token;        // JWT 令牌
        private String tokenType = "Bearer"; // 令牌类型
        private Long userId;         // 用户 ID
        private String username;     // 用户名
        private String realName;     // 真实姓名
        private String role;         // 角色

        public LoginResponse(String token, Long userId, String username, String realName, String role) { // 全参构造
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.realName = realName;
        this.role = role;
    }
}
