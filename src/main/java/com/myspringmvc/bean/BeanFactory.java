package com.myspringmvc.bean;

import java.util.Map;

public interface BeanFactory {
    Object getBean(String name);

    Object getBean(Class c);

    void scanPackage(String packageName);

    void registBean();

    void pourIntoBean();

     Map<String, Object> getAllBeans();
}
