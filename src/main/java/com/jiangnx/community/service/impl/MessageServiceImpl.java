package com.jiangnx.community.service.impl;

import com.jiangnx.community.dao.MessageMapper;
import com.jiangnx.community.entity.Message;
import com.jiangnx.community.entity.User;
import com.jiangnx.community.service.MessageService;
import com.jiangnx.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public List<Message> findConversationByUserId(Integer userId, Integer offset, Integer limit) {
        return messageMapper.selectConversationByUserId(userId,offset,limit);
    }

    @Override
    public Integer findConversationCountByUserId(Integer userId) {

        return messageMapper.selectConversationCountByUserid(userId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public List<Message> findLetterByConId(String conId, Integer offset, Integer limit,Integer unReadCount) {
        //将会话相关的未读消息设置为已读
        if (unReadCount!=0){
            User user = hostHolder.getUser();
            List<Integer> unReadList = messageMapper.selectUnReadMessage(user.getId(),conId);
            messageMapper.updateStatus(unReadList);
        }
        List<Message> messages = messageMapper.selectLetterByConversationId(conId, offset, limit);
        //为了使消息在显示是按照时间的先后顺序，将其进行翻转
        Collections.reverse(messages);
        return messages;
    }

    @Override
    public Integer findLetterCountByConId(String conId) {

        return messageMapper.selectLetterCountByConversationId(conId);
    }

    @Override
    public Integer findUnReadLetterByUserId(Integer userId, String conId) {

        return messageMapper.selectUnReadMessageCount(userId,conId);
    }

    @Override
    public Integer sendMessage(Message message) {
        return messageMapper.insertMessage(message);
    }
}
