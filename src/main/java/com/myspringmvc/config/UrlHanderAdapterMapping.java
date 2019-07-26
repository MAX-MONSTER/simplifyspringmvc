package com.myspringmvc.config;

import com.myspringmvc.annotation.MyRequestPara;
import com.myspringmvc.bean.Conter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlHanderAdapterMapping implements HanderAdaptersMapping {
    private Map<Handler, HandlerApapter> adapaterMapping = new HashMap<Handler, HandlerApapter>();

    @Override
    public HandlerApapter getHandlerAdapter(Handler hander) {
        if (adapaterMapping.isEmpty()) {
            return null;
        }

        return adapaterMapping.get(hander);
    }

    public UrlHanderAdapterMapping() {
        initHandAdaptorMappings();
    }

    void initHandAdaptorMappings() {
        List<Handler> handerList = Conter.getHanderList();
        if (handerList.isEmpty()) {

        }

        //参数类型作为key，参数的索引号作为值
        Map<String, Integer> paramMapping = null;

//        for (Map.Entry<Pattern, Handler> entry : handlerMapping.entrySet()) {
        for (Handler handler : handerList) {
            paramMapping = new HashMap<String, Integer>();

            //把这个方法上面所有的参数全部获取到
            Class<?>[] parameterTypes = handler.method.getParameterTypes();

            //有顺序，但是通过反射，没法拿到我们参数名字
            //因为每个参数上面是可以加多个数组的，所以是二维数组,第一位表示参数位置，第二位表示注解个数
            Annotation[][] pa = handler.method.getParameterAnnotations();
            //匹配自定参数列表
            for (int i = 0; i < pa.length; i++) {
                Class<?> type = parameterTypes[i];

                if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                    paramMapping.put(type.getName(), i);
                    continue;
                }

                for (Annotation annotation : pa[i]) {
                    if (annotation instanceof MyRequestPara) {
                        String paramName = ((MyRequestPara) annotation).value();
                        if (!"".equals(paramName.trim())) {
                            paramMapping.put(paramName, i);
                        }
                    }
                }
            }

            adapaterMapping.put(handler, new HandlerApapter(paramMapping));

        }
    }
}
