package com.soecode.lyf.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Foo {
    private ReentrantLock lock;
    private Condition oneComplete;
    private Condition twoComplete;

    private volatile boolean oneMethod = false;
    private volatile boolean twoMethod = false;

    public Foo() {
        lock = new ReentrantLock();
        oneComplete = lock.newCondition();
        twoComplete = lock.newCondition();
    }

    public void one() {
        lock.lock();
        try {
            System.out.println("one");
            oneMethod = true;
            oneComplete.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void two() {
        lock.lock();
        try {
            if (!oneMethod) {
                oneComplete.await();
            }
            System.out.println("two");
            twoMethod = true;
            twoComplete.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void three() {
        lock.lock();
        try {
            if (!twoMethod) {
                twoComplete.await();
            }
            System.out.println("three");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        Foo foo = new Foo();

        Thread thread1 = new Thread(() -> {
            foo.one();
        });

        Thread thread2 = new Thread(() -> {
            foo.two();
        });

        Thread thread3 = new Thread(() -> {
            foo.three();
        });

        thread3.start();
        thread2.start();
        thread1.start();
    }
}