package com.jiangnx.community.dao;

import com.jiangnx.community.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginTicketMapper {

    Integer insertLoginTicket(LoginTicket loginTicket);

    LoginTicket selectLoginTicketByTicket(String ticket);

    Integer updateLoginTicketStatus(@Param("ticket") String ticket, @Param("status") Integer status);

}
