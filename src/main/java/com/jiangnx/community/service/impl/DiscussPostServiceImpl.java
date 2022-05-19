package com.jiangnx.community.service.impl;

import com.jiangnx.community.dao.DiscussPostMapper;
import com.jiangnx.community.entity.DiscussPost;
import com.jiangnx.community.entity.User;
import com.jiangnx.community.service.DiscussPostService;
import com.jiangnx.community.util.SensitiveFilterUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.tags.HtmlEscapeTag;
import org.springframework.web.util.HtmlUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiscussPostServiceImpl implements DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilterUtil sensitiveFilterUtil;

    @Override
    public List<DiscussPost> findDiscussPosts(Integer userId, Integer offset, Integer limit) {
        return discussPostMapper.selectDiscussPosts(userId,offset,limit);
    }

    @Override
    public Integer findDiscussPostRows(Integer userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    @Override
    public Map<String,Object> addDiscussPost(User user, DiscussPost discussPost) {
        Map<String,Object> map = new HashMap<>();

        if (user == null){
            map.put("msg", "请先登录后再发布帖子");
            //告诉控制器，用户未登录
            map.put("login", "login");
            return map;
        }

        if (discussPost==null){
            throw new IllegalArgumentException("参数不能为空");
        }


        //排除html标签的干扰
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));

        //过滤敏感词
        discussPost.setTitle(sensitiveFilterUtil.doFilter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilterUtil.doFilter(discussPost.getContent()));
        discussPost.setUserId(user.getId());
        int result = discussPostMapper.insertDiscussPost(discussPost);
        if (result == 1){
            map.put("msg", "添加成功");
            return map;
        }else {
            map.put("msg", "添加失败");
            map.put("error", "error");
            return map;
        }
    }

    @Override
    public DiscussPost findDiscussPostById(Integer id) {
        DiscussPost discussPost = discussPostMapper.selectDiscussPostById(id);
        if (discussPost.getStatus() == 2){
            discussPost.setTitle("xxx");
            discussPost.setContent("当前帖子暂时无法查看");
        }
        return discussPost;
    }
}
