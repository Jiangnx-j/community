package com.jiangnx.community.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Aspect
public class ServiceLogAspect {
    private static final Logger loger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("execution(* com.jiangnx.community.service.*.*(..))")
    public void pointCut(){}

    @Before("pointCut()")
    public void sercieAccessLog(JoinPoint joinPoint){
        //记录用户xxx（ip）在xxx时间访问了xxx的xxx方法
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = null;
        if (request!=null){
           ip = request.getRemoteHost();
        }
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        String name = joinPoint.getSignature().getName();
        String log = String.format("时间：[%s],用户：[%s],访问了[%s]", time,ip,declaringTypeName+name);
        loger.info(log);
    }
}
