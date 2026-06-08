package com.example.courseselect.controller;

/**
 * 认证控制器
 * 处理用户登录、注册、个人信息管理请求（无需特殊角色）
 */

import com.example.courseselect.common.Result;
import com.example.courseselect.dto.LoginDTO;
import com.example.courseselect.dto.LoginResponse;
import com.example.courseselect.dto.RegisterDTO;
import com.example.courseselect.dto.UpdateUserDTO;
import com.example.courseselect.entity.User;
import com.example.courseselect.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

        private final UserService userService; // 用户服务

    @PostMapping("/login")
        /** 用户登录 */
    public Result<LoginResponse> login(@Valid @RequestBody LoginDTO loginDTO) {
                return Result.success(userService.login(loginDTO)); // 返回令牌和用户信息
    }

    @PostMapping("/register")
        /** 用户注册 */
    public Result<Void> register(@Valid @RequestBody RegisterDTO registerDTO) {
                userService.register(registerDTO);
                return Result.success("注册成功", null);
    }

    @GetMapping("/me")
        /** 获取当前登录用户信息 */
    public Result<User> getCurrentUser() {
        return Result.success(userService.getCurrentUser());
    }

    @PutMapping("/profile")
        /** 更新个人信息 */
    public Result<Void> updateProfile(@Valid @RequestBody UpdateUserDTO updateUserDTO) {
                User user = userService.getCurrentUser(); // 获取当前登录用户
        userService.updateProfile(user.getId(), updateUserDTO);
        return Result.success("更新成功", null);
    }

    @PutMapping("/password")
        /** 修改密码 */
    public Result<Void> updatePassword(@RequestBody Map<String, String> body) {
        User user = userService.getCurrentUser();
        userService.updatePassword(user.getId(), body.get("oldPassword"), body.get("newPassword"));
        return Result.success("密码修改成功", null);
    }
}
