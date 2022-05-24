package com.jiangnx.community.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface LikeService {

    //给帖子，评论等实体点赞
    void like(Integer entityType,Integer entityId,Integer userId,Integer authorId);

    //通过实体类型，实体id查询点赞数量
    Long getLikeCount(Integer entityType,Integer entityId);

    //查询用户是否已经点赞
    Integer getLikeStatusByUserid(Integer entityType,Integer entityId,Integer userId);

    //获取用户获得的点赞的数量
    Integer getLikeCountUser(Integer userId);

    /**
     * 给作者点关注，关注时
     * 1. 用户自己关注的人+1 follower
     * 2. 博主粉丝+1    followee
     * @param userId    用户自己的Id
     * @param followerId    自己关注的人的id
     */
    boolean follower(Integer userId,Integer followerId);

    //查询用户是否关注了这个博主

    /**
     * 判断followerId 是否是 userId 的粉丝
     * @param userId    作者id
     * @param followerId 粉丝id
     * @return
     */
    boolean isFollower(Integer userId,Integer followerId);

    //查询用户粉丝数
    long findFolloweeCount(Integer userId);
    //查询用户关注数
    long findFollowerCount(Integer userId);

    //获取用户关注列表
    List<Map<String,Object>> getUserFollowers(Integer userId,Integer offset,Integer limit);

    //获取用户粉丝列表
    List<Map<String,Object>> getUserFollowees(Integer userId,Integer offset,Integer limit);

}
