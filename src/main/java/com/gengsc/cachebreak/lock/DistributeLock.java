package com.gengsc.cachebreak.lock;

/**
 * @Description
 * 分布式锁接口
 * @Author shichaogeng
 * @Create 2018-02-05 15:57
 */
public interface DistributeLock {

    Boolean getLock();

    void releaseLock();
}
