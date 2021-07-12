package com.soecode.lyf.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.ttl.threadpool.TtlExecutors;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class AsyncUtil {
    private static final long DEFAULT_TIMEOUT_SECONDS = 5;
    private static ExecutorService executorService;

    static List<People> peopleList = Lists.newArrayList(
            People.builder()
                    .id(1)
                    .name("zs")
                    .build(),
            People.builder()
                    .id(2)
                    .name("zs")
                    .build()
    );

    public static void initPool() {
        if (executorService == null) {
            synchronized (AsyncUtil.class) {
                if (executorService == null) {
                    ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("async-util-%d").build();
                    executorService =
                            //解决线程复用，导致TransmittableThreadLocal数据没有清除的问题
                            TtlExecutors.getTtlExecutorService(
                                    new ThreadPoolExecutor(16, 32, 300, TimeUnit.MILLISECONDS,
                                            new LinkedBlockingQueue<>(1000), threadFactory, new ThreadPoolExecutor.DiscardPolicy())
                            );
                    MoreExecutors.addDelayedShutdownHook(executorService, 3000, TimeUnit.MILLISECONDS);
                }
            }
        }
    }

    public static <K, V> Map<K, V> executeByMap(List<K> list, Function<K, V> invoker, Long timeoutSec) {
        initPool();

        Map<K, V> resultMap = new ConcurrentHashMap<>();
        if (CollectionUtils.isEmpty(list)) {
            return resultMap;
        }

        long realityTimeoutSec = getRealityTimeoutSec(timeoutSec);

        List<CompletableFuture<V>> futureList = Lists.newArrayList();
        for (K k : list) {
            CompletableFuture<V> completableFuture = CompletableFuture.supplyAsync(() -> invoker.apply(k), executorService)
                    .whenComplete((item, throwable) -> {
                        if (Objects.nonNull(item)) {
                            resultMap.put(k, item);
                        } else if (throwable != null) {
                            log.error("catch a exception", throwable);
                        }
                    });

            futureList.add(completableFuture);
        }

        withComplete(futureList, realityTimeoutSec);

        return resultMap;
    }

    public static <K, V> List<V> executeByList(List<K> list, Function<K, V> invoker, Long timeoutSec) {
        initPool();

        List<V> resultList = new CopyOnWriteArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return resultList;
        }

        long realityTimeoutSec = getRealityTimeoutSec(timeoutSec);
        List<CompletableFuture<V>> futureList = Lists.newArrayList();
        for (K k : list) {
            CompletableFuture<V> completableFuture = CompletableFuture.supplyAsync(() -> invoker.apply(k))
                    .whenComplete((item, throwable) -> {
                        if (Objects.nonNull(item)) {
                            resultList.add(item);
                        } else if (throwable != null) {
                            log.error("catch a exception", throwable);
                        }
                    });

            futureList.add(completableFuture);
        }

        withComplete(futureList, realityTimeoutSec);
        return resultList;


    }

    private static <V> void withComplete(List<CompletableFuture<V>> futureList, long realityTimeoutSec) {
        try {
            CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).get(realityTimeoutSec, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("catch a exception", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private static long getRealityTimeoutSec(Long timeoutSec) {
        long realityTimeoutSec = (timeoutSec == null || timeoutSec <= 0) ? DEFAULT_TIMEOUT_SECONDS : timeoutSec;
        return realityTimeoutSec;
    }

    public static void main(String[] args) {
        List<Integer> idList = Lists.newArrayList(1, 2, 3);


        System.out.println(
                JSON.toJSONString(
                        AsyncUtil.executeByList(idList,
                                AsyncUtil::queryPeople,
                                3L)
                )
        );
    }

    public static People queryPeople(Integer id) {
        if (id == null) {
            return null;
        }

        Map<Integer, People> peopleMap = peopleList.stream().collect(
                Collectors.toMap(People::getId, Function.identity())
        );
        return peopleMap.get(id);
    }

    @Data
    @ToString(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class People {
        private Integer id;
        private String name;
    }
}
