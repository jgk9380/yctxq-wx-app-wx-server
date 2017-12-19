package com.control;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller

public class MainController {
    @ResponseBody
    @RequestMapping("/testjwt1")
    public Map testJwt() {
        Map map = new HashMap<>();
        map.put("data", "abc");
        return map;
    }
//    在 @PreAuthorize 中我们可以利用内建的 SPEL 表达式：比如 'hasRole()' 来决定哪些用户有权访问。
//            * 需注意的一点是 hasRole 表达式认为每个角色名字前都有一个前缀 'ROLE_'。所以这里的 'ADMIN' 其实在
// * 数据库中存储的是 'ROLE_ADMIN' 。这个 @PreAuthorize 可以修饰Controller也可修饰Controller中的方法。
    @ResponseBody
    @RequestMapping("/testjwt2")
    @PreAuthorize("hasRole('MONTH_ADMIN')")
    public Map testJwt2() {
        Map map = new HashMap<>();
        map.put("data", "abc2");
        return map;
    }


//    @RequestMapping("/")
//    public String main() {
//        return "index";
//    }

}


//        安全表达式　	计算结果
//        authentication　　	用户认证对象
//        denyAll　　	结果始终为false
//        hasAnyRole(list of roles)　　	如果用户被授权指定的任意权限，结果为true
//        hasRole(role)	如果用户被授予了指定的权限，结果 为true
//        hasIpAddress(IP Adress)	用户地址
//        isAnonymous()　　	是否为匿名用户
//        isAuthenticated()　　	不是匿名用户
//        isFullyAuthenticated　　	不是匿名也不是remember-me认证
//        isRemberMe()　　	remember-me认证
//        permitAll	始终true
//        principal	用户主要信息对象