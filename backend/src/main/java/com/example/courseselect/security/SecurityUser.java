package com.example.courseselect.security;

/**
 * Spring Security 认证用户类
 * 实现 UserDetails 接口，封装当前登录用户的认证信息
 */

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
public class SecurityUser implements UserDetails { // Spring Security 认证用户

        private Long id;                 // 用户 ID
        private String username;          // 用户名
        private String password;          // 加密密码
        private String realName;          // 真实姓名
        private String role;              // 角色
        private boolean enabled;          // 账号是否启用

    @Override
        /** 返回用户角色权限 */
    public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)); // 角色前缀 ROLE_
    }

    @Override
        /** 账号未过期 */
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
        /** 账号未锁定 */
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
        /** 凭证未过期 */
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
        /** 账号是否启用 */
    public boolean isEnabled() {
        return enabled;
    }
}
