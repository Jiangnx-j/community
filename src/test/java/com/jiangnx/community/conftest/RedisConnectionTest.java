package com.jiangnx.community.conftest;

import com.jiangnx.community.CommunityApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisConnectionTest {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate template;

    @Test
    public void testRedis(){
        String stringKey = "string:test01";
        template.opsForValue().set(stringKey,1);
        System.out.println(template.opsForValue().get(stringKey));
//        template.opsForValue().set(stringKey, user);
//        System.out.println(template.opsForValue().get(stringKey));
//        template.opsForValue().increment(stringKey);
//        System.out.println(template.keys("*"));
    }


    @Test
    public void testTrancation(){
        template.execute(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                String redisKey = "redis:tx";

                //开启事务
                redisOperations.multi();

                redisOperations.opsForSet().add((K)redisKey, (V)"111");
                redisOperations.opsForSet().add((K)redisKey, (V)"222");
                return redisOperations.exec();
            }
        });
    }
}
