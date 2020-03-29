package com.soecode.lyf.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @Description:分布式锁
 * @Author:xzx
 * @date:2020/3/29 0029
 **/
@Component
public class DistributedLock {
    private static boolean isExecuteLock = true;
    @Autowired
    private RedissonClient redissonClient;

    public <V> V execute(String key, Supplier<V> supplier,long timeoutSeconds) throws InterruptedException {
        if(!isExecuteLock){
            return supplier.get();
        }
        RLock rLock = redissonClient.getLock(key);

        boolean isSuccess = false;
        try{
            isSuccess = rLock.tryLock(timeoutSeconds, TimeUnit.SECONDS);
            if(isSuccess){
                return supplier.get();
            }else{
                throw new RuntimeException("操作太频繁，稍后重试");
            }
        }finally {
            if(isSuccess){
                rLock.unlock();
            }
        }
    }

    public <V> V execute(List<String> keyList, Supplier<V> supplier, long timeoutSeconds) throws InterruptedException {
        if(!isExecuteLock){
            return supplier.get();
        }

        List<RLock> rLockList = createLocks(keyList);
        boolean isSuccess = false;
        try{
            isSuccess = tryLock(rLockList,timeoutSeconds);
            if(isSuccess){
                return supplier.get();
            }else{
                throw new RuntimeException("操作太频繁，稍后重试");
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