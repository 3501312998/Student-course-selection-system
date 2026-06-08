package com.example.courseselect.dto;

/**
 * 注册请求参数
 * 封装用户注册时提交的各项信息，含参数校验规则
 */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度3-50位")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 50, message = "密码长度6-50位")
    private String password;

    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    private String email;

    private String phone;

    private Integer gender;

    @NotBlank(message = "学号不能为空")
    private String studentNo;
}
