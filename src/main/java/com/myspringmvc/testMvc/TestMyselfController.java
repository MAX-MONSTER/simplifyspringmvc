package com.myspringmvc.testMvc;

import com.myspringmvc.annotation.MyAutowire;
import com.myspringmvc.annotation.MyController;
import com.myspringmvc.annotation.MyRequestMapping;
import com.myspringmvc.annotation.MyRequestPara;
import com.myspringmvc.testMvc.service.TestService;

@MyController
@MyRequestMapping("/web")
public class TestMyselfController {
    @MyAutowire
    public TestService testService;

    @MyRequestMapping("/addUser")
    public String addUser(@MyRequestPara(value = "name") String name,@MyRequestPara(value = "uid") String uid) {
        testService.sys();
        return  name+" sys";
    }

}
