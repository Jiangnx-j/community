package com.jiangnx.community.service;

import com.jiangnx.community.entity.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    List<Comment> findCommentByEntity(Integer entityType,Integer entityId,Integer offset,Integer limit);

    Integer selectCommentCountsByEntity(Integer entityType,Integer entityId);

    Integer addComment(Comment comment,Integer discussPostId,Integer oldConut);

}
