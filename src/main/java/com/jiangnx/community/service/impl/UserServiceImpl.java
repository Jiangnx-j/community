package com.jiangnx.community.service.impl;

import com.jiangnx.community.dao.UserMapper;
import com.jiangnx.community.entity.User;
import com.jiangnx.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserById(Integer  userId) {
        return userMapper.selectUserById(userId);
    }
}
