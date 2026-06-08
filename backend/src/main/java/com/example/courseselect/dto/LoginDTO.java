package com.example.courseselect.dto;

/**
 * 登录请求参数
 * 封装用户登录时提交的用户名和密码
 */

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO { // 登录请求参数
        @NotBlank(message = "用户名不能为空") // 不能为空
        private String username;    // 用户名

        @NotBlank(message = "密码不能为空")
        private String password;    // 密码
}
