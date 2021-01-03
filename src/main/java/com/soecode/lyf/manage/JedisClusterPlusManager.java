package com.soecode.lyf.manage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.soecode.lyf.util.GuavaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @Description:redis结合本地缓存，封装redis基于string以及hash的通用操作
 *              优点：redis结合本地缓存大大提高了接口的并发性；
 *              缺点：由于现在服务都是分布式部署，本地缓存可能没有及时更新，这里缓存有效时间设置为5秒，可能造成短暂的数据不一致，
 *                   对数据有强一致性要求的业务不推荐使用
 * @Author:xzx
 * @date:2020/3/29 0029
 **/
@Component
@Slf4j
public class JedisClusterPlusManager{
    private static final String CONCAT_STR = "_";
    private static Cache<String,String> cache;
    @Autowired
    private JedisCluster jedisCluster;

    @PostConstruct
    public void init(){
        Cache<String, String> cache = CacheBuilder.newBuilder()
                .initialCapacity(10)
                .maximumSize(1000)
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .expireAfterAccess(12, TimeUnit.HOURS)
                .build();
        JedisClusterPlusManager.cache = cache;
    }

    public <K,V> V computeByFuntionIfAbsent(String key, K k, TypeReference<V> typeReference, Function<K, V> function,int timeoutSeconds){
        //先读取本地缓存
        String bizJson = cache.getIfPresent(key);
        if(bizJson != null){
            return JSON.parseObject(bizJson,typeReference);
        }

        //redis挂了不影响后续业务，继续往下走
        bizJson = this.tryCatch(() -> this.jedisCluster.get(key));
        if(bizJson != null){
            cache.put(key,bizJson);
            return JSON.parseObject(bizJson,typeReference);
        }

        V value = function.apply(k);
        if(value != null){
            String queryBizJson = JSON.toJSONString(value);
            //存本地缓存
            cache.put(key,queryBizJson);
            //redis挂了不影响后续业务，继续往下走
            this.tryCatch(() -> {
                this.jedisCluster.set(key,queryBizJson);
                this.jedisCluster.expire(key,timeoutSeconds);
            });
        }
        return value;
    }

