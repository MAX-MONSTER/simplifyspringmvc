package com.myspringmvc.bean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Interceptor {
    public boolean  preHandle(HttpServletRequest request, HttpServletResponse response,Object handler
    );
}
