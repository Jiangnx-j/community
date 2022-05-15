package com.jiangnx.community.controller;

import com.jiangnx.community.entity.User;
import com.jiangnx.community.service.UserService;
import com.jiangnx.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {

    @Autowired
    private UserService userService;

    // 前端页面点击注册，负责跳转到注册页面
    @GetMapping("/register")
    public String getRegisterPage(){
        return "site/register";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "site/login";
    }

    @PostMapping("/register")
    public String register(Model model, User user){
        Map<String, Object> map = userService.register(user);
        //map中有信息说明注册失败
        if (!map.isEmpty()){
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "site/register";
        }else {
            model.addAttribute("msg","注册成功！我们向您的邮箱发送了一封邮件，请尽快激活。");
            model.addAttribute("targetPage", "/community/index");
            return "site/operate-result";
        }
    }

    @GetMapping("/activation/{userid}/{code}")
    public String activation(Model model, @PathVariable("userid")Integer userid,@PathVariable("code")String code){
       int result = userService.activation(userid,code);

        if (result == ACTIVATION_SUCCESS){
            model.addAttribute("msg", "激活成功");
            model.addAttribute("targetPage", "/community/login");
        }else if (result == ACTIVATION_REPEAT){
            model.addAttribute("msg", "您的账号已经激活，请勿重复操作");
            model.addAttribute("targetPage", "/community/login");
        }else{
            model.addAttribute("msg", "激活失败");
            model.addAttribute("targetPage", "/community/index");
        }
        return "site/operate-result";
    }
}
