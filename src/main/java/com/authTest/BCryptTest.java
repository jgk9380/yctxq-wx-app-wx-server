package com.authTest;

/**
 * Created by jianggk on 2017/7/4.
 */


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptTest {

//    public static void main1(String[] args) {
//        test1();
//    }

    public static void test1(){
        String pass = "hello";
        BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
        String hashPass = encode.encode(pass);
        System.out.println(hashPass);
       Boolean result = encode.matches(pass,"$2a$10$HEPUYjE/T2VOIjB5BIypyunUzvgQaxDmRmNFbe5t9C3vY24lJBdsu");
       System.out.println("resutl="+result);
        //如何验证密码一致
    }

    public  static  void test2(){
        int t = 0;
        String password = "123456";
        System.out.println(password + " -> ");
        for (t = 1; t <= 10; t++) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(password);
            System.out.println(hashedPassword);
        }

        password = "MIKE123";
        System.out.println(password + " -> ");
        for (t = 1; t <= 10; t++) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(password);
            System.out.println(hashedPassword);
        }
    }
}

