package com.config.cors;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jianggk on 2017/1/24.
 */
//@Component
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init CorsFilter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String origin = (String) servletRequest.getRemoteHost() + ":" + servletRequest.getRemotePort();
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,HEAD,PUT,PATCH,GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Accept,Authorization,x-requested-with");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        response.setStatus(200);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}


//SPRING 4中更优雅的办法
//        SpringMVC4提供了非常方便的实现跨域的方法。在requestMapping中使用注解。
//@CrossOrigin(origins = “http://kbiao.me”)
//        全局实现 .定义类继承WebMvcConfigurerAdapter,设置跨域相关的配置
//        public class CorsConfigurerAdapter extends WebMvcConfigurerAdapter{
//
//        @Override
//        public void addCorsMappings(CorsRegistry registry) {
//
//        registry.addMapping("/api/*").allowedOrigins("*");
//        }
//        }
