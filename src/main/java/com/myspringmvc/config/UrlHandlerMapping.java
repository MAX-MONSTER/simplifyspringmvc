package com.myspringmvc.config;


import com.myspringmvc.annotation.MyController;
import com.myspringmvc.annotation.MyRequestMapping;
import com.myspringmvc.bean.Conter;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlHandlerMapping implements HandMapping {



    public UrlHandlerMapping() {
        initMappings();
    }

    public void initMappings() {
        Map<String, Object> ioc = Conter.getBeansMap();
        if (ioc.isEmpty()) {
            return;
        }

        //只要是由Cotroller修饰
        //
        // 类，里面方法全部找出来
        //而且这个方法上应该要加了RequestMaping注解，如果没加这个注解，这个方法是不能被外界来访问的
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {

            Class<?> clazz = entry.getValue().getClass();

            if (!clazz.isAnnotationPresent(MyController.class)) {
                continue;
            }

            String url = "";

            if (clazz.isAnnotationPresent(MyRequestMapping.class)) {
                MyRequestMapping requestMapping = clazz.getAnnotation(MyRequestMapping.class);
                url = requestMapping.value();
            }

            //扫描Controller下面的所有方法
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(MyRequestMapping.class)) {
                    continue;
                }
                MyRequestMapping requestMapping = method.getAnnotation(MyRequestMapping.class);
                String regex = (url + requestMapping.value()).replaceAll("/+", "/");
                Pattern pattern = Pattern.compile(regex);
                Conter.getHanderList().add(new Handler(entry.getValue(), method, pattern));
                System.out.println("Mapping: " + regex + " " + method.toString());
            }

        }

        //RequestMapping 会配置一个url，那么一个url就对应一个方法，并将这个关系保存到Map中
    }

    @Override
    public Handler getHandler(HttpServletRequest request) {
        //循环handlerMapping
        if ( Conter.getHanderList().isEmpty()) {
            return null;
        }

        String url = request.getRequestURI();
        String contextPath = request.getContextPath();

        url = url.replace(contextPath, "").replaceAll("/+", "/");


        for (Handler handler :  Conter.getHanderList()) {

            Matcher matcher = handler.pattern.matcher(url);

            if (!matcher.matches()) {
                continue;
            }

            return handler;
        }

        return null;
    }
}
