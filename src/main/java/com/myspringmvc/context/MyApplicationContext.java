package com.myspringmvc.context;


import com.myspringmvc.annotation.MyAutowire;
import com.myspringmvc.annotation.MyController;
import com.myspringmvc.annotation.MyService;
import com.myspringmvc.testMvc.TestMyselfController;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class MyApplicationContext {


    private Map<String, Object> instanceMap = new ConcurrentHashMap<String, Object>();
    private Properties config = new Properties();
    private List<String> classs = new ArrayList<String>();

    public MyApplicationContext(String configDir) {
        InputStream in = null;
        in = this.getClass().getClassLoader().getResourceAsStream(configDir);
        try {
            config.load(in);
            scanPackage(config.getProperty("scanPackage"));
            createBenas();
            pourInto();
            TestMyselfController testMyselfController = (TestMyselfController)instanceMap.get("testMyselfController");
            testMyselfController.testService.sys();
            System.out.println(instanceMap.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scanPackage(String scanPackage) {
        String s = scanPackage.replaceAll("\\.", "/");
        URL resource = this.getClass().getClassLoader().getResource(s);
        if (resource == null || resource.getFile() == null) {
//            throw new NullPointerException("the resource or its file is null!");
            return;
        }
        File dir = new File(resource.getFile());
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            //如果是一个文件夹，继续递归
            if (file.isDirectory()) {
                scanPackage(scanPackage + "." + file.getName());
            } else {
                classs.add(scanPackage + "." + file.getName().replace(".class", "").trim());
            }
        }
    }

    public static void main(String[] args) {
        MyApplicationContext context = new MyApplicationContext("config.properties");
    }

    public Map<String,Object> getAllBean(){
        return  this.instanceMap;
    }

    public void createBenas() {
        if (classs.size() == 0) {
            return;
        }
        try {
            for (String className : classs) {
                Class<?> aClass = null;

                aClass = Class.forName(className);
                if (aClass.isAnnotationPresent(MyController.class)) {
                    String beanId = lowerName(aClass.getSimpleName());
                    instanceMap.put(beanId, aClass.newInstance());
                }
                if (aClass.isAnnotationPresent(MyService.class)) {
                    MyService service = aClass.getAnnotation(MyService.class);

                    //如果设置了自定义名字，就优先使用自定义名字
                    String id = service.value();
                    if (!"".equals(id.trim())) {
                        instanceMap.put(id, aClass.newInstance());
                        continue;
                    }

                    //如果是空的，就用默认规则类名首字母小写，如果是接口可以根据类型匹配
                    Class<?>[] interfaces = aClass.getInterfaces();
                    //如果这个类实现了接口，就用接口的类型作为id
                    for (Class<?> anInterface : interfaces) {
                        instanceMap.put(lowerName(anInterface.getSimpleName()), aClass.newInstance());
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pourInto() {
        if (instanceMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : instanceMap.entrySet()) {
            //把所有的属性全部取出来，包括私有属性
            Field[] fields = entry.getValue().getClass().getDeclaredFields();

            for (Field field : fields) {
                if (!field.isAnnotationPresent(MyAutowire.class)) {
                    continue;
                }
                MyAutowire autowired = field.getAnnotation(MyAutowire.class);

                String id = autowired.value().trim();
                //如果id为空，也就是说，自己没有设置，默认根据类型来注入
                if ("".equals(id)) {
                    id = field.getType().getSimpleName();
                }
                //开发私有变量属性
                field.setAccessible(true);

                try {
                    field.set(entry.getValue(), instanceMap.get(lowerName(id)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String lowerName(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }
}
