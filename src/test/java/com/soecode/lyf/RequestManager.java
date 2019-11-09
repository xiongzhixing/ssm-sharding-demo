package com.soecode.lyf;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RequestManager {
    private static final ThreadLocal<RequestContent> requestContentThreadLocal = new ThreadLocal<RequestContent>(){
        @Override
        protected RequestContent initialValue() {
            return new RequestContent();
        }
    };

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,5,300, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(20));

        for(int i = 0;i < 1;i++){
            threadPoolExecutor.execute(new TaskSet());
        }

        for(int i = 0;i < 1;i++){
            threadPoolExecutor.execute(new TaskGet());
        }


    }

    public static class TaskSet implements Runnable{
        @Override
        public void run() {
            RequestContent requestContent = requestContentThreadLocal.get();
            String deviceName = Thread.currentThread().getName();
            requestContent.setDeviceName(Thread.currentThread().getName());
            System.out.println("set deviceName:" + deviceName);
        }
    }

    public static class TaskGet implements Runnable{
        @Override
        public void run() {
            RequestContent requestContent = requestContentThreadLocal.get();
            System.out.println("get deviceName:" + requestContent.getDeviceName());
        }
    }

    public static class RequestContent extends HashMap<String, Object> {
        private final static String DEVICE_NAME = "deviceName";
        public void setDeviceName(String deviceName){
            this.put(DEVICE_NAME,deviceName);
        }

        public String getDeviceName(){
            return String.valueOf(this.get(DEVICE_NAME));
        }
    }
}
