package com.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jianggk on 2016/11/8.
 */
public class DemoIntecepter extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime=System.currentTimeMillis();
        request.setAttribute("startTime",startTime);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long endTime=System.currentTimeMillis();
       // long startTime=(Long)request.getAttribute("startTime");
        request.removeAttribute("startTime");
       // System.out.println("本次请求的时间为："+(endTime-startTime)/1000+"秒 path:"+ request.getPathInfo()+"   user:"+request.getRemoteUser());

    }
}
