package com.jiangnx.community.controller;

import com.google.code.kaptcha.Producer;
import com.jiangnx.community.entity.User;
import com.jiangnx.community.service.UserService;
import com.jiangnx.community.util.CommunityConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptcha;

    @Value("${server.servlet.context-path}")
    private String contextPath;

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

    @GetMapping("/verifycode")
    public void verifycode(HttpServletResponse response, HttpSession session){
        String text = kaptcha.createText();
        BufferedImage image = kaptcha.createImage(text);
        //将验证码数据保存在session中，便于验证
        session.setAttribute("verifycode", text);
        response.setContentType("image/png");
        try {
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(image,"png", out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param model
     * @param username
     * @param password
     * @param verifyCode 验证码
     * @param rememberme 用户是否选择“记住我”选项
     * @param response  通过response将Cookie返回给前端
     * @param session   通过session获取验证码
     * @return
     */
    @PostMapping("/login")
    public String login(Model model,String username,String password,String verifyCode,boolean rememberme,
                        HttpServletResponse response, HttpSession session){

        //校验验证码
        if (StringUtils.isBlank(verifyCode) || verifyCode == null || !verifyCode.equalsIgnoreCase(session.getAttribute("verifycode").toString())){
            model.addAttribute("codeMsg", "验证码错误");
            return "site/login";
        }
        //根据rememberme 确定ticket的有效时间
        long expired = DEFAULT_EXPIRED;
        if (rememberme){
            expired = REMEMBER_EXPIRED;
        }

        //尝试登录,根据map的值不同，跳转不同的页面，并向前端返回提示信息
        Map<String, Object> map = userService.login(username, password, expired);
        //map中有ticket说明登录成功，创建Cookie，跳转首页
        if ( map.get("ticket") != null){
            String ticket = map.get("ticket").toString();
            Cookie cookie = new Cookie("ticket",ticket);
            cookie.setMaxAge((int)expired);
            cookie.setPath(contextPath);
            response.addCookie(cookie);
            return "redirect:/index";
        }else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "site/login";
        }

    }

    @GetMapping("/logout")
    public String logout(@CookieValue("ticket")String ticket){
        userService.logout(ticket);
        //重定向默认是get请求
        return "redirect:login";
    }

}
