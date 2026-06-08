package com.example.courseselect.dto;

/**
 * 用户信息更新请求参数
 * 封装用户修改个人信息时提交的姓名、邮箱、电话等
 */

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String realName;
    private String email;
    private String phone;
    private Integer gender;
}
