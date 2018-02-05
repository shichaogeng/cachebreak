package com.gengsc.cachebreak;

import com.gengsc.cachebreak.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author shichaogeng
 * @Create 2018-02-02 14:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserCacheTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() throws Exception {
        stringRedisTemplate.opsForValue().set("aaa", "111");
    }

    @Test
    public void testObj() throws Exception {
        User user = new User("aa", 123);
        user.setId(1L);
        ValueOperations<String, User> operations = redisTemplate.opsForValue();
        operations.set("user:"+user.getId(), user, 1, TimeUnit.SECONDS);
        //redisTemplate.delete("com.neo.f");
        boolean exists = redisTemplate.hasKey("user:"+user.getId());
        if (exists) {
            System.out.println("exists is true");
        } else {
            System.out.println("exists is false");
        }
    }

    @Test
    public void testGetCache() throws Exception {

    }
}
