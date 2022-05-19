package com.jiangnx.community.dao;

import com.jiangnx.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    /**
     * 使用动态Sql去实现，在查询全部帖子时不会用上userID
     * @param userId
     *为0表示查询全部帖子。不为零表示查询用户发布的帖子
     * @param offset
     * 当前页面
     * @param limit
     * 页面大小
     * @return
     */
    List<DiscussPost> selectDiscussPosts(@Param("userId") Integer userId, @Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 查找所有帖子的数量或者个人帖子的数量
     * @param userId
     * @return
     * 返回值是帖子的总数
     */
    Integer selectDiscussPostRows(@Param("userId") Integer userId);

    Integer insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(Integer id);

    Integer updateDisCussPostCommentCount(@Param("count") Integer count,@Param("id") Integer id);

}
