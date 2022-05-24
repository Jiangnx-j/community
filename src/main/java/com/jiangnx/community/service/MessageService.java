package com.jiangnx.community.service;

import com.jiangnx.community.entity.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService {

    List<Message> findConversationByUserId(Integer userId,Integer offset,Integer limit);

    Integer findConversationCountByUserId(Integer userId);

    List<Message> findLetterByConId(String conId,Integer offset,Integer limit,Integer unReadCount);

    Integer findLetterCountByConId(String conId);

    Integer findUnReadLetterByUserId(Integer userId,String conId);

    Integer sendMessage(Message message);
}
