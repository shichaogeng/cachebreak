package com.gengsc.cachebreak.cache.impl;

import com.gengsc.cachebreak.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author shichaogeng
 * @Create 2018-02-05 10:30
 */
@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    private CacheManager cacheManager;

    @Override
    public String cacheGet(String key, String cacheName) {
        Cache.ValueWrapper wrapper = cacheManager.getCache(cacheName).get(key);
        return wrapper == null ? null : (String) wrapper.get();
    }

    @Override
    public void cacheRemove(String key, String cacheName) {
        cacheManager.getCache(cacheName).evict(key);
    }

    @Override
    public void cachePut(String key, String value, String cacheName) {
        cacheManager.getCache(cacheName).put(key, value);
    }
}
