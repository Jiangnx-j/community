package com.jiangnx.community.controller.advice;

import com.jiangnx.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//对有controller注解的类进行处理
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    //参数表示这个方法处理那种类型的异常
    @ExceptionHandler(Exception.class)
    public void handleException(HttpServletRequest request, HttpServletResponse response,Exception e) throws IOException {
        logger.error("服务器异常"+e.getMessage());
        for (StackTraceElement element:e.getStackTrace()){
            logger.error(element.toString());
        }
        //判断是普通请求还是AJAX请求
        String header = request.getHeader("x-requested-with");
        if ("XMLHttpRequest".equals(header)){
            //如果是AJAX请求，就给浏览器返回一个字符串
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.print(CommunityUtil.getJSONString(1,"服务器异常", null));
        }else {
            response.sendRedirect(request.getContextPath()+"/error");
        }

    }
}
