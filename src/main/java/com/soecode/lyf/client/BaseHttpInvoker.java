package com.soecode.lyf.client;

import com.alibaba.fastjson.JSON;
import com.soecode.lyf.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.Objects;

public abstract class BaseHttpInvoker<Request,Response,HttpResponse> {
    private static final Logger logger = LoggerFactory.getLogger(BaseHttpInvoker.class);

    public Response invoke(Request request){
        try{
            InvokerMethod invokerMethod = getInvokerMethod();

            byte[] result = null;
            if(invokerMethod == InvokerMethod.GET){
                result = HttpClientUtil.doGet(getUrl(request),getHeadMap(request),getParamMap(request));
            }else if(invokerMethod == InvokerMethod.POST_JSON){
                result = HttpClientUtil.doPostJson(getUrl(request),getHeadMap(request),getParamJson(request));
            }else if(invokerMethod == InvokerMethod.POST_MAP){
                result = HttpClientUtil.doPostMap(getUrl(request),getHeadMap(request),getHeadMap(request));
            }else{
                throw new RuntimeException("not support request type.InvokeMethod=" +getInvokerMethod());
            }

            if(Objects.isNull(result)){
                throw new RuntimeException("invalid request result");
            }
            HttpResponse httpResponse = convertResult(result);
            if(!isSuccess(httpResponse)){
                throw new RuntimeException("request fail!");
            }

            return buildResponse(httpResponse);
        }catch (Exception e){
            logger.error("BaseHttpInvoker#invoke catch a exception.",e);
            throw e;
        }
    }

    public abstract String getUrl(Request request);

    /**
     * 获取请求头：设计为非抽象方法,子类可以不用，但是要使用必须重写该方法。
     * @return
     */
    public Map<String,String> getHeadMap(Request request){
        throw new RuntimeException("not support method exception");
    }

    /**
     * 获取JSON请求体：设计为非抽象方法,子类可以不用，但是要使用必须重写该方法。
     * @return
     */
    public String getParamJson(Request request){
        throw new RuntimeException("not support method exception");
    }

    /**
     * 获取Map请求体：设计为非抽象方法,子类可以不用，但是要使用必须重写该方法。
     * @return
     */
    public Map<String,String> getParamMap(Request request){
        throw new RuntimeException("not support method exception");
    }

    public HttpResponse convertResult(byte[] bytes){
        return JSON.parseObject(bytes,((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[2]);
    }

    public abstract Response buildResponse(HttpResponse httpResponse);

    public abstract boolean isSuccess(HttpResponse httpResponse);

    public abstract InvokerMethod getInvokerMethod();

    public enum InvokerMethod{
        GET,POST_JSON,POST_MAP;
    }
}
