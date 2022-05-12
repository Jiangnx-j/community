package com.jiangnx.community.mappertest;

import com.jiangnx.community.CommunityApplication;
import com.jiangnx.community.dao.UserMapper;
import com.jiangnx.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.spring5.context.SpringContextUtils;

import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectUserByid(){
        User user = userMapper.selectUserById(101);
        System.out.println(user);
    }

    @Test
    public void testInsertUser(){

        User user = new User();
        user.setSalt("12345");
        user.setActivationCode("1");
        user.setCreateTime(new Date());
        user.setEmail("2453421345@qq.com");
        user.setHeaderUrl("https://www.keaidian.com/uploads/allimg/190424/24110307_8.jpg");
        user.setPassword("12345");
        user.setStatus(1);
        user.setType(1);
        user.setUsername("zs");

        int result = userMapper.insertUser(user);
        System.out.println(result);
    }

    @Test
    public void tesSelectUserByName(){
        User user = userMapper.selectUserByName("zs");
        System.out.println(user);
        System.out.println(userMapper.selectUserByEmail("2453421345@qq.com"));
    }

    @Test
    public void testUpadteUser(){

        System.out.println(userMapper.selectUserById(150).getPassword());
        int result = userMapper.updatePassword("2222", 150);
        System.out.println(userMapper.selectUserById(150).getPassword());
    }

}
