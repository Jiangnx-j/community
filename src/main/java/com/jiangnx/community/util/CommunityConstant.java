package com.jiangnx.community.util;

/**
 * 激活三种状态常量
 */
public interface CommunityConstant {
    //激活成功
    Integer ACTIVATION_SUCCESS = 0;

    //重复激活
    Integer ACTIVATION_REPEAT = 1;

    //激活失败
    Integer ACTIVATION_FALSE = 2;

    // 默认ticket的生效时间 12个小时 单位：秒
    long DEFAULT_EXPIRED =  60 * 60 * 12;

    //选择记住我选项的ticket的生效时间 100天
    long REMEMBER_EXPIRED = 60 * 60 * 24 * 100;
}
