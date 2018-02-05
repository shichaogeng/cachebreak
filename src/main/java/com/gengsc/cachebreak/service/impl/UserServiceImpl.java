package com.gengsc.cachebreak.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gengsc.cachebreak.cache.CacheLoadback;
import com.gengsc.cachebreak.cache.CacheService;
import com.gengsc.cachebreak.cache.CacheTemplateService;
import com.gengsc.cachebreak.domain.User;
import com.gengsc.cachebreak.lock.DistributeLock;
import com.gengsc.cachebreak.mapper.UserMapper;
import com.gengsc.cachebreak.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @Description
 * @Author shichaogeng
 * @Create 2018-02-02 15:30
 */
@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CacheTemplateService cacheTemplateService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private DistributeLock lock;

    @Override
    public User getUser(Long id) {
        String cacheName = "userCache";
        String key = "user:" + id;
        User user = cacheTemplateService.findCache(key, cacheName, new Date(), User.class, new CacheLoadback<User>() {
            @Override
            public User load() {
                return userMapper.getOne(id);
            }
        });

        return user;

    }

    @Override
    public User getUserWithBackCache(Long id) {
        String cacheName = "userCache";
        String key = "user:" + id;
        String backupCacheName = cacheName+"_backup";
        String backupKey = key+"_backup";
        String jsonString = cacheService.cacheGet(key, cacheName);
        if (!StringUtils.isEmpty(jsonString)) {
            logger.debug("query from main cache");
            User user = JSONObject.parseObject(jsonString, User.class);
            return user;
        }

        //获取分布式锁
        if (lock.getLock()) {//获取到锁 查询数据库 刷新缓存
            logger.debug("query from db");
            User user = userMapper.getOne(id);
            cacheService.cachePut(key, JSONObject.toJSONString(user), cacheName);//刷新主缓存
            cacheService.cachePut(backupKey, JSONObject.toJSONString(user), backupCacheName);//刷新备份缓存
            lock.releaseLock();
            return user;
        } else {
            logger.debug("query from backup cache");
            jsonString = cacheService.cacheGet(backupKey, backupCacheName);
            User user = JSONObject.parseObject(jsonString, User.class);;
            return user;
        }

    }
}
