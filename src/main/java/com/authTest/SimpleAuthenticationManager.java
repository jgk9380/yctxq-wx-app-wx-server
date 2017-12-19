package com.authTest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianggk on 2017/7/5.
 */

// 自定义验证方法
public class SimpleAuthenticationManager implements AuthenticationManager {
    static final List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();

    // 构建一个角色列表
    static {
        AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // 验证方法
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        // 这里我们自定义了验证通过条件：username与password相同就可以通过认证
        System.out.println(auth.getCredentials());
        if (auth.getName().equals(auth.getCredentials())) {
            return new UsernamePasswordAuthenticationToken(auth.getName(), auth.getCredentials(), AUTHORITIES);
        }
        // 没有通过认证则抛出密码错误异常
        throw new BadCredentialsException("Bad Credentials");
    }
}
