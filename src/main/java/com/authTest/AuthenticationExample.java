package com.authTest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by jianggk on 2017/7/5.
 */

public class AuthenticationExample {

    private static AuthenticationManager am = new SimpleAuthenticationManager();

    public static void test(String[] args) throws Exception {

        String name = "1";
        String password = "1";
        try {
            // request就是第一步，使用name和password封装成为的token
            Authentication request = new UsernamePasswordAuthenticationToken(name, password);
            System.out.println(request.isAuthenticated()+" "+request.toString());
            // 将token传递给Authentication进行验证
            Authentication result = am.authenticate(request);
            System.out.println(result.isAuthenticated()+" "+result.toString());
            SecurityContextHolder.getContext().setAuthentication(result);

        } catch (AuthenticationException e) {
            System.out.println("认证失败：" + e.getMessage());
        }
        System.out.println("认证成功，Security context 包含：" + SecurityContextHolder.getContext().getAuthentication());
    }
}
