package com.soecode.lyf.util;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * @Description:本地缓存工具类
 * @Author:xzx
 * @date:2020/3/18 0018
 **/
@Slf4j
public class GuavaUtil {

    private static ThreadPoolExecutor threadPoolExecutor;

    public static void initPool(){
        if(threadPoolExecutor == null){
            synchronized (GuavaUtil.class){
                if(threadPoolExecutor == null){
                    ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("biz-name-%d").build();
                    threadPoolExecutor = new ThreadPoolExecutor(
                            16,32,300, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(1000),threadFactory,new ThreadPoolExecutor.DiscardPolicy());
                    MoreExecutors.addDelayedShutdownHook(
                            //解决线程复用，导致TransmittableThreadLocal数据没有清除的问题
                            TtlExecutors.getTtlExecutorService(threadPoolExecutor)
                            ,3000,TimeUnit.MILLISECONDS);
                }
            }
        }
    }

    /**
     *
     * @param function 传一个抽象的函数
     * @param <K>  函数参数
     * @param <V>  函数返回值
     * @return
     */
    public static <K,V> CacheLoader<K,V> asyncLoadCacheBuilder(Function<K,V> function){
        initPool();
        return new CacheLoader<K, V>() {
            @Override
            public V load(K k) throws Exception {
                V v = function.apply(k);
                log.debug("CacheLoader#load load data.v={}",v);
                return v;
            }

            @Override
            public ListenableFuture<V> reload(K k, V oldV) throws Exception{
                ListenableFutureTask task = ListenableFutureTask.create(new Callable<V>() {
                    @Override
                    public V call() throws Exception {
                        try{
                            V v = function.apply(k);
                            log.debug("CacheLoader#reload newV={}",v);
                            return v;
                        }catch (Exception e){
                            log.error("CacheLoader#reload catch a exception.",e);
                        }
                        log.debug("CacheLoader#reload oldV={}",oldV);
                        return oldV;
                    }
                });
                threadPoolExecutor.execute(task);
                return task;
            }
        };
    }

    public static void main(String[] args) throws ExecutionException {
        Map<Integer,String> dataMap = new HashMap<>();

        dataMap.put(1,"zs");
        dataMap.put(2,"ls");
        dataMap.put(3,"ww");
        dataMap.put(4,"zl");
        dataMap.put(5,"qq");
        dataMap.put(6,"wb");

        LoadingCache loadingCache = CacheBuilder.newBuilder()
                .refreshAfterWrite(3, TimeUnit.SECONDS)
                .expireAfterAccess(12,TimeUnit.HOURS)
                .build(GuavaUtil.asyncLoadCacheBuilder(t -> dataMap.get(t)));

        System.out.println(loadingCache.get(1));
        System.out.println(loadingCache.get(2));
        System.out.println(loadingCache.get(3));
        System.out.println(loadingCache.get(4));
        System.out.println(loadingCache.get(5));
        System.out.println(loadingCache.get(6));
    }
}