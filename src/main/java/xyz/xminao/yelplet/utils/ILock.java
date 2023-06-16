package xyz.xminao.yelplet.utils;

public interface ILock {

    /**
     * 尝试获取锁
     * @param timeoutSec 锁持有的超时时间，过期自动释放锁
     * @return true 获取锁成功 false 获取锁失败
     */
    boolean tryLock(long timeoutSec);

    // 释放锁
    void unlock();
}
