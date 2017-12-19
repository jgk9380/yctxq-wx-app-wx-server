package com.control.login;

import com.entity.p.LoginUser;


public interface AuthService {
    LoginUser register(LoginUser userToAdd);
    String login(String username, String password);
    String refresh(String oldToken);
}