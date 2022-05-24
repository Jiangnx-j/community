package com.jiangnx.community.dao;

import com.jiangnx.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    //message的一条记录存储的是一条消息，一条消息有发信人，收信人，会话id（会话id由发信人和收信人确定）
    //查询与用户相关的会话，只需要最新的message，每个conversation_id只需要一个最新的消息即可
    //四个关键 1. 与用户相关 2. 最新消息 3. 根据conversation_id分组 4. 分页
    List<Message> selectConversationByUserId(@Param("userId") Integer userId,
                                             @Param("offset") Integer offset, @Param("limit") Integer limit);

    //查询与用户相关的会话的数量。主要用于分页
    Integer selectConversationCountByUserid(Integer userId);

    //根据会话id查询消息
    List<Message> selectLetterByConversationId(@Param("conId")String conid,
                                               @Param("offset")Integer offset,@Param("limit")Integer limit);

    //查询会话id查询消息总数
    Integer selectLetterCountByConversationId(String conId);

    //查询未读消息数量
    Integer selectUnReadMessageCount(@Param("userId")Integer userId,@Param("conId")String conId);

    //查询未读消息id
    List<Integer> selectUnReadMessage(@Param("userId")Integer userId,@Param("conId")String conId);

    //发消息，添加message
    Integer insertMessage(Message message);

    //将消息设置为已读
    Integer updateStatus(List<Integer> ids);
}
