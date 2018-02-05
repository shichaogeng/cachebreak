package com.gengsc.cachebreak.cache;

import com.alibaba.fastjson.JSONObject;
import com.gengsc.cachebreak.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @Description
 * @Author shichaogeng
 * @Create 2018-02-02 17:22
 */
@Service
public class CacheTemplateService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private CacheService cacheService;

    public <T> T findCache(String key, String cacheName, Date expire, Class<T> clazz, CacheLoadback<T> loadback) {
        String jsonString = cacheService.cacheGet(key, cacheName);
        if (!StringUtils.isEmpty(jsonString)) {
            logger.debug("query from cache");
            return JSONObject.parseObject(jsonString, clazz);
        }

        synchronized (this) {
            jsonString = cacheService.cacheGet(key, cacheName);
            if (!StringUtils.isEmpty(jsonString)) {
                logger.debug("synchronized query from cache");
                return JSONObject.parseObject(jsonString, clazz);
            }
            T t = loadback.load();
            logger.debug("synchronized query from db");
            cacheService.cachePut(key, JSONObject.toJSONString(t), cacheName);
            return t;
        }

    }
}
