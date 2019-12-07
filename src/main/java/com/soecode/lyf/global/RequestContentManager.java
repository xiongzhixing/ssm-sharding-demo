package com.soecode.lyf.global;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @author Administrator
 */
public class RequestContentManager {
    private static ThreadLocal<RequestContent> requestContentThreadLocal = new TransmittableThreadLocal<RequestContent>() {
        @Override
        public RequestContent initialValue() {
            return new RequestContent();
        }
    };

    public static RequestContent get() {
        return requestContentThreadLocal.get();
    }

    public static void remove() {
        requestContentThreadLocal.remove();
    }
}
