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

public interface UserService {
    LoginResponse login(LoginDTO loginDTO);
    void register(RegisterDTO registerDTO);
    User getById(Long id);
    void updateProfile(Long userId, UpdateUserDTO updateUserDTO);
    void updatePassword(Long userId, String oldPassword, String newPassword);
    List<User> listUsers(String role);
    void toggleUserStatus(Long userId);
    User getCurrentUser();
}
