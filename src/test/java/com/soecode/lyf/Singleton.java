package com.soecode.lyf;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Singleton {
    private static volatile Singleton singleton;

    private static AtomicBoolean initaling = new AtomicBoolean(false);
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private Singleton() {
    }

    public static Singleton getSingleton() {
        if (singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }

    public static Singleton getSingleton1(){
        if(initaling.compareAndSet(false,true)){
            try{
                singleton = new Singleton();
            }finally {
                countDownLatch.countDown();
            }
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return singleton;
    }

    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(20,100,100, TimeUnit.MICROSECONDS,new LinkedBlockingQueue<>());
        CountDownLatch countDownLatch = new CountDownLatch(1);

        for(int i = 0;i < 100;i++){
            executorService.execute(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Singleton.getSingleton());
            });
        }

        countDownLatch.countDown();
    }


}
