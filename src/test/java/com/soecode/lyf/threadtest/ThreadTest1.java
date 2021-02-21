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
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadTest1 {
    public static Map<Integer,String> DIGIT_MOTHED_MAP;
    public static List<Thread> workerList = Lists.newLinkedList();

    static {
        DIGIT_MOTHED_MAP = new HashMap<>(3);

        DIGIT_MOTHED_MAP.put(1,"firstThing");
        DIGIT_MOTHED_MAP.put(2,"secondThing");
        DIGIT_MOTHED_MAP.put(3,"thirdThing");
    }

    public static void main(String[] args) throws NoSuchMethodException {
        Foo foo = new Foo();

        List<Integer> list = Lists.newArrayList(1,2,3);
        Collections.shuffle(list);
        System.out.println(JSON.toJSONString(list));

        for(Integer idx:list){
            String methodName = DIGIT_MOTHED_MAP.get(idx);

            Method method = Foo.class.getMethod(methodName);

            new Thread(() -> {
                try {
                    method.invoke(foo);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        while(true){}


    }

    public static class Foo{
        private ReentrantLock lock = new ReentrantLock();
        private Condition firdtCondition = lock.newCondition();
        private Condition secondCondition = lock.newCondition();

        private AtomicBoolean firstThingDone = new AtomicBoolean(false);
        private AtomicBoolean secondThingDone = new AtomicBoolean(false);

        public void firstThing() throws InterruptedException {
            lock.lockInterruptibly();
            try{

                System.out.println("firstThing");
                firstThingDone.set(true);
                firdtCondition.signal();
            }finally {
                lock.unlock();
            }
        }

        public void secondThing() throws InterruptedException {
            lock.lockInterruptibly();
            try{
                while(!firstThingDone.get()){
                    firdtCondition.await();
                }
                System.out.println("secondThing");
                secondThingDone.set(true);
                secondCondition.signal();
            }finally {
                lock.unlock();
            }
        }

        public void thirdThing() throws InterruptedException {
            lock.lockInterruptibly();
            try{

                while(!secondThingDone.get()){
                    secondCondition.await();
                }
                System.out.println("thirdThing");
            }finally {
                lock.unlock();
            }
        }
    }
}
