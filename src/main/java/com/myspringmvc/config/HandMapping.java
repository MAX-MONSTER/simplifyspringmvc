package com.myspringmvc.config;

import com.myspringmvc.config.Handler;

import javax.servlet.http.HttpServletRequest;

public interface HandMapping {
Handler getHandler(HttpServletRequest request);
}
