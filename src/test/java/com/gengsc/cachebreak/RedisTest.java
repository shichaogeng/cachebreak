package com.gengsc.cachebreak;

import com.gengsc.cachebreak.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Description
 * @Author shichaogeng
 * @Create 2018-02-02 14:56
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisConnectionFactory factory;

    @Autowired
    private RedisTemplate<String, Object> template;

    @Test
    public void testRedis() {
        RedisConnection connection = factory.getConnection();
        connection.set("hello".getBytes(), "world".getBytes());
        System.out.println(new String(connection.get("hello".getBytes())));
    }

    @Test
    public void testRedisTemplate() {
        ValueOperations<String, Object> ops = template.opsForValue();
        ops.set("key1", "value1");
        System.out.println(ops.get("key1"));
    }

    @Test
    public void testRedisHash() {
        HashOperations<String, Object, Object> ops = template.opsForHash();
        User tom = new User("tom", 23);
        tom.setId(1L);
        ops.put("userhash", tom.getId(), tom);
        System.out.println(ops.get("userhash", tom.getId()));
    }

    @Test
    public void testRedisTemplateList(){

        User tom = new User("tom", 23);
        tom.setId(1L);
        User jerry = new User("jerry", 24);
        jerry.setId(2L);

        //依次从尾部添加元素
        ListOperations<String, Object> ops = template.opsForList();
        ops.rightPush("user", tom);
        ops.rightPush("user", jerry);

        //查询索引0到商品总数-1索引（也就是查出所有的商品）
        List<Object> prodList = ops.range("user", 0, ops.size("user") - 1);
        for(Object obj:prodList){
            System.out.println((User)obj);
        }
        System.out.println("用户数量:"+ ops.size("user"));

    }

}
