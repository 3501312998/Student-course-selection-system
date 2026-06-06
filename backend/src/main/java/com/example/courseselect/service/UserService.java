package com.example.courseselect.service;

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
