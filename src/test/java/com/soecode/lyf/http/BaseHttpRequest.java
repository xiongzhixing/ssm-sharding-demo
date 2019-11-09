package com.soecode.lyf.http;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.ParameterizedType;

/**
 * @author 熊志星
 * @description
 * @date 2019/10/17
 */
public abstract class BaseHttpRequest<Request,Response,HttpResult> {

    public Response invoker(){
        InvokerEnum invokerEnum = getInvoker();

        byte[] resultBytes = null;
        if(invokerEnum == InvokerEnum.GET){
            resultBytes = new String("{\"code\":200,\"data\":{\"id\":1,\"name\":\"张三\"}}").getBytes();
        }else if(invokerEnum == InvokerEnum.POST_PARAN_MAP){
            resultBytes = new String("调用post方法传map").getBytes();
        }else if(invokerEnum == InvokerEnum.POST_PARAM_JSON){
            resultBytes = new String("调用post方法传json").getBytes();
        }else{
            throw new RuntimeException("系统运行时异常");
        }
        if(null == resultBytes){
            throw new RuntimeException("请求返回数据为空");
        }
        HttpResult httpResult = convertResult(resultBytes);

        if(httpResult == null || !isSuccess(httpResult)){
            throw new RuntimeException("网络请求失败，转化的数据为空");
        }

        return builderResponse(httpResult);
    }

    public abstract String getUrl(Request request);

    public abstract String getHeader(Request request);

    public String getParamJson(Request request){
        throw new RuntimeException("不支持的请求异常");
    }

    public String getParamMap(Request request){
        throw new RuntimeException("不支持的请求异常");
    }

    public abstract boolean isSuccess(HttpResult httpResult);

    public abstract InvokerEnum getInvoker();

    public HttpResult convertResult(byte[] resultBytes){
        return JSON.parseObject(resultBytes,((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[2]);
    }

    public abstract Response builderResponse(HttpResult httpResult);

    enum InvokerEnum{
        GET,POST_PARAN_MAP,POST_PARAM_JSON
    }
}