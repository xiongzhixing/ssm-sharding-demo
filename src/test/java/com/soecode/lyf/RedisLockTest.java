package com.soecode.lyf;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 利用redis锁，分支定时任务方法被反复调用，造成服务器资源损耗
 */
public class RedisLockTest {
    private RedisObject redisObject = new RedisObject();
    public void lock(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String cacheKey = sdf.format(new Date());
        boolean lockSuccess = redisObject.setNx(cacheKey);
        if(!lockSuccess){
            return;
        }

        //TODO 执行业务代码操作


        unlock(cacheKey);
    }

    public void unlock(String cacheKey){
        redisObject.del(cacheKey);
    }

    public static class RedisObject{
        public boolean setNx(String key){
            return true;
        }

        public boolean del(String key){
            return true;
        }
    }
}
