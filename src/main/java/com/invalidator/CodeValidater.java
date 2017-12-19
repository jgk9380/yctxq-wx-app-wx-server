package com.invalidator;

//用map根据用户名或tele保存用户验证码
public interface CodeValidater {
   // String generateAuthCode(String userName);//根据用户名或手机号获取验证码。短信方式或图片方式。
    String getCode(String userName);
    boolean authCode(String userName,String code);
    // getDate();
    // shrinkMap();
}

