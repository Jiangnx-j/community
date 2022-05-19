package com.jiangnx.community.controller;

import com.jiangnx.community.annotation.LoginRequried;
import com.jiangnx.community.entity.User;
import com.jiangnx.community.service.UserService;
import com.jiangnx.community.util.CommunityUtil;
import com.jiangnx.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private HostHolder hostHolder;

    @Value("${community.domain}")
    private String domain;

    @Value("${community.path.upload}")
    private String uploadPath;

    @LoginRequried
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getUserSetting(){
        System.out.println(1);
        return "/site/setting";
    }

    @LoginRequried
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage, Model model){
        //判空
        if (headerImage == null){
            model.addAttribute("error", "您还没有选择图片");
            return "/site/setting";
        }

        //判断格式
        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".")); //获得文件后缀名
        if (StringUtils.isBlank(suffix) || (!".png".equals(suffix)&!".jpg".equals(suffix)&!".jpeg".equals(suffix))){
            model.addAttribute("error","文件格式不正确");
            return "/site/setting";
        }

        fileName = CommunityUtil.generateUUID() + suffix;
        File file  = new File(uploadPath+"/"+fileName);
        try {
            headerImage.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        User user = hostHolder.getUser();
        userService.updateHeader(user.getId(),domain+contextPath+"/user/header/"+fileName);

        return "redirect:/index";
    }


    @GetMapping("/header/{fileName}")
    public void getHeaderImage(@PathVariable("fileName")String fileName, HttpServletResponse response){
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        suffix.replace(".", "");
        File file = new File(uploadPath+"/"+fileName);
        response.setContentType("image/"+suffix);
        try(ServletOutputStream os = response.getOutputStream();
            FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = new byte[1024];
            int b = 0;
            while ((b = fis.read(bytes)) != -1){
                os.write(bytes,0,b);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @LoginRequried
    @PostMapping("/updatePassword")
    public String updatePassword(@CookieValue("ticket")String ticket, String oldPassword,String newPassword,Model model){
        Map<String, Object> map = userService.updateUserPassword(oldPassword, newPassword);
        if (map.size()==0){
            userService.logout(ticket);
            return "redirect:/login";
        }else {
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("newPasswordMsg",map.get("newPasswordMsg"));
            return "/site/setting";
        }
    }
}
