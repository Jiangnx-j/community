package com.jiangnx.community.controller;

import com.jiangnx.community.dao.DiscussPostMapper;
import com.jiangnx.community.entity.Comment;
import com.jiangnx.community.entity.DiscussPost;
import com.jiangnx.community.entity.Page;
import com.jiangnx.community.entity.User;
import com.jiangnx.community.service.CommentService;
import com.jiangnx.community.service.DiscussPostService;
import com.jiangnx.community.service.LikeService;
import com.jiangnx.community.service.UserService;
import com.jiangnx.community.util.CommunityConstant;
import com.jiangnx.community.util.CommunityUtil;
import com.jiangnx.community.util.HostHolder;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    //添加帖子
    @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title,String content){
        User user = hostHolder.getUser();

        DiscussPost discussPost = new DiscussPost();
        discussPost.setStatus(0);
        discussPost.setContent(content);
        discussPost.setTitle(title);
        discussPost.setCreateTime(new Date());
        discussPost.setType(0);

        Map<String, Object> map = discussPostService.addDiscussPost(user, discussPost);

        if (map.get("login")!=null){
            return CommunityUtil.getJSONString(1, map.get("msg").toString(),null);
        }else if (map.get("error")!=null){
            return CommunityUtil.getJSONString(1,map.get("msg").toString(), null);
        }else {
            return CommunityUtil.getJSONString(0,map.get("msg").toString(),null);
        }
    }

    //帖子详情
    @GetMapping("/detail/{id}")
    public String discussDetail(@PathVariable("id") Integer id, Model model, Page page){
        DiscussPost discussPost = discussPostService.findDiscussPostById(id);
        model.addAttribute("post",discussPost);
        //作者信息
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute("user",user);
        //登录用户信息

        User loginUser = hostHolder.getUser();
        //查询帖子点赞情况
        //当前登录用户是否已经点赞
        Integer status = 0;
        //用户登录了，才判断他是否点赞了
        if (loginUser!=null){
            status = likeService.getLikeStatusByUserid(1, discussPost.getId(), loginUser.getId());
        }
        //帖子点赞总数
        Long count = likeService.getLikeCount(1, discussPost.getId());
        model.addAttribute("status",status);
        model.addAttribute("count",count);
        //设置分页的属性
        page.setLimit(5);
        page.setPath("/discuss/detail/"+discussPost.getId());
        page.setTotal(discussPost.getCommentCount()==null?0:discussPost.getCommentCount());

        //从数据库查询的数据，光是评论本身的信息还不够，每一个评论还有与他相关的其他信息
        List<Comment> commentList = commentService.findCommentByEntity(ENTITY_TYPE_POST, discussPost.getId(), page.getOffset(), page.getLimit());
        //用于存放给前端返回的数据，一评论为单位，因为每个评论还有与他相关的其他信息，所以使用map来存放一条评论的相关信息
        List<Map<String,Object>> commentVoList = new ArrayList<>();
        //如果有评论，就对他进行遍历，获取每个品论的相关信息
        int commentstatus = 0;
        int likeStatusByUserid = 0;
        if (commentList != null){
            for (Comment comment : commentList){
                Map<String,Object> commentVo = new HashMap<>();
                //评论本身的信息
                commentVo.put("comment", comment);
                //评论作者的信息
                commentVo.put("user", userService.findUserById(comment.getUserId()));
                //查询评论下的所有回复
                List<Comment> replyList = commentService.findCommentByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                //点赞情况
                Long count1 = likeService.getLikeCount(2, comment.getId());
                if (loginUser!=null){
                    commentstatus = likeService.getLikeStatusByUserid(2, comment.getId(), loginUser.getId());
                }
                commentVo.put("likeStatus", commentstatus);
                commentVo.put("likeCount", count1);
                //回复除了回复本身,还有作者等其他信息
                List<Map<String,Object>> replyVoList = new ArrayList<>();
                if (replyList!=null){
                    for (Comment reply:replyList){
                        Map<String,Object> replyVo = new HashMap<>();
                        replyVo.put("reply",reply);
                        //作者
                        replyVo.put("user",userService.findUserById(reply.getUserId()));
                        //回复的对象
                        if (reply.getTargetId() != 0){
                            replyVo.put("targetUser", userService.findUserById(reply.getTargetId()));
                        }else {
                            replyVo.put("targetUser",null);
                        }
                        Long count2 = likeService.getLikeCount(2, reply.getId());
                       if (loginUser!=null){
                           likeStatusByUserid = likeService.getLikeStatusByUserid(2, reply.getId(), loginUser.getId());
                       }
                        replyVo.put("likeStatus",likeStatusByUserid);
                        replyVo.put("likeCount", count2);
                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replyCount",replyVoList.size());
                commentVo.put("replyVoList", replyVoList);
                commentVoList.add(commentVo);
            }
        }

        model.addAttribute("comments",commentVoList);

        return "/site/discuss-detail";
    }

}
