package com.soecode.lyf;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/8/9 0009
 **/
public class CompletableFutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CouponVO couponVO = new CouponVO();

        CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() ->{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<String> couponList = new ArrayList<>();
            couponList.add("卡券1");
            couponList.add("卡券2");
            couponList.add("卡券3");

            couponVO.setCouponList(couponList);
            return couponList;
        }).thenApply(couponList -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            couponVO.setCount(CollectionUtils.isEmpty(couponList) ? 0 : couponList.size());
            return null;
        });

        System.out.println(completableFuture.get());
        System.out.println(JSON.toJSONString(couponVO));
    }

    @Data
    @ToString(callSuper = true)
    public static class CouponVO{
        private List<String> couponList;
        private int count;
    }
}