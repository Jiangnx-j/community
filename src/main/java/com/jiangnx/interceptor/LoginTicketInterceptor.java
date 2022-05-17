package com.jiangnx.interceptor;

import com.jiangnx.community.entity.LoginTicket;
import com.jiangnx.community.entity.User;
import com.jiangnx.community.service.UserService;
import com.jiangnx.community.util.CookiesUtil;
import com.jiangnx.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie cookie = CookiesUtil.getCookie(request, "ticket");

        if (cookie != null){
            String ticket = cookie.getValue();
            LoginTicket loginTicket = userService.findLoginTicketByTicket(ticket);
            //判断ticket是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 &&loginTicket.getExpired().after(new Date())){
                User user = userService.findUserById(loginTicket.getUserId());
                hostHolder.setUser(user);
            }

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (request!=null && modelAndView !=null){
            User user = hostHolder.getUser();
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
