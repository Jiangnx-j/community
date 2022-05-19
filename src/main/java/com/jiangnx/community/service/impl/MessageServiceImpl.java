package com.jiangnx.community.service.impl;

import com.jiangnx.community.dao.MessageMapper;
import com.jiangnx.community.entity.Message;
import com.jiangnx.community.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<Message> findConversationByUserId(Integer userId, Integer offset, Integer limit) {
        return messageMapper.selectConversationByUserId(userId,offset,limit);
    }

    @Override
    public Integer findConversationCountByUserId(Integer userId) {

        return messageMapper.selectConversationCountByUserid(userId);
    }

    @Override
    public List<Message> findLetterByConId(String conId, Integer offset, Integer limit) {

        return messageMapper.selectLetterByConversationId(conId,offset,limit);
    }

    @Override
    public Integer findLetterCountByConId(String conId) {

        return messageMapper.selectLetterCountByConversationId(conId);
    }

    @Override
    public Integer findUnReadLetterByUserId(Integer userId, String conId) {

        return messageMapper.selectUnReadMessage(userId,conId);
    }
}
