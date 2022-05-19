package com.jiangnx.community.service.impl;

import com.jiangnx.community.dao.CommentMapper;
import com.jiangnx.community.dao.DiscussPostMapper;
import com.jiangnx.community.entity.Comment;
import com.jiangnx.community.entity.DiscussPost;
import com.jiangnx.community.service.CommentService;
import com.jiangnx.community.util.SensitiveFilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilterUtil sensitiveFilterUtil;

    @Override
    public List<Comment> findCommentByEntity(Integer entityType, Integer entityId, Integer offset, Integer limit) {
        return commentMapper.selectCommentsByEntity(entityType,entityId,offset,limit);
    }

    @Override
    public Integer selectCommentCountsByEntity(Integer entityType, Integer entityId) {
        return commentMapper.selectCommentCount(entityType,entityId);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Integer addComment(Comment comment,Integer discussPostId,Integer olsCount) {
        //排除html标签的干扰
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        //过滤敏感词
        comment.setContent(sensitiveFilterUtil.doFilter(comment.getContent()));
        //插入
        Integer result = commentMapper.insertComment(comment);
        //将帖子评论数加1
        if (comment.getEntityType()==1) {
            discussPostMapper.updateDisCussPostCommentCount(++olsCount, discussPostId);
        }
        return result;
    }
}
