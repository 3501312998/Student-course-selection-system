package com.example.courseselect.dto;

/**
 * 用户信息更新请求参数
 * 封装用户修改个人信息时提交的姓名、邮箱、电话等
 */

import lombok.Data;

@Data
public class UpdateUserDTO { // 用户信息更新参数
        private String realName;   // 真实姓名
        private String email;      // 邮箱
        private String phone;      // 手机号
        private Integer gender;    // 性别
}
