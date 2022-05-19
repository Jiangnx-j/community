package com.jiangnx.community.dao;

import com.jiangnx.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<Comment> selectCommentsByEntity(@Param("entityType") Integer entityType, @Param("entityId") Integer entityId,
                                         @Param("offset") Integer offset, @Param("limit") Integer limit);

    Integer selectCommentCount(@Param("entityType")Integer entityType,@Param("entityId")Integer entityId);

    Integer insertComment(Comment comment);
}

