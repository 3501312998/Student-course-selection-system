package com.example.courseselect.security;

/**
 * 用户详情加载服务
 * Spring Security 认证时从数据库加载用户信息，构建 SecurityUser
 */

import com.example.courseselect.entity.User;
import com.example.courseselect.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService { // 用户详情加载服务

        private final UserMapper userMapper; // 用户数据访问

    @Override
        /** 根据用户名加载用户认证信息 */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userMapper.findByUsername(username); // 从数据库查询用户
                if (user == null) {
                        throw new UsernameNotFoundException("用户不存在"); // 用户不存在
        }

                // 构建 SecurityUser 对象，供 Spring Security 使用
        securityUser.setId(user.getId());
        securityUser.setUsername(user.getUsername());
        securityUser.setPassword(user.getPassword());
        securityUser.setRealName(user.getRealName());
        securityUser.setRole(user.getRole());
        securityUser.setEnabled(user.getStatus() == 1);
        return securityUser;
    }
}
