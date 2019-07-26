package com.myspringmvc.bean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionInterceptor implements  Interceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");
        if(token==null){
            System.out.println("拦截器LOG "+" token 为 NULL");
        }
        return true;
    }
}
