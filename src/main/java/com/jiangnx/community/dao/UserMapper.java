package com.jiangnx.community.dao;

import com.jiangnx.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserMapper {
    User selectUserById(Integer id);

    User selectUserByName(String name);

    User selectUserByEmail(String email);

    int insertUser(User user);

    int updateStatus(@Param("status") Integer status,@Param("id") Integer id);

    int updateHeader(@Param("id") Integer id,@Param("url") String url);

    int updatePassword(@Param("password") String password, @Param("id") Integer id);
}
