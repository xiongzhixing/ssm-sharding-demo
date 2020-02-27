package com.soecode.lyf;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadTest {
    public static ReentrantLock lock = new ReentrantLock();
    public static Condition conditionA = lock.newCondition();
    public static Condition conditionB = lock.newCondition();
    public static Condition conditionC = lock.newCondition();
    public static int index = 0;

    public static void main(String[] args) {
        ThreadA threadA = new ThreadA();
        ThreadB threadB = new ThreadB();
        ThreadC threadC = new ThreadC();

        threadA.start();//（1）
        threadB.start();//（2）
        threadC.start();//（3）
    }

    public static class ThreadA extends Thread {
        @Override
        public void run() {
            try {
                lock.lock();
                System.out.println("A进程输出" + " : " + ++index);
                conditionB.signal();
                conditionA.await();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static class ThreadB extends Thread {
        @Override
        public void run() {
            try {
                lock.lock();
                System.out.println("B进程输出" + " : " + ++index);
                conditionC.signal();
                conditionB.await();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static class ThreadC extends Thread {
        @Override
        public void run() {
            try {
                lock.lock();
                System.out.println("C进程输出" + " : " + ++index);
                conditionA.signal();
                conditionC.await();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
