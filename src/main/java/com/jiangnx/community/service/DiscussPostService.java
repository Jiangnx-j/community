package com.jiangnx.community.service;

import com.jiangnx.community.entity.DiscussPost;
import com.jiangnx.community.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface DiscussPostService {

    List<DiscussPost> findDiscussPosts(Integer userId,Integer offset,Integer limit);

    Integer  findDiscussPostRows(@Param("userId") Integer userId);

    Map<String,Object> addDiscussPost(User user, DiscussPost discussPost);

    DiscussPost findDiscussPostById(Integer id);

}
