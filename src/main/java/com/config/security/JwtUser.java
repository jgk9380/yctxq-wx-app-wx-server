package com.config.security;

import com.entity.p.LoginUser;
import com.entity.p.SystemRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;


/**
 * 默认密码为空字符串  , 并且设置为启用,没有锁定 . 没有过期.
 */
public class JwtUser implements UserDetails {


   private LoginUser loginUser;

    public JwtUser(LoginUser loginUser) {
        this.loginUser = loginUser;
        //        this.authorities = authorities;
        //        this.enabled = enabled;
        //        this.lastPasswordResetDate = lastPasswordResetDate;
    }


    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> result = new ArrayList<>();
        List<SystemRole> lsr = loginUser.getUserRoles();
        for (SystemRole sr : lsr) {
            SimpleGrantedAuthority ca = new SimpleGrantedAuthority(sr.getName());
            result.add(ca);
        }
        return result;
    }

    @Override
    public String getPassword() {
        return this.loginUser.getPassword();
    }

    @Override
    public String getUsername() {
        return this.loginUser.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.loginUser.getIsValid();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.loginUser.getIsValid();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.loginUser.getIsValid();
    }

    @Override
    public boolean isEnabled() {
        return this.loginUser.getIsValid();
    }

}


