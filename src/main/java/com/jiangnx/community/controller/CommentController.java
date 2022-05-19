package com.jiangnx.community.controller;

import com.jiangnx.community.annotation.LoginRequried;
import com.jiangnx.community.entity.Comment;
import com.jiangnx.community.service.CommentService;
import com.jiangnx.community.service.DiscussPostService;
import com.jiangnx.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequried
    @PostMapping("/add/{discussId}")
    public String addComment(@PathVariable("discussId") Integer discussId,
                              Comment comment){
        //从前端传来的comment参数
        //userId,content,targetId,entityType,entityId
        if (comment.getTargetId() == null){
            comment.setTargetId(0);
        }
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        Integer count = discussPostService.findDiscussPostById(discussId).getCommentCount();
        if (count == null){
            count = 0;
        }
        comment.setUserId(hostHolder.getUser().getId());
        commentService.addComment(comment,discussId,count);
        return "redirect:/discuss/detail/"+discussId;

    }
}
