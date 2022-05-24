package com.jiangnx.community.util;

public class RedisUtil {
    public static final String SPLIT = ":";
    //文章、评论等实体保存点赞的key的前缀
    public static final String PREIFX_ENTITY_LIKE = "like:entity";
    //用户获得点赞数量key的前缀
    public static final String PREIFX_USERCOUNT_LIKE = "likeCount:user";
    //关注列表key的前缀
    public static final String PREFIX_USER_FOLLOWER = "follower:user";
    //粉丝列表key的前缀
    public static final String PREFIX_USER_FOLLOWEE = "followee:user";
    //验证码前缀
    public static final String PREFIX_VERIFYCODE = "kaptcha";

    //获取保存实体点赞的key
    //实体点赞是将前缀+实体类型+实体id作为set的key，然后将点赞人的id放入set中
    //一个实体对应一个key
    public static String getEntityLikeKey(Integer entitytype,Integer entityId){
        return PREIFX_ENTITY_LIKE + SPLIT +entitytype + SPLIT +entityId;
    }

    //获取保存用户点赞数量的Key，一个用户使用一个key
    public static String getUserLikeKey(Integer userId){
        return PREIFX_ENTITY_LIKE + SPLIT +userId;
    }

    //保存的是自己关注的人的
    public static String getUserFollowerKey(Integer userId){
        return PREFIX_USER_FOLLOWER + SPLIT + userId;
    }

    //保存的是关注自己的人
    public static String getUserFolloweeKey(Integer userId){
        return PREFIX_USER_FOLLOWEE + SPLIT + userId;
    }

    //返回验证码Key
    public static String getVerifyCodeKey(String verifycodeOwn){
        return PREFIX_VERIFYCODE + SPLIT + verifycodeOwn;
    }
}
