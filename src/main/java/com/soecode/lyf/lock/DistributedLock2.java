package com.soecode.lyf.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @Description:分布式锁:现获取本地锁，成功再去获取分布式锁，实现方式上更轻量级，适合分布式环境跑定时器
 * @Author:xzx
 * @date:2020/3/29 0029
 **/
//@Component
@Slf4j
public class DistributedLock2 {
    private static ReentrantLock lock = new ReentrantLock();
    @Autowired
    private JedisCluster jedisCluster;


    /**
     *
     * @param key 获取锁的key
     * @param supplier 锁获取成功需要调用的方法
     * @param timeoutSeconds  获锁超时时间
     * @param <V>  返回值
     * @return
     * @throws InterruptedException
     */
    public void execute(String key,Runnable runnable,long timeoutSeconds){
        if(!lock.tryLock()){
            //获取本地所失败
            return;
        }
        //本地可重入锁竞争成功
        try{
            //获取redis锁
            if(tryDistributedLock(key,timeoutSeconds)){
                //获取锁成功
                tryCatch(runnable);
                //自己加的锁自己释放
                this.jedisCluster.del(key);
            }
        }finally {
            lock.unlock();
        }
    }

    private boolean tryDistributedLock(String key,long timeoutSeconds){
        if(this.jedisCluster.setnx(key,"1") == 1L){
            //设置锁成功,设置过期时间
            this.jedisCluster.expire(key,(int)timeoutSeconds);
            return true;
        }
        return false;
    }

    private void tryCatch(Runnable runnable){
        try{
            runnable.run();
        }catch (Exception e){
            log.error("catch a exception.",e);
        }
    }
}