    public <V> V computeBySupplierIfAbsent(String key, TypeReference<V> typeReference, Supplier<V> supplier, int timeoutSeconds){
        //先读取本地缓存
        String bizJson = cache.getIfPresent(key);
        if(bizJson != null){
            return JSON.parseObject(bizJson,typeReference);
        }

        bizJson = this.tryCatch(() -> this.jedisCluster.get(key));
        if(bizJson != null){
            cache.put(key,bizJson);
            return JSON.parseObject(bizJson,typeReference);
        }
        V value = supplier.get();
        if(value != null){
            String queryBizJson = JSON.toJSONString(value);
            //存本地缓存
            cache.put(key,queryBizJson);
            //redis挂了不影响后续业务，继续往下走
            this.tryCatch(()->{
                this.jedisCluster.set(key,queryBizJson);
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
        List<V> resultList = Lists.newArrayList();
        //先读取本地缓存
        List<String> valueList = fieldList.stream()
                 .filter(Objects::nonNull)
                 .map(item -> new StringBuilder().append(key).append(CONCAT_STR).append(item).toString())
                 .map(item -> cache.getIfPresent(item))
                 .filter(Objects::nonNull)
                 .collect(Collectors.toList());

        List<String> queryList = Lists.newArrayList(fieldList);
        setHashResult(typeReference,priFunction,resultList, valueList, queryList);
        if(CollectionUtils.isEmpty(queryList)){
            //本地缓存全部命中,返回结果
            return resultList;
        }

        //存在没有命中的，去redis里面查询
        valueList = Optional.ofNullable(this.tryCatch(
                        () -> this.jedisCluster.hmget(key,queryList.toArray(new String[0]))
                    )).orElse(Lists.newArrayList()).stream()
                      .filter(Objects::nonNull)
                      .collect(Collectors.toList());
        //设置Hash结果之后，queryList若不为空，剩下的是本地缓存还有redis都不命中
        setHashResult(typeReference, priFunction, resultList, valueList, queryList);
        fillLocalCache(key, typeReference, priFunction, valueList);

        if(CollectionUtils.isEmpty(queryList)){
            //缓存全部命中，直接返回
            return resultList;
        }

        //缓存没有，查询指定存储，
        List<V> sourceList = function.apply(queryList);
        //填补redis缓存,redis没有命中，则本地缓存也一定没有命中，在填补redis的时候填补本地缓存
        fillHashCache(key, priFunction, timeoutSeconds, sourceList);
        if(CollectionUtils.isNotEmpty(sourceList)){
            resultList.addAll(sourceList);
        }

        return resultList;
    }

    /**
     *
     * @param key
     * @param priFunction
     * @param timeoutSeconds
     * @param sourceList
     * @param <T>
     * @param <V>
     */
    private <T, V> void fillHashCache(String key, Function<V, T> priFunction, int timeoutSeconds,List<V> sourceList) {
        fillLocalCache2(key,priFunction,sourceList);
        fillRedisHashCache(key,priFunction,timeoutSeconds,sourceList);
    }

    /**
     *
     * @param key
     * @param priFunction
     * @param timeoutSeconds
     * @param sourceList
     * @param <T>
     * @param <V>
     */
    private <T, V> void fillRedisHashCache(String key, Function<V, T> priFunction, int timeoutSeconds,List<V> sourceList) {
        if(CollectionUtils.isNotEmpty(sourceList)){
            Map<String,String> fieldMap = sourceList.stream().collect(
                    Collectors.toMap(
                            item -> String.valueOf(priFunction.apply(item)),
                            item -> JSON.toJSONString(Function.identity())
                    )
            );
            this.tryCatch(() -> {
                this.jedisCluster.hmset(key,fieldMap);
                this.jedisCluster.expire(key, timeoutSeconds);
            });
        }
    }

    /**
     * 填补本地缓存
     * @param key
     * @param typeReference
     * @param priFunction
     * @param valueList
     * @param <T>
     * @param <V>
     */
    private <T, V> void fillLocalCache(String key, TypeReference<V> typeReference, Function<V, T> priFunction, List<String> valueList) {
        if(CollectionUtils.isNotEmpty(valueList)){
            //填补本地缓存空白
            List<V> cacheList = getVList(typeReference, valueList);
            fillLocalCache2(key,priFunction,cacheList);
        }
    }

    /**
     * 填补本地缓存
     * @param key
     * @param priFunction
     * @param list
     * @param <T>
     * @param <V>
     */
    private <T, V> void fillLocalCache2(String key,Function<V, T> priFunction, List<V> list) {
        if(CollectionUtils.isNotEmpty(list)){
            //填补本地缓存空白
            cache.putAll(
                    list.stream()
                            .filter(Objects::nonNull)
                            .collect(
                                    Collectors.toMap(
                                            item -> new StringBuilder().append(key).append(CONCAT_STR).append(priFunction.apply(item)).toString(),
                                            item -> JSON.toJSONString(item)
                                    )
                            )
            );
        }
    }

    private <T, V> void setHashResult(TypeReference<V> typeReference, Function<V, T> priFunction, List<V> resultList, List<String> valueList, List<String> queryList) {
        if (CollectionUtils.isNotEmpty(valueList)) {
            //解析对象
            List<V> cacheList = getVList(typeReference, valueList);

            if (CollectionUtils.isNotEmpty(cacheList)) {
                queryList.removeAll(
                        cacheList.stream()
                                .map(item -> String.valueOf(priFunction.apply(item)))
                                .collect(Collectors.toList())
                );
                resultList.addAll(cacheList);
            }
        }
    }

    /**
     * 解析字符换列表到对象列表
     * @param typeReference
     * @param valueList
     * @param <V>
     * @return
     */
    private <V> List<V> getVList(TypeReference<V> typeReference, List<String> valueList) {
        if(CollectionUtils.isEmpty(valueList)){
            return Lists.newArrayList();
        }
        List<V> cacheList = valueList.stream()
                                .filter(Objects::nonNull)
                                .map(item -> JSON.parseObject(item, typeReference))
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
        return cacheList;
    }


    /**
     * 捕获异常
     * @param runnable
     */
    private void tryCatch(Runnable runnable){
        try{
            runnable.run();
        }catch (Exception e){
            log.error("catch a exception.",e);
        }
    }

    /**
     * 捕获异常
     * @param supplier
     * @param <T>
     * @return
     */
    private <T> T tryCatch(Supplier<T> supplier){
        try{
            return supplier.get();
        }catch (Exception e){
            log.error("catch a exception.",e);
            return null;
        }
    }
}