package com.jiangnx.community.service;

import com.jiangnx.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DiscussPostService {

    List<DiscussPost> findDiscussPosts(Integer userId,Integer offset,Integer limit);

    Integer  findDiscussPostRows(@Param("userId") Integer userId);

}
