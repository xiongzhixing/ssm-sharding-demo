package com.soecode.lyf.redis;

import com.soecode.lyf.BaseTest;
import com.soecode.lyf.lock.DistributedLock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * DistributedLock Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>三月 29, 2020</pre>
 */
public class DistributedLockTest extends BaseTest{
    private static ThreadPoolExecutor threadPoolExecutor;

    static {
        threadPoolExecutor = new ThreadPoolExecutor(
                3,6,300, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(1000),new ThreadPoolExecutor.DiscardPolicy());
    }

    @Autowired
    private DistributedLock distributedLock;

    @Test
    public void testExecuteForKeySupplierTimeoutSeconds() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        threadPoolExecutor.execute(() -> {
            try {
                countDownLatch.await();
                this.distributedLock.execute("lock",() -> {
                    System.out.println(Thread.currentThread().getName()+ "拿到了锁");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                },5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        threadPoolExecutor.execute(() -> {
            try {
                countDownLatch.await();
                this.distributedLock.execute("lock",() -> {
                    System.out.println(Thread.currentThread().getName()+ "拿到了锁");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                },5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        threadPoolExecutor.execute(() -> {
            try {
                countDownLatch.await();
                this.distributedLock.execute("lock",() -> {
                    System.out.println(Thread.currentThread().getName()+ "拿到了锁");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                },5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        countDownLatch.countDown();
        while(true){}
    }

    @Test
    public void testExecuteForKeyListSupplierTimeoutSeconds() throws Exception {
    }

}
