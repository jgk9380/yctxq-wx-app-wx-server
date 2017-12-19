package com.wx.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/wxf")
//处理angular路由
public class WebController  {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 默认处理/web下说有的请求，全部转发到index.html
     * @param request
     * @param response
     */
//    @RequestMapping("**")
//    public void routes(HttpServletRequest request , HttpServletResponse response) {
//        request.setAttribute("routes","路由跳转");
//        try {
//            // 此处路径要打两点，如果直接写 index.html 会循环反问/web/index.html 造成死循环
//            request.getRequestDispatcher("../index.html").forward(request,response);
//        } catch (Exception es) {
//            logger.error("路由失败",es);
//        }
//    }

}

