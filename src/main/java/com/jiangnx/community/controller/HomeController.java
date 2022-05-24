package com.jiangnx.community.controller;

import com.jiangnx.community.entity.DiscussPost;
import com.jiangnx.community.entity.Page;
import com.jiangnx.community.entity.User;
import com.jiangnx.community.service.DiscussPostService;
import com.jiangnx.community.service.LikeService;
import com.jiangnx.community.service.UserService;
import com.jiangnx.community.util.HostHolder;
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

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    //网站首页
    @GetMapping("/index")
    public String getIndexPage(Model model, Page page){
        page.setTotal(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String,Object>> discussPost = new ArrayList<>();
        User loginUser = hostHolder.getUser();
        Integer status = 0;
        if(list != null){
            for(DiscussPost discussPost1:list){
                Map<String,Object> map = new HashMap<>();
                //查询作者信息
                User user = userService.findUserById(discussPost1.getUserId());
                //查询点赞情况
                if (loginUser!=null){
                    status = likeService.getLikeStatusByUserid(1, discussPost1.getId(), loginUser.getId());
                }
                Long count = likeService.getLikeCount(1, discussPost1.getId());

                map.put("post", discussPost1);
                map.put("user", user);
                map.put("status", status);
                map.put("count", count);
                discussPost.add(map);
            }
        }
        model.addAttribute("discussPost", discussPost);
        return "index.html";
    }

    @GetMapping("/error")
    public String getErrorPage(){
        return "error/500";
    }
}
