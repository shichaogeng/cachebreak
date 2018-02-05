package com.gengsc.cachebreak.cache;

/**
 * @Description
 * @Author shichaogeng
 * @Create 2018-02-05 10:30
 */
public interface CacheService {

    String cacheGet(String key, String cacheName);

    void cacheRemove(String key, String cacheName);

    void cachePut(String key, String value, String cacheName);

}
