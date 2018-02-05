package com.gengsc.cachebreak;

import com.alibaba.fastjson.JSONObject;
import com.gengsc.cachebreak.domain.User;
import com.gengsc.cachebreak.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @Description
 * @Author shichaogeng
 * @Create 2018-02-02 15:36
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheBreakDownTest {

    private static Logger logger = LoggerFactory.getLogger(CacheBreakDownTest.class);
    private final Integer threadNum = 10;
    private final CountDownLatch latch = new CountDownLatch(threadNum);

    @Autowired
    private UserService userService;

    @Test
    public void testCacheBreak() throws Exception {

        for (int i = 0; i < threadNum; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    User user = userService.getUserWithBackCache(4L);
//                    User user = userService.getUser(4L);
                    logger.debug(JSONObject.toJSONString(user));
                }
            });
            t.start();
            latch.countDown();
        }

        Thread.currentThread().sleep(5000);
    }

    @Test
    public void testGetUser() {
        User user = userService.getUserWithBackCache(2L);
        System.out.println(user);
    }

    @Autowired
    private RedisConnectionFactory factory;

    @Test
    public void testJedis() {
        Jedis jedis = (Jedis) factory.getConnection().getNativeConnection();
        String value = UUID.randomUUID().toString();
        String ret = jedis.set("LOCK", value, "NX", "PX", 10000);
        System.out.println(ret);
    }

}
