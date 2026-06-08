package com.example.courseselect.service.impl;

/**
 * 用户服务实现类
 * 实现用户注册登录、信息管理、密码修改等核心业务逻辑
 */

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.courseselect.common.ResultCode;
import com.example.courseselect.dto.LoginDTO;
import com.example.courseselect.dto.LoginResponse;
import com.example.courseselect.dto.RegisterDTO;
import com.example.courseselect.dto.UpdateUserDTO;
import com.example.courseselect.entity.User;
import com.example.courseselect.exception.BusinessException;
import com.example.courseselect.mapper.UserMapper;
import com.example.courseselect.security.JwtTokenProvider;
import com.example.courseselect.security.SecurityUser;
import com.example.courseselect.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService { // 用户服务实现

        private final UserMapper userMapper; // 用户数据访问
        private final PasswordEncoder passwordEncoder; // 密码加密器
        private final JwtTokenProvider jwtTokenProvider; // JWT 令牌工具

    @Override
        /** 用户登录：验证用户名密码，生成 JWT 令牌 */
    public LoginResponse login(LoginDTO loginDTO) {
                User user = userMapper.findByUsername(loginDTO.getUsername()); // 根据用户名查询用户
                if (user == null) {
                                    throw new BusinessException(ResultCode.PASSWORD_ERROR.getCode(), "用户名或密码错误"); // 密码错误 // 防止枚举：不提示用户是否存在
        }
                if (user.getStatus() == null || user.getStatus() == 0) {
                        throw new BusinessException(ResultCode.USER_DISABLED.getCode(), "账号已被禁用"); // 账号被禁用
        }
                if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) { // BCrypt 校验密码
            throw new BusinessException(ResultCode.PASSWORD_ERROR.getCode(), "用户名或密码错误");
        }

                String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername(), user.getRole()); // 生成 JWT
                return new LoginResponse(token, user.getId(), user.getUsername(), user.getRealName(), user.getRole()); // 返回令牌和用户信息
    }

    @Override
        /** 用户注册：检查唯一性，创建新用户 */
    public void register(RegisterDTO registerDTO) {
        // 检查用户名是否已存在
                if (userMapper.findByUsername(registerDTO.getUsername()) != null) { // 检查用户名是否已存在
                        throw new BusinessException(ResultCode.CONFLICT.getCode(), "用户名已存在");
        }
        // 检查学号是否已存在
                if (userMapper.findByStudentNo(registerDTO.getStudentNo()) != null) { // 检查学号是否已存在
            throw new BusinessException(ResultCode.CONFLICT.getCode(), "学号已存在");
        }

                // 构建用户实体，密码加密存储
                .username(registerDTO.getUsername())
                                .password(passwordEncoder.encode(registerDTO.getPassword())) // BCrypt 加密密码
                .realName(registerDTO.getRealName())
                .email(registerDTO.getEmail())
                .phone(registerDTO.getPhone())
                .gender(registerDTO.getGender())
                .studentNo(registerDTO.getStudentNo())
                                .role("STUDENT") // 注册默认为学生角色
                                .status(1) // 默认启用
                .build();
                userMapper.insert(user); // 保存到数据库
    }

    @Override
        /** 根据 ID 查询用户 */
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
        /** 更新用户个人信息 */
    public void updateProfile(Long userId, UpdateUserDTO updateUserDTO) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setRealName(updateUserDTO.getRealName());
        user.setEmail(updateUserDTO.getEmail());
        user.setPhone(updateUserDTO.getPhone());
        user.setGender(updateUserDTO.getGender());
        userMapper.updateById(user);
    }

    @Override
        /** 修改密码：需校验原密码 */
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
                if (!passwordEncoder.matches(oldPassword, user.getPassword())) { // 校验原密码
                        throw new BusinessException("原密码错误");
        }
                user.setPassword(passwordEncoder.encode(newPassword)); // 新密码加密存储
        userMapper.updateById(user);
    }

    @Override
        /** 查询用户列表（可选按角色筛选） */
    public List<User> listUsers(String role) {
                if (role != null && !role.isEmpty()) {
                        return userMapper.selectList(
                    new LambdaQueryWrapper<User>()
                                                        .eq(User::getRole, role)); // 按角色查询
        }
        return userMapper.selectList(null);
    }

    @Override
        /** 切换用户启用/禁用状态 */
    public void toggleUserStatus(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
                user.setStatus(user.getStatus() == 1 ? 0 : 1); // 1变0，0变1
        userMapper.updateById(user);
    }

    @Override
        /** 从 Spring Security 上下文中获取当前登录用户 */
    public User getCurrentUser() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // 获取认证信息
                if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
                        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
                        return userMapper.selectById(securityUser.getId()); // 根据 ID 查询完整用户信息
        }
                throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "未登录"); // 未登录或会话已过期
    }
}
