package com.jiangnx.community.mappertest;


import com.jiangnx.community.CommunityApplication;
import com.jiangnx.community.dao.LoginTicketMapper;
import com.jiangnx.community.entity.LoginTicket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class LoginTicketTest {

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void testInsert(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000*60*10));
        loginTicket.setStatus(0);
        loginTicket.setTicket("test01");
        loginTicket.setUserId(150);

        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void testSelectAndUpdate(){
        LoginTicket loginTicket = loginTicketMapper.selectLoginTicketByTicket("test01");
        System.out.println(loginTicket);

        loginTicketMapper.updateLoginTicketStatus("test01", 1);


    }

    @Test
    public  void setTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        Integer time = 60 * 60 * 24 * 100;
       // System.out.println(sdf.format(new Date(System.currentTimeMillis() + time * 1000)));
        System.out.println(System.currentTimeMillis() + time * 1000);
        System.out.println(sdf.format(new Date(System.currentTimeMillis() + time * 1000)));
    }
}
