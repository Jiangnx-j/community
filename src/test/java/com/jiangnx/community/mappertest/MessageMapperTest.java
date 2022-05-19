package com.jiangnx.community.mappertest;

import com.jiangnx.community.CommunityApplication;
import com.jiangnx.community.dao.MessageMapper;
import com.jiangnx.community.entity.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MessageMapperTest {

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void test(){
        List<Message> messages = messageMapper.selectConversationByUserId(111, 0, 15);
        for(Message message : messages){
            System.out.println(message);
        }

        System.out.println(messageMapper.selectConversationCountByUserid(111));

        List<Message> messages1 = messageMapper.selectLetterByConversationId("111_112", 0, 10);
        for(Message message : messages1){
            System.out.println(message);
        }

        System.out.println(messageMapper.selectLetterCountByConversationId("111_112"));
        System.out.println(messageMapper.selectUnReadMessage(131, null));
    }
}
