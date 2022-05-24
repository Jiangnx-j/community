package com.jiangnx.community.controller;

import com.jiangnx.community.annotation.LoginRequried;
import com.jiangnx.community.entity.Page;
import com.jiangnx.community.entity.User;
import com.jiangnx.community.service.LikeService;
import com.jiangnx.community.service.UserService;
import com.jiangnx.community.util.CommunityUtil;
import com.jiangnx.community.util.HostHolder;
import com.jiangnx.community.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    //点赞功能
    @LoginRequried
    @PostMapping("/like")
    @ResponseBody
    public String like(Integer entityType, Integer entityId, Integer authorId) {
        User user = hostHolder.getUser();
        likeService.like(entityType, entityId, user.getId(), authorId);
        Long likeCount = likeService.getLikeCount(entityType, entityId);
        Integer status = likeService.getLikeStatusByUserid(entityType, entityId, user.getId());

        Map<String, Object> map = new HashMap<>();
        map.put("counts", likeCount);
        map.put("status", status);
        String jsonString = CommunityUtil.getJSONString(0, null, map);
        return jsonString;
    }

    //关注功能
    @LoginRequried
    @PostMapping("/follower")
    @ResponseBody
    public String follower(Integer userId, Integer followerId) {
        boolean follower = likeService.follower(userId, followerId);
        if (follower) {
            //告诉前端目前是关注状态
            return CommunityUtil.getJSONString(0, "follower", null);
        } else {
            return CommunityUtil.getJSONString(0, "noFollower", null);
        }
    }

    //关注列表
    @GetMapping("/followeelist/{userId}")
    public String getFollowersList(@PathVariable("userId") Integer userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        String userFolloweesKey = RedisUtil.getUserFollowerKey(userId);
        page.setTotal(redisTemplate.opsForZSet().zCard(userFolloweesKey).intValue());
        page.setPath("/followeelist/" + userId);
        page.setLimit(10);

        List<Map<String, Object>> list = likeService.getUserFollowees(userId, page.getOffset(), page.getLimit());
        model.addAttribute("list",list);
        model.addAttribute("user",user);

        return "/site/followee";
    }

    @GetMapping("/followerlist/{userId}")
    public String getFolloweesList(@PathVariable("userId") Integer userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user==null){
            throw new RuntimeException("用户不存在");
        }

        page.setLimit(10);
        page.setTotal((int) likeService.findFollowerCount(userId));
        page.setPath("/followerlist/"+userId);

        List<Map<String, Object>> userFollowers = likeService.getUserFollowers(userId, page.getOffset(), page.getLimit());

        model.addAttribute("list",userFollowers);
        model.addAttribute("user",user);
        return "/site/follower";
    }

}
