package com.gengsc.cachebreak.lock.impl;

import com.gengsc.cachebreak.lock.DistributeLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import redis.clients.jedis.Jedis;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Description
 * @Author shichaogeng
 * @Create 2018-02-05 16:55
 */
@Service
public class RedisDistributeLock implements DistributeLock {

    @Autowired
    private RedisConnectionFactory factory;

    private static final String LOCK_NODE = "LOCK";

    private ThreadLocal<String> local = new ThreadLocal<>();

    @Override
    public Boolean getLock() {
        Boolean success = false;
        Jedis jedis = (Jedis) factory.getConnection().getNativeConnection();
        String value = UUID.randomUUID().toString();
        String ret = jedis.set("LOCK", value, "NX", "PX", 10000);

        if ("OK".equals(ret)) {
            local.set(value);
            success = true;
        }
        jedis.close();
        return success;
    }

    @Override
    public void releaseLock() {
        String script =null;
        try {
            script = FileCopyUtils.copyToString(new FileReader(ResourceUtils.getFile("classpath:unlock.lua")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Jedis jedis = (Jedis) factory.getConnection().getNativeConnection();
        List<String> keys = new ArrayList();
        keys.add(LOCK_NODE);
        List<String> args = new ArrayList();
        args.add(local.get());
        jedis.eval(script, keys, args);
        jedis.close();
    }

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString());
    }
}
