package com.jiangnx.community.service.impl;

import com.jiangnx.community.entity.User;
import com.jiangnx.community.service.LikeService;
import com.jiangnx.community.service.UserService;
import com.jiangnx.community.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Override
    public void like(Integer entityType, Integer entityId, Integer userId, Integer authorId) {
/*        String likeKey = RedisUtil.getEntityLikeKey(entityType,entityId);
        //查询用户是否已经点赞
        Boolean member = redisTemplate.opsForSet().isMember(likeKey, userId);
        //如果存在表示已经点赞了，需要取消点赞
        if(member){
            redisTemplate.opsForSet().remove(likeKey,userId);
        }else {
            redisTemplate.opsForSet().add(likeKey,userId);
        }*/

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String likeKey = RedisUtil.getEntityLikeKey(entityType, entityId);
                String likeUserKey = RedisUtil.getUserLikeKey(authorId);
                //查询用户是否已经点赞
                Boolean member = redisTemplate.opsForSet().isMember(likeKey, userId);
                operations.multi();
                if (member) {
                    redisTemplate.opsForSet().remove(likeKey, userId);
                    //改变作者获得点赞的数量
                    redisTemplate.opsForValue().decrement(likeUserKey);
                } else {
                    redisTemplate.opsForSet().add(likeKey, userId);
                    redisTemplate.opsForValue().increment(likeUserKey);
                }
                return operations.exec();
            }
        });

    }

    @Override
    public Long getLikeCount(Integer entityType, Integer entityId) {
        String likeKey = RedisUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(likeKey);
    }

    @Override
    public Integer getLikeStatusByUserid(Integer entityType, Integer entityId, Integer userId) {
        String likeKey = RedisUtil.getEntityLikeKey(entityType, entityId);
        Boolean member = redisTemplate.opsForSet().isMember(likeKey, userId);
        //1表示已赞，0表示未赞
        return member == true ? 1 : 0;
    }

    //返回用户获得点赞的数量
    @Override
    public Integer getLikeCountUser(Integer userId) {
        String likeCountUserKey = RedisUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(likeCountUserKey);
        return count == null ? 0 : count;
    }

    /**
     * @param userId     用户自己的Id
     * @param followerId 自己关注的人的id
     * @return 返回方法执行之后，用户的关注状态，是关注了这个作者。还是没关注，方便前端判断
     */
    @Override
    public boolean follower(Integer userId, Integer followerId) {
        //用户自己的关注列表
        String userFolloweeKey = RedisUtil.getUserFolloweeKey(userId);
        //用户关注的作者的粉丝列表
        String followerKey = RedisUtil.getUserFollowerKey(followerId);
        //先查询作者的粉丝列表中是否有自己
        //也可以查询自己的关注列表中是否有作者
        boolean follower = isFollower(followerId, userId);
        System.out.println(follower);
        //关注不仅需要将作者的id加入到用户的关注列表中，还需要将用户的id加入到作者的粉丝列表中，是一个事务
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                //如果关注了就取消关注
                operations.multi();
                if (follower) {
                    //用户取消关注，将关注列表中作者的id删除
                    operations.opsForZSet().remove(userFolloweeKey, followerId);
                    //作者的粉丝列表中移除用户的id
                    operations.opsForZSet().remove(followerKey, userId);
                } else {
                    operations.opsForZSet().add(followerKey, userId, System.currentTimeMillis());
                    operations.opsForZSet().add(userFolloweeKey, followerId, System.currentTimeMillis());
                }
                return operations.exec();
            }
        });

        //follower查询的是没执行之前用户的关注的状态，代码执行之后，没关注——>关注 关注->没关注
        return !follower;
    }

    /**
     * 判断followerId 是否是 userId 的粉丝
     * @param userId    作者id
     * @param followerId 粉丝id
     * @return
     */
    @Override
    public boolean isFollower(Integer userId, Integer followerId) {
        //userId的粉丝列表
        String userFollowersKey = RedisUtil.getUserFollowerKey(userId);
        //判断userid的粉丝列表中是否有followerId
        return redisTemplate.opsForZSet().score(userFollowersKey, followerId) != null;
    }

    @Override
    public long findFolloweeCount(Integer userId) {
      String followees = RedisUtil.getUserFolloweeKey(userId);
        Long size = redisTemplate.opsForZSet().zCard(followees);
        return size == null ? 0 : size.longValue();
    }


    @Override
    public long findFollowerCount(Integer userId) {
        String userFollowersKey = RedisUtil.getUserFollowerKey(userId);
        Long size = redisTemplate.opsForZSet().zCard(userFollowersKey);
        return size == null ? 0 : size.longValue();
    }

    //粉丝列表
    @Override
    public List<Map<String, Object>> getUserFollowers(Integer userId, Integer offset, Integer limit) {
        String userFollowersKey = RedisUtil.getUserFollowerKey(userId);
        Set<Integer> set = redisTemplate.opsForZSet().reverseRange(userFollowersKey, offset, offset + limit - 1);
        if (set!=null){
            List<Map<String, Object>> list = new ArrayList<>();
            //获取用户粉丝的详细信息
            for (Integer id:set){
                Map<String,Object> map  = new HashMap<>();
                User user = userService.findUserById(id);
                map.put("user", user);
                //判断用户是否关注了这个用户
                boolean follower = isFollower(id, userId);
                map.put("isFollower", follower);
                //查询关注时间
                Double score = redisTemplate.opsForZSet().score(userFollowersKey, id);
                Date date = new Date(score.longValue());
                map.put("followerTime", date);
                list.add(map);
            }
            return list;
        }else {
            return null;
        }
    }

    //获取关注列表
    @Override
    public List<Map<String, Object>> getUserFollowees(Integer userId, Integer offset, Integer limit) {
        String userFolloweesKey = RedisUtil.getUserFolloweeKey(userId);
        Set<Integer> set = redisTemplate.opsForZSet().reverseRange(userFolloweesKey, offset, offset + limit - 1);
        if (set!=null){
            List<Map<String, Object>> list = new ArrayList<>();
            //获取用户粉丝的详细信息
            for (Integer id:set){
                Map<String,Object> map  = new HashMap<>();
                User user = userService.findUserById(id);
                map.put("user", user);
                //判断用户是否关注了这个用户
                boolean follower = isFollower(id, userId);
                map.put("isFollower", follower);
                //查询关注时间
                Double score = redisTemplate.opsForZSet().score(userFolloweesKey, id);
                Date date = new Date(score.longValue());
                map.put("followerTime", date);
                list.add(map);
            }
            return list;
        }else {
            return null;
        }

    }

}
