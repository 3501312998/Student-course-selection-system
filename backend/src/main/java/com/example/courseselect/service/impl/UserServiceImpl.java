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
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public LoginResponse login(LoginDTO loginDTO) {
        User user = userMapper.findByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR.getCode(), "用户名或密码错误");
        }
        if (user.getStatus() == null || user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_DISABLED.getCode(), "账号已被禁用");
        }
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR.getCode(), "用户名或密码错误");
        }

        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername(), user.getRole());
        return new LoginResponse(token, user.getId(), user.getUsername(), user.getRealName(), user.getRole());
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        if (userMapper.findByUsername(registerDTO.getUsername()) != null) {
            throw new BusinessException(ResultCode.CONFLICT.getCode(), "用户名已存在");
        }
        // 检查学号是否已存在
        if (userMapper.findByStudentNo(registerDTO.getStudentNo()) != null) {
            throw new BusinessException(ResultCode.CONFLICT.getCode(), "学号已存在");
        }

        User user = User.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .realName(registerDTO.getRealName())
                .email(registerDTO.getEmail())
                .phone(registerDTO.getPhone())
                .gender(registerDTO.getGender())
                .studentNo(registerDTO.getStudentNo())
                .role("STUDENT")
                .status(1)
                .build();
        userMapper.insert(user);
    }

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
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
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
    }

    @Override
    public List<User> listUsers(String role) {
        if (role != null && !role.isEmpty()) {
            return userMapper.selectList(
                    new LambdaQueryWrapper<User>()
                            .eq(User::getRole, role));
        }
        return userMapper.selectList(null);
    }

    @Override
    public void toggleUserStatus(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setStatus(user.getStatus() == 1 ? 0 : 1);
        userMapper.updateById(user);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
            return userMapper.selectById(securityUser.getId());
        }
        throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "未登录");
    }
}
