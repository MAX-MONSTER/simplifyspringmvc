package com.myspringmvc.serverlet;

import com.alibaba.fastjson.JSON;
import com.myspringmvc.bean.Conter;
import com.myspringmvc.config.Handler;
import com.myspringmvc.config.InitConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


public class MyDispatcherServlet extends HttpServlet {
    private static final String CONFIG_LOCATION = "application";

    @Override
    public void init(ServletConfig config) throws ServletException {
        InitConfig.getInstance().init(config.getInitParameter(CONFIG_LOCATION));
    }

    /**
     * 解析url和Method的关联关系
     *
     */

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    //在这里调用自己写的Controller的方法
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("调用");
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("500 Exception, Msg :" + Arrays.toString(e.getStackTrace()));
        }
    }


    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {

        //先取出来一个 Handler， 从 HandlerMapping 中取
        Handler handler = Conter.getHandMppings().get(0).getHandler(request);
        if (handler == null) {
            response.getWriter().write("404 Not Found");
        }

        //再取出来一个适配器
        com.myspringmvc.config.HandlerApapter ha = Conter.getHandlerApapters().get(0).getHandlerAdapter(handler);


        if (ha != null) {
            //再由适配器调用具体方法
            Object mv = ha.handle(request, response, handler);
            response.getWriter().write(JSON.toJSONString(mv));
        }


    }


}
