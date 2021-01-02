package com.soecode.lyf.manage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @Description:封装redis基于string以及hash的通用操作
 * @Author:xzx
 * @date:2020/3/29 0029
 **/
@Component
@Slf4j
public class JedisClusterManager {
    @Autowired
    private JedisCluster jedisCluster;

    public <K,V> V computeByFuntionIfAbsent(String key, K k, TypeReference<V> typeReference, Function<K, V> function,int timeoutSeconds){
        //redis挂了不影响后续业务，继续往下走
        String bizJson = this.tryCatch(() -> this.jedisCluster.get(key));
        if(bizJson != null){
            return JSON.parseObject(bizJson,typeReference);
        }

        V value = function.apply(k);
        if(value != null){
            //redis挂了不影响后续业务，继续往下走
            this.tryCatch(() -> {
                this.jedisCluster.set(key,JSON.toJSONString(value));
                this.jedisCluster.expire(key,timeoutSeconds);
            });
        }
        return value;
    }

    public <V> V computeBySupplierIfAbsent(String key, TypeReference<V> typeReference, Supplier<V> supplier, int timeoutSeconds){
        String bizJson = this.tryCatch(() -> this.jedisCluster.get(key));
        if(bizJson != null){
            return JSON.parseObject(bizJson,typeReference);
        }
        V value = supplier.get();
        if(value != null){
            this.tryCatch(()->{
                this.jedisCluster.set(key,JSON.toJSONString(value));
                this.jedisCluster.expire(key,timeoutSeconds);
            });
        }
        return value;
    }

    /**
     * 封装通用的redis查询hash结构数据的缓存逻辑
     * @param key  redis对应的hash的key
     * @param fieldList 要查询的hash列表
     * @param typeReference 解析查询的对象引用
     * @param priFunction 查询对象存储在redis的主键函数
     * @param function  redis里面没有，去存储里面加载的函数
     * @param timeoutSeconds 函数key的时间
     * @param <T> 查询对象的主键
     * @param <V> 查询对象
     * @return
     */
    public <T,V> List<V> computeHashByFunctionIfAbsent(String key,List<String> fieldList,
                                                       TypeReference<V> typeReference,
                                                       Function<V,T> priFunction,
                                                       Function<List<String>, List<V>> function,
                                                       int timeoutSeconds){
        if(CollectionUtils.isEmpty(fieldList)){
            return Lists.newArrayList();
        }
        List<String> valueList = this.tryCatch(() -> this.jedisCluster.hmget(key,fieldList.toArray(new String[0])));

        List<V> resultList = Lists.newArrayList();

        List<String> queryList = Lists.newArrayList(fieldList);
        if(CollectionUtils.isNotEmpty(valueList)){
            //解析对象
            List<V> cacheList = valueList.stream()
                                    .filter(Objects::nonNull)
                                    .map(item -> JSON.parseObject(item,typeReference))
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toList());

            if(CollectionUtils.isNotEmpty(cacheList)){
                queryList.removeAll(
                        cacheList.stream()
                                 .map(item -> String.valueOf(priFunction))
                                 .collect(Collectors.toList())
                );
                resultList.addAll(cacheList);
            }
        }

        if(CollectionUtils.isEmpty(queryList)){
            //缓存全部命中，直接返回
            return resultList;
        }

        //缓存没有，查询指定存储，
        List<V> sourceList = function.apply(queryList);
        if(CollectionUtils.isNotEmpty(sourceList)){
            Map<String,String> fieldMap = sourceList.stream().collect(
                    Collectors.toMap(
                            item -> String.valueOf(priFunction),
                            item -> JSON.toJSONString(Function.identity())
                    )
            );
            this.tryCatch(() -> {
                this.jedisCluster.hmset(key,fieldMap);
                this.jedisCluster.expire(key,timeoutSeconds);
            });
            resultList.addAll(sourceList);
        }

        return resultList;
    }


    /**
     * try-catch nihao
     * @param runnable
     */
    private void tryCatch(Runnable runnable){
        try{
            runnable.run();
        }catch (Exception e){
            log.error("catch a exception.",e);
        }
    }

    private <T> T tryCatch(Supplier<T> supplier){
        try{
            return supplier.get();
        }catch (Exception e){
            log.error("catch a exception.",e);
            return null;
        }
    }
}