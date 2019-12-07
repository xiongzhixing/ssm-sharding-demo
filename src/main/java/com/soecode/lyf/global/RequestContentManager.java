package com.soecode.lyf.global;

public class RequestContentManager {
    private static ThreadLocal<RequestContent> requestContentThreadLocal = new InheritableThreadLocal<RequestContent>(){
        @Override
        public RequestContent initialValue() {
            return new RequestContent();
        }
    };

    public static RequestContent get(){
        return requestContentThreadLocal.get();
    }

    public static void removeAll(){
        requestContentThreadLocal.get().removeAll();
    }
}
