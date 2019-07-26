package com.myspringmvc.testMvc.service;

import com.myspringmvc.annotation.MyService;

@MyService
public class TestServiceIpl implements TestService{
    public String sys() {
        System.out.println("service");
        return "kk";
    }
}
