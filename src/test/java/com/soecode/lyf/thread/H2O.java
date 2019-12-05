package com.soecode.lyf.thread;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class H2O {
    private ReentrantLock lock;
    private Condition hCondition;
    private Condition oCondition;

    private AtomicInteger hNum;
    private AtomicInteger oNum;
    public H2O() {
        lock = new ReentrantLock();
        hCondition = lock.newCondition();
        oCondition = lock.newCondition();

        hNum = new AtomicInteger(0);
        oNum = new AtomicInteger(0);
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        lock.lock();
        try{
            releaseHydrogen.run();
            hNum.incrementAndGet();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        releaseOxygen.run();
    }
}
