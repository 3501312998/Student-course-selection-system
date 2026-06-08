package com.example.courseselect.dto;

/**
 * 注册请求参数
 * 封装用户注册时提交的各项信息，含参数校验规则
 */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO { // 注册请求参数
    @NotBlank(message = "用户名不能为空")
        @Size(min = 3, max = 50, message = "用户名长度3-50位")
        private String username;    // 用户名

    @NotBlank(message = "密码不能为空")
        @Size(min = 6, max = 50, message = "密码长度6-50位")
        private String password;    // 密码

        @NotBlank(message = "真实姓名不能为空")
        private String realName;    // 真实姓名

        private String email;       // 邮箱（选填）

        private String phone;       // 手机号（选填）

        private Integer gender;     // 性别

        @NotBlank(message = "学号不能为空")
        private String studentNo;   // 学号
}
