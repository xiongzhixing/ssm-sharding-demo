package com.soecode.lyf.manage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Description:封装缓存通用逻辑
 * @Author:xzx
 * @date:2020/3/29 0029
 **/
@Component
public class JedisClusterManager {
    @Autowired
    private JedisCluster jedisCluster;

    public <K,V> V computeByFuntionIfAbsent(String key, K k, TypeReference<V> typeReference, Function<K, V> function,int timeoutSeconds){
        String bizJson = this.jedisCluster.get(key);
        if(bizJson != null){
            return JSON.parseObject(bizJson,typeReference);
        }
        V value = function.apply(k);
        if(value != null){
            this.jedisCluster.set(key,JSON.toJSONString(value));
            this.jedisCluster.expire(key,timeoutSeconds);
        }
        return value;
    }

    public <V> V computeBySupplierIfAbsent(String key, TypeReference<V> typeReference, Supplier<V> supplier, int timeoutSeconds){
        String bizJson = this.jedisCluster.get(key);
        if(bizJson != null){
            return JSON.parseObject(bizJson,typeReference);
        }
        V value = supplier.get();
        if(value != null){
            this.jedisCluster.set(key,JSON.toJSONString(value));
            this.jedisCluster.expire(key,timeoutSeconds);
        }
        return value;
    }
}