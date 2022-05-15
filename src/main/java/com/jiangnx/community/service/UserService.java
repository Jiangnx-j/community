package com.jiangnx.community.service;

import com.jiangnx.community.entity.User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UserService {

    User findUserById(Integer userId);

    Map<String,Object> register(User user);

    Integer activation(Integer userid, String code);

}
