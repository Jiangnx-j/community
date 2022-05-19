package com.jiangnx.community.service;

import com.jiangnx.community.entity.LoginTicket;
import com.jiangnx.community.entity.User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UserService {

    User findUserById(Integer userId);

    Map<String,Object> register(User user);

    Integer activation(Integer userid, String code);

    Map<String,Object> login(String username,String password,long expiredSeconds);

    void logout(String ticket);

    LoginTicket findLoginTicketByTicket(String ticket);

    Integer updateHeader(Integer userId,String headerUrl);

    Map<String,Object> updateUserPassword(String oldPassword,String newPassword);
}
