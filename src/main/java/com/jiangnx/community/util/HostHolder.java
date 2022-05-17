package com.jiangnx.community.util;

import com.jiangnx.community.entity.User;
import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
