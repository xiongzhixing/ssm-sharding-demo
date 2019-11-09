package com.soecode.lyf.http;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.ParameterizedType;

public abstract class HttpBaseRequest<Request,Response,HttpResult> {

    public Response invoker(Request request){
        byte[] bytes = null;
        if(getInvokerEnum() == InvokerEnum.GET){
            bytes = new String("发送get请求").getBytes();
        }else if(getInvokerEnum() == InvokerEnum.POST_PARAM_JSON){
            bytes = new String("发送POST JSON请求").getBytes();
        }else if(getInvokerEnum() == InvokerEnum.POST_PARAM_MAP){
            bytes = new String("发送POST MAP请求").getBytes();
        }else{
            throw new RuntimeException("不支持的请求类型");
        }
        if(bytes == null){
            System.out.println("拿到的数据为null");
        }
        HttpResult httpResult = convertResult(bytes);

        return buildContent(httpResult);
    }

    public abstract InvokerEnum getInvokerEnum();

    public abstract String getUrl(Request request);

    public abstract String getParamMap(Request request);

    public abstract String getParamJson(Request request);

    public HttpResult convertResult(byte[] bytes){
        return JSON.parseObject(bytes,((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[2]);
    }

    public abstract Response buildContent(HttpResult httpResult);

    enum InvokerEnum{
        GET,POST_PARAM_MAP,POST_PARAM_JSON
    }

}
