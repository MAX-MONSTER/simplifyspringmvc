package com.myspringmvc.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitConfig {
    public static final String INTERCEPTORS="INTERCEPTORS";
    private static Map<String,List<Object>> configMap = new HashMap<>();

    private static List<HandMapping> handMappingList = new ArrayList<>();
    private static InitConfig instance = null;

    public void init(String path) {
        new XmlUtil().readXml(path);
    }


    private InitConfig() {

    }

    public static synchronized InitConfig getInstance() {
        if (null == instance) {
            instance = new InitConfig();
        }
        return instance;
    }
}
