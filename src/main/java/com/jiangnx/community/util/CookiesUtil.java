package com.jiangnx.community.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


public class CookiesUtil {

    /**
     *
     * @param request 请求
     * @param name  cookie name
     * @return
     *  从request的cookie数组中获取指定名称的cookie，没有则返回null
     */

    public static Cookie getCookie(HttpServletRequest request,String name){
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(name)){
                cookie = cookies[i];
            }
        }
        return cookie;
    }
}
