package com.jiangnx.community.controller;

import com.jiangnx.community.dao.MessageMapper;
import com.jiangnx.community.entity.Message;
import com.jiangnx.community.entity.Page;
import com.jiangnx.community.entity.User;
import com.jiangnx.community.service.MessageService;
import com.jiangnx.community.service.UserService;
import com.jiangnx.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/letter")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @GetMapping("/list")
    public String getLetterList(Model model, Page page){
        User user = hostHolder.getUser();
        //获取总的会话数量
        Integer count = messageService.findConversationCountByUserId(user.getId());

        page.setTotal(count);
        page.setPath("/letter/list");
        page.setLimit(8);

        //用于保存返回给前端的数据
        List<Map<String,Object>> conversationVoLost = new ArrayList<>();

        List<Message> conversations = messageService.findConversationByUserId(user.getId(), page.getOffset(), page.getLimit());

        if (conversations!=null){
            for(Message conversation:conversations){
                Map<String,Object> conversationVo = new HashMap<>();
                //添加会话信息
                conversationVo.put("con",conversation);
                User targetUser = null;
                //确定聊天的对象
                //如果登录的个人id与会话中发信人的id相同，那么聊天对象id就是toId，否则就是fromId
                if (user.getId() == conversation.getFromId()){
                   targetUser = userService.findUserById(conversation.getToId());
                }else {
                    targetUser = userService.findUserById(conversation.getFromId());
                }
                conversationVo.put("targetUser", targetUser);
                Integer unReadMessage = messageService.findUnReadLetterByUserId(user.getId(), conversation.getConversationId());
                Integer totalMessage = messageService.findLetterCountByConId(conversation.getConversationId());
                conversationVo.put("unRead", unReadMessage);
                conversationVo.put("totalMsg", totalMessage);
                conversationVoLost.add(conversationVo);
            }
            model.addAttribute("list",conversationVoLost);
            model.addAttribute("totalUnRead",messageService.findUnReadLetterByUserId(user.getId(),null));

        }

        return "site/letter";
    }
}
