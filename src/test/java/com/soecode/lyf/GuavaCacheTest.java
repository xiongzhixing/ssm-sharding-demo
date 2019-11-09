package com.soecode.lyf;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GuavaCacheTest {
    private final static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 300, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(200));

    public static void main(String[] args) throws Exception {
        LoadingCache<String, String> localCache = CacheBuilder.newBuilder().maximumSize(2)
                //.expireAfterAccess(5,TimeUnit.SECONDS)   //距离上次访问时间超过5s没有被访问被删除
                //.expireAfterWrite(5,TimeUnit.SECONDS)  //写入之后5s会被删除
                .refreshAfterWrite(5, TimeUnit.SECONDS).build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        return "load" + System.currentTimeMillis();
                    }

                    @Override
                    public ListenableFuture<String> reload(final String key, String oVal) {
                        ListenableFutureTask<String> task = ListenableFutureTask.create(() -> {
                            String nVal = oVal;
                            try {
                                Thread.sleep(5000);
                                nVal = "reload" + System.currentTimeMillis();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                return nVal;
                            }
                        });
                        threadPoolExecutor.execute(task);
                        return task;
                    }
                });


        localCache.put("name", "xzx");
        int i = 1;
        while (true) {
            System.out.println("--------------:" + localCache.get("name"));
            Thread.sleep(1 * 1000);
            i++;
        }


    }
}
