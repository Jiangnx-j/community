package com.jiangnx.community.mappertest;

import com.jiangnx.community.CommunityApplication;
import com.jiangnx.community.dao.CommentMapper;
import com.jiangnx.community.entity.Comment;
import com.sun.org.apache.bcel.internal.classfile.SourceFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class CommentMapperTest {

    @Autowired
    private CommentMapper commentMapper;

    @Test
    public void testSeletcCommentByEntity() {
        List<Comment> comments = commentMapper.selectCommentsByEntity(1, 277, 1, 5);
        for(Comment comment:comments){
            System.out.println(comment);
        }
    }

    @Test
    public void testSelectCount(){
        System.out.println(commentMapper.selectCommentCount(1, 277));
    }
}
