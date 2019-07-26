package com.myspringmvc.config;

import com.myspringmvc.bean.Conter;
import com.myspringmvc.bean.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HandlerApapter {
    private Map<String, Integer> paramMapping;

    public HandlerApapter(Map<String, Integer> paramMapping) {
        this.paramMapping = paramMapping;
    }

    /**
     * 主要目的是用反射调用url对应的method
     *
     * @param request
     * @param response
     * @param handler
     */
    public Object handle(HttpServletRequest request, HttpServletResponse response, Handler handler) throws InvocationTargetException, IllegalAccessException {

        //为什么要传req、为什么要穿resp、为什么传handler
        //为了给request、response赋值
        Class<?>[] parameterTypes = handler.method.getParameterTypes();

        //要想给参数赋值，只能通过索引号来找到具体的某个参数
        Object[] paramValues = new Object[parameterTypes.length];

        Map<String, String[]> params = request.getParameterMap();

        for (Map.Entry<String, String[]> param : params.entrySet()) {
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "")
                    .replaceAll(",\\s", ",");

            if (!this.paramMapping.containsKey(param.getKey())) {
                continue;
            }

            Integer index = this.paramMapping.get(param.getKey());

            //单个赋值是不行的
            paramValues[index] = caseStringValue(value, parameterTypes[index]);
        }

        //request 和 response 要赋值
        String requestName = HttpServletRequest.class.getName();
        if (this.paramMapping.containsKey(requestName)) {
            Integer requestIndex = this.paramMapping.get(requestName);
            paramValues[requestIndex] = request;
        }
        String responseName = HttpServletResponse.class.getName();
        if (this.paramMapping.containsKey(responseName)) {
            Integer responseIndex = this.paramMapping.get(responseName);
            paramValues[responseIndex] = response;
        }
        List<Interceptor> allInterceptors = Conter.getInterceps();
        for (int i = 0; i < allInterceptors.size(); i++) {
            Interceptor interceptor = allInterceptors.get(i);
            interceptor.preHandle(request, response, handler);
        }
        Object r = handler.method.invoke(handler.controller, paramValues);
        return r;
    }

    /**
     * 转换参数类型
     *
     * @param value
     * @param clazz
     * @return
     */
    private Object caseStringValue(String value, Class<?> clazz) {
        if (clazz == String.class) {
            return value;
        } else if (clazz == Integer.class) {
            return Integer.valueOf(value);
        } else if (clazz == int.class) {
            return Integer.valueOf(value);
        } else {
            return null;
        }
    }


}
