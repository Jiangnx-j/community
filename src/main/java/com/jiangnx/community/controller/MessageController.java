package com.jiangnx.community.controller;

import com.jiangnx.community.annotation.LoginRequried;
import com.jiangnx.community.dao.MessageMapper;
import com.jiangnx.community.entity.Message;
import com.jiangnx.community.entity.Page;
import com.jiangnx.community.entity.User;
import com.jiangnx.community.service.MessageService;
import com.jiangnx.community.service.UserService;
import com.jiangnx.community.util.CommunityUtil;
import com.jiangnx.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/letter")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequried
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

    @LoginRequried
    @GetMapping("/detail/{conId}")
    public String letterDetail(@PathVariable("conId") String conId,Page page,Model model){
        page.setLimit(10);
        page.setPath("/letter/detail/"+conId );
        page.setTotal(messageService.findLetterCountByConId(conId));
        User me = hostHolder.getUser();

        //查询未读消息数量
        Integer unReadMsgCount = messageService.findUnReadLetterByUserId(me.getId(),conId);
        //在用户点击会话详情时，应该清楚会话未读消息数量
        List<Message> letters = messageService.findLetterByConId(conId,page.getOffset(),page.getLimit(),unReadMsgCount);
        User targetUser = null;

        List<Map<String,Object>> msgVoList = new ArrayList<>();
        for(Message letter:letters){
            Map<String,Object> msgVo = new HashMap<>();
            User fromUser = userService.findUserById(letter.getFromId());
            msgVo.put("message", letter);
            msgVo.put("fromUser", fromUser);
            msgVoList.add(msgVo);
        }
        if (me.getId() == letters.get(0).getFromId()){
            targetUser = userService.findUserById(letters.get(0).getToId());
        }else {
            targetUser = userService.findUserById(letters.get(0).getFromId());
        }
        model.addAttribute("targetUser",targetUser);
        model.addAttribute("msgVoList",msgVoList);
        return "/site/letter-detail";
    }

    @LoginRequried
    @PostMapping("/send")
    @ResponseBody
    public String sendMessage(String targetName,String content){
        User user = hostHolder.getUser();
        User targetUser = userService.findByName(targetName);
        if (targetUser == null){
            return CommunityUtil.getJSONString(1,"用户不存在", null);
        }

        Message message = new Message();
        message.setFromId(user.getId());
        message.setToId(targetUser.getId());
        message.setContent(content);
        message.setCreateTime(new Date());
        message.setStatus(0);
        String conid = null;
        if (targetUser.getId() < user.getId()){
            conid = targetUser.getId() + "_" + user.getId();
        }else {
            conid = user.getId() + "_" + targetUser.getId();
        }
        message.setConversationId(conid);
        Integer result = messageService.sendMessage(message);
       if (result == 1){
           return CommunityUtil.getJSONString(0,"发送成功", null);
       }else {
           return CommunityUtil.getJSONString(1,"发送失败", null);
       }
    }

}
