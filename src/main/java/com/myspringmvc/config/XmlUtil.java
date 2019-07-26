package com.myspringmvc.config;

import com.myspringmvc.bean.AbstractBeanFactory;
import com.myspringmvc.bean.Conter;
import com.myspringmvc.bean.Interceptor;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.Iterator;

public class XmlUtil {
    public void readXml(String path) {
        String LOCAL_LIST_PATH = this.getClass().getClassLoader().getResource(path).getPath();
        //1.读取
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(LOCAL_LIST_PATH);
            Element rootElement = document.getRootElement();
            Iterator it = rootElement.elementIterator();
            Element ele = null;
            while (it.hasNext()) {
                ele = (Element) it.next();
                Attribute value = ele.attribute("value");
                if (null != value && value.getValue()!=null) {
                    new AbstractBeanFactory(value.getValue());
                    continue;
                }
                String aClass = ele.attribute("class").getValue();
                Class<?> c = null;
                try {
                    c = Class.forName(aClass);

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                try {

                    if (ele.getQName().getName().equals("Interceptors")) {
                        Interceptor o = null;
                        o = (Interceptor) c.newInstance();
                        Conter.getInterceps().add(o);
                    }
                    if (ele.getQName().getName().equals("HandMappings")) {
                        HandMapping o = null;
                        o = (HandMapping) c.newInstance();
                        Conter.getHandMppings().add(o);
                    }
                    if (ele.getQName().getName().equals("HanderAdaptersMapping")) {
                        HanderAdaptersMapping o = null;
                        o = (HanderAdaptersMapping) c.newInstance();
                        Conter.getHandlerApapters().add(o);
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


}
