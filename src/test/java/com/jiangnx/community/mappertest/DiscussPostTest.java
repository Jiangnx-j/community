package com.jiangnx.community.mappertest;

import com.jiangnx.community.CommunityApplication;
import com.jiangnx.community.dao.DiscussPostMapper;
import com.jiangnx.community.entity.DiscussPost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class DiscussPostTest {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testSelectDiscussRows(){
        System.out.println(discussPostMapper.selectDiscussPostRows(0));
    }

    @Test
    public void testSelectDiscussPosts(){
        System.out.println(discussPostMapper.selectDiscussPosts(0, 0, 5));
    }

    @Test
    public void testInsert(){
        DiscussPost discussPost = new DiscussPost();
        discussPost.setCreateTime(new Date());
        discussPost.setCommentCount(0);
        discussPost.setScore(0);
        discussPost.setUserId(151);
        discussPost.setContent("test");
        discussPost.setTitle("testtile");
        discussPost.setType(0);

        discussPostMapper.insertDiscussPost(discussPost);
    }
}
