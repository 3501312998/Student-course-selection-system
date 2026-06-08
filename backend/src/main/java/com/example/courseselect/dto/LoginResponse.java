package com.example.courseselect.dto;

/**
 * 登录响应参数
 * 封装登录成功后返回给前端的令牌和用户基本信息
 */

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String username;
    private String realName;
    private String role;

    public LoginResponse(String token, Long userId, String username, String realName, String role) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.realName = realName;
        this.role = role;
    }
}
