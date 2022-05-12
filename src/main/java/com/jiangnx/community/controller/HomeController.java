package com.jiangnx.community.controller;

import com.jiangnx.community.entity.DiscussPost;
import com.jiangnx.community.entity.Page;
import com.jiangnx.community.entity.User;
import com.jiangnx.community.service.DiscussPostService;
import com.jiangnx.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public String getIndexPage(Model model, Page page){
        page.setTotal(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String,Object>> discussPost = new ArrayList<>();
        if(list != null){
            for(DiscussPost discussPost1:list){
                Map<String,Object> map = new HashMap<>();
                User user = userService.findUserById(discussPost1.getUserId());
                map.put("post", discussPost1);
                map.put("user", user);
                discussPost.add(map);
            }
        }
        model.addAttribute("discussPost", discussPost);
        return "index.html";
    }
}
