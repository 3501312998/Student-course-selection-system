package com.example.courseselect.entity;

/**
 * 用户实体类
 * 对应数据库 t_user 表，存储系统用户（管理员/教师/学生）信息
 */

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user") // 用户表，存储管理员/教师/学生
public class User {
        @TableId(type = IdType.AUTO) // 自增主键
        private Long id;                   // 用户 ID

        private String username;            // 用户名（唯一）

        private String password;            // BCrypt 加密密码

        private String realName;            // 真实姓名

        private String email;               // 邮箱

        private String phone;               // 手机号

        private String role;                // 角色: ADMIN/TEACHER/STUDENT

        private Integer gender;             // 性别: 0未知 1男 2女

        private String studentNo;           // 学号/工号

        private Integer status;             // 状态: 1启用 0禁用

        @TableField(fill = FieldFill.INSERT) // 插入时自动填充
        private LocalDateTime createTime;   // 创建时间

        @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时自动填充
        private LocalDateTime updateTime;   // 更新时间

        @TableLogic // 逻辑删除注解
        private Integer deleted;            // 逻辑删除: 0未删 1已删
}
