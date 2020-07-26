package com.soecode.lyf;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/7/25 0025
 **/
public class TestFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return "qwe";
        });

        System.out.println(future.get());
    }
}