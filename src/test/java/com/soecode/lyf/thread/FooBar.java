package com.soecode.lyf.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class FooBar {
    private int n;
    private final ReentrantLock lock;
    private final Condition fooCondition;
    private final Condition barCondition;

    private volatile boolean isExecuteFoo = false;
    private volatile boolean isExecuteBar = true;

    public FooBar(int n) {
        this.n = n;
        lock = new ReentrantLock();
        fooCondition = lock.newCondition();
        barCondition = lock.newCondition();
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            lock.lock();
            try {
                if (!isExecuteBar) {
                    fooCondition.await();
                }
                printFoo.run();
                isExecuteFoo = true;
                isExecuteBar = false;
                barCondition.signal();
                fooCondition.await();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            lock.lock();
            try{
                if(!isExecuteFoo){
                    barCondition.await();
                }
                printBar.run();
                isExecuteBar = true;
                isExecuteFoo = false;
                fooCondition.signal();
                barCondition.await();
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        FooBar fooBar = new FooBar(10);

        Thread thread1 = new Thread(() -> {
            try {
                fooBar.foo(() -> System.out.print("foo"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                fooBar.bar(() -> System.out.print("bar"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread2.start();
        thread1.start();

    }
}
