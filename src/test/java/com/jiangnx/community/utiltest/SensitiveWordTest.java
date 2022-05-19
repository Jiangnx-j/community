package com.jiangnx.community.utiltest;

import com.jiangnx.community.util.SensitiveFilterUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SensitiveWordTest {

    @Autowired
    private SensitiveFilterUtil sensitiveFilterUtil;
    @Test
    public void testFilter(){
        String text = "我爱喝酒,爱烫头发，喜欢赌博";
        System.out.println(sensitiveFilterUtil.doFilter(text));
    }
}
