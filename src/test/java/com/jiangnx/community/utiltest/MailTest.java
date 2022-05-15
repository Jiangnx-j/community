package com.jiangnx.community.utiltest;

import com.jiangnx.community.CommunityApplication;
import com.jiangnx.community.util.MyMailSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Random;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {

    @Autowired
    private MyMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testSender(){
        mailSender.sendMail("2609134535@qq.com","Test01--欢迎注册", "welcome");
    }

    @Test
    public void testHtmlMail(){
        //thymeleaf模板中的类，可以向html页面传送数据
        Context context = new Context();
        context.setVariable("username", "许文龙");

        String content = templateEngine.process("/mail/demo01", context);

        mailSender.sendMail("15665560323@163.com","HTMLMail-test01",content);
    }

    @Test
    public void stringFormat(){
        System.out.println(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
    }

}
