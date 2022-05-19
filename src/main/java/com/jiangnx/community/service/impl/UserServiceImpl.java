package com.jiangnx.community.service.impl;

import com.jiangnx.community.dao.LoginTicketMapper;
import com.jiangnx.community.dao.UserMapper;
import com.jiangnx.community.entity.LoginTicket;
import com.jiangnx.community.entity.User;
import com.jiangnx.community.service.UserService;
import com.jiangnx.community.util.CommunityConstant;
import com.jiangnx.community.util.CommunityUtil;
import com.jiangnx.community.util.HostHolder;
import com.jiangnx.community.util.MyMailSender;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService, CommunityConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private HostHolder hostHolder;

    //用于发送邮件
    @Autowired
    private MyMailSender mailSender;

    //用于发送邮件时传递参数给邮件html页面
    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    @Override
    public User findUserById(Integer userId) {
        return userMapper.selectUserById(userId);
    }

    @Override
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        //判断用户信息是否为空
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }

        //判断用户名和email是否已经被注册
        User u = userMapper.selectUserByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "用户已存在");
            return map;
        }

        u = userMapper.selectUserByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "邮箱已被注册");
            return map;
        }
        //开始注册用户,对密码加密，前端传过来的信息只有用户名、密码、邮箱、设置用户状态等信息
        // 0普通用户 1超级管理员 2版主
        user.setType(0);
        // 0未激活 1 激活
        user.setStatus(0);
        user.setCreateTime(new Date());
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl("static/img/4.jpg");
        //user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        //对密码进行加密处理
        String passwordString = user.getPassword() + user.getSalt();
        user.setPassword(CommunityUtil.md5(passwordString));
        userMapper.insertUser(user);

        //发送短信验证码
        Context context = new Context();
        context.setVariable("username", user.getUsername());
        String path = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("path", path);

        //将html模板变为String类型，并且加上context中的参数
        String content = templateEngine.process("/mail/activation", context);
        //发送邮件
        mailSender.sendMail(user.getEmail(), "激活邮件", content);


        return map;
    }

    /**
     * @param userid 用户id
     * @param code   激活码
     * @return 返回激活状态 ACTIVATION_SUCCESS ACTIVATION_REPEAT ACTIVATION_FALSE
     */

    @Override
    public Integer activation(Integer userid, String code) {
        User user = userMapper.selectUserById(userid);
        if (user == null || "".equals(user.getActivationCode()) || code == null) {
            return ACTIVATION_FALSE;
        } else if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (code.equals(user.getActivationCode())) {
            userMapper.updateStatus(1, userid);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FALSE;
        }
    }

    @Override
    public Map<String,Object> login(String username,String password,long expiredSeconds){
        Map<String, Object> map = new HashMap<>();

        //判空
        if (StringUtils.isBlank(username)){
            map.put("usernameMsg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        //判断用户是否存在，是否激活
        User user = userMapper.selectUserByName(username);
        if (user == null){
            map.put("usernameMsg", "该账户不存在");
            return map;
        }
        if (user.getStatus() == 0){
            map.put("usernameMsg", "该账户未激活，请检查邮箱查看邮件激活账户");
            return map;
        }

        //验证密码
        password = CommunityUtil.md5(password+user.getSalt());
        if (!password.equals(user.getPassword())){
            map.put("passwordMsg", "密码错误");
            return map;
        }

        //生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
            //设置凭证是否有效 0-有效 1-无效
        loginTicket.setStatus(0);
            //设置过期时间，当前时间+有效时间
        loginTicket.setExpired(new Date(System.currentTimeMillis()+expiredSeconds*1000));
        loginTicketMapper.insertLoginTicket(loginTicket);

        //将ticket的信息返回给controller，controller将ticket的信息保存在Cookie中，在用户下次访问时不必登录
        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    @Override
    public void logout(String ticket) {
        loginTicketMapper.updateLoginTicketStatus(ticket,1);
    }

    @Override
    public LoginTicket findLoginTicketByTicket(String ticket) {
       return loginTicketMapper.selectLoginTicketByTicket(ticket);
    }

    @Override
    public Integer updateHeader(Integer userId, String headerUrl) {
        return userMapper.updateHeader(userId,headerUrl);
    }

    @Override
    public Map<String,Object> updateUserPassword(String oldPassword,String newPasswrod) {
        Map<String,Object> map = new HashMap<>();
        //判空
       if (oldPassword == null){
           map.put("passwordMsg","请输入原密码");
           return map;
       }
       if (newPasswrod == null){
           map.put("newPasswordMsg","请输入新密码");
           return map;
       }

       //判断密码是否正确
        User user = hostHolder.getUser();
       if (user!=null){
          oldPassword = CommunityUtil.md5(oldPassword+user.getSalt());
          if (!oldPassword.equals(user.getPassword())){
              map.put("passwordMsg", "密码错误");
              return map;
          }

          //对新密码加密，然后更新
          newPasswrod = CommunityUtil.md5(newPasswrod+user.getSalt());
          userMapper.updatePassword(newPasswrod,user.getId());
       }
        return map;
    }
}
