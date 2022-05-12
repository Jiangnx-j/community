package com.jiangnx.community.service;

import com.jiangnx.community.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User findUserById(Integer userId);

}
