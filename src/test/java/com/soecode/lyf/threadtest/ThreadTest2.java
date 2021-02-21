package com.soecode.lyf.threadtest;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadTest2 {
    public static Map<Integer, String> DIGIT_MOTHED_MAP;
    public static List<Thread> workerList = Lists.newLinkedList();

    static {
        DIGIT_MOTHED_MAP = new HashMap<>(3);

        DIGIT_MOTHED_MAP.put(1, "foo");
        DIGIT_MOTHED_MAP.put(2, "bar");
    }

    public static void main(String[] args) throws NoSuchMethodException {
        AtomicInteger count = new AtomicInteger(9);

        Foo foo = new Foo();

        List<Integer> list = Lists.newArrayList(1, 2);
        Collections.shuffle(list);
        System.out.println(JSON.toJSONString(list));

        for (Integer idx : list) {
            String methodName = DIGIT_MOTHED_MAP.get(idx);

            Method method = Foo.class.getMethod(methodName);

            new Thread(() -> {
                AtomicInteger i = new AtomicInteger(0);
                while(true){
                    try {
                        method.invoke(foo);
                        i.incrementAndGet();
                        if(i.get() >= count.get()){
                            break;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        while (true) {
        }


    }

    public static class Foo {
        private ReentrantLock lock = new ReentrantLock();
        private Condition fooCondition = lock.newCondition();
        private Condition barCondition = lock.newCondition();

        private AtomicBoolean fooDown = new AtomicBoolean(false);
        private AtomicBoolean barDown = new AtomicBoolean(true);

        public void foo() throws InterruptedException {
            lock.lockInterruptibly();
            try {
                while(!barDown.get()){
                    barCondition.await();
                }
                System.out.println("foo");
                fooDown.set(true);
                barDown.set(false);
                fooCondition.signal();
            } finally {
                lock.unlock();
            }
        }

        public void bar() throws InterruptedException {
            lock.lockInterruptibly();
            try {
                while(!fooDown.get()){
                    fooCondition.await();
                }
                System.out.println("bar");
                barDown.set(true);
                fooDown.set(false);
                barCondition.signal();
            } finally {
                lock.unlock();
            }
        }
    }
}