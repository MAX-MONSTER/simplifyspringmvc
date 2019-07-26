package com.myspringmvc.bean;

import com.myspringmvc.config.HandMapping;
import com.myspringmvc.config.HanderAdaptersMapping;
import com.myspringmvc.config.Handler;
import com.myspringmvc.config.HandlerApapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Conter {
    private static List<Handler> handerList = new ArrayList<>();
    private static List<HandMapping> handMppings = new ArrayList<>();
    private static List<HanderAdaptersMapping> handlerApapters = new ArrayList<>();
    private static Map<String, Object> beansMap = new HashMap<>();
    private static List<Interceptor> interceps = new ArrayList<>();
    public static Map<String, Object> getBeansMap() {
        return beansMap;
    }
    public static List<Handler>  getHanderList() {
        return handerList;
    }
    public static List<HandMapping>  getHandMppings() {
        return handMppings;
    }
    public static List<HanderAdaptersMapping>  getHandlerApapters() {
        return handlerApapters;
    }
    public static List<Interceptor>  getInterceps() {
        return interceps;
    }
}
