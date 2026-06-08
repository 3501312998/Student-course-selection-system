package com.example.courseselect.service;

/**
 * 用户服务接口
 * 定义用户注册、登录、信息更新、密码修改等业务方法
 */

import com.example.courseselect.dto.LoginDTO;
import com.example.courseselect.dto.LoginResponse;
import com.example.courseselect.dto.RegisterDTO;
import com.example.courseselect.dto.UpdateUserDTO;
import com.example.courseselect.entity.User;

import java.util.List;

public interface UserService { // 用户服务接口
        LoginResponse login(LoginDTO loginDTO); // 登录
        void register(RegisterDTO registerDTO); // 注册
        User getById(Long id); // 根据 ID 查询用户
        void updateProfile(Long userId, UpdateUserDTO updateUserDTO); // 更新个人信息
        void updatePassword(Long userId, String oldPassword, String newPassword); // 修改密码
        List<User> listUsers(String role); // 查询用户列表（可按角色筛选）
        void toggleUserStatus(Long userId); // 切换用户启用/禁用状态
        User getCurrentUser(); // 获取当前登录用户的信息
}
