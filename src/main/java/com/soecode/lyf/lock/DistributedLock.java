package com.soecode.lyf.lock;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @Description:分布式锁:基于redisson实现可靠的分布式获取方式，可靠主要表现为：
 *                     1.通过Lua脚本实现原子的加锁并设置过期时间；
 *                     2.redisson内置watchdog机制。
 * @Author:xzx
 * @date:2020/3/29 0029
 **/
//@Component
@Slf4j
public class DistributedLock {
    private static boolean isExecuteLockDegrade = false;
    //@Autowired
    private RedissonClient redissonClient;

    /**
     *
     * @param key 获取锁的key
     * @param supplier 锁获取成功需要调用的方法
     * @param timeoutSeconds  获锁超时时间
     * @param <V>  返回值
     * @return
     * @throws InterruptedException
     */
    public <V> V execute(String key,Supplier<V> supplier,long timeoutSeconds,boolean isThrowExceptionWhenLockFail) throws InterruptedException {
        if(isExecuteLockDegrade){
            return supplier.get();
        }
        RLock rLock = redissonClient.getLock(key);

        boolean isSuccess = false;
        try{
            isSuccess = rLock.tryLock(timeoutSeconds, TimeUnit.SECONDS);
            if(isSuccess){
                return supplier.get();
            }else{
                log.warn("DistributedLock#execute get lock fail.");
                if(isThrowExceptionWhenLockFail){
                    throw new RuntimeException("操作太频繁，稍后重试");
                }
                return null;
            }
        }finally {
            if(isSuccess){
                rLock.unlock();
            }
        }
    }

    /**
     *
     * @param keyList 获取锁的key
     * @param supplier 锁获取成功需要调用的方法
     * @param timeoutSeconds  获锁超时时间
     * @return  <V>  返回值
     * @throws InterruptedException
     */
    public <V> V execute(List<String> keyList,Supplier<V> supplier, long timeoutSeconds,boolean isThrowExceptionWhenLockFail) throws InterruptedException {
        if(isExecuteLockDegrade){
            return supplier.get();
        }

        List<RLock> rLockList = createLocks(keyList);
        boolean isSuccess = false;
        try{
            isSuccess = tryLock(rLockList,timeoutSeconds);
            if(isSuccess){
                return supplier.get();
            }else{
                log.warn("DistributedLock#execute get lock fail.");
                if(isThrowExceptionWhenLockFail){
                    throw new RuntimeException("操作太频繁，稍后重试");
                }
                return null;
            }
        }finally {
            if(isSuccess){
                unlock(rLockList);
            }
        }
    }

    private void unlock(List<RLock> rLockList) {
        for(RLock rLock:rLockList){
            rLock.unlock();
        }
    }

    private boolean tryLock(List<RLock> rLockList,long timeoutSeconds) throws InterruptedException {
        for(RLock rLock:rLockList){
            if(!rLock.tryLock(timeoutSeconds,TimeUnit.SECONDS)){
                return false;
            }
        }
        return true;
    }

    private List<RLock> createLocks(List<String> keyList){
        return keyList.stream().map(key -> this.redissonClient.getLock(key)).collect(Collectors.toList());
    }

}