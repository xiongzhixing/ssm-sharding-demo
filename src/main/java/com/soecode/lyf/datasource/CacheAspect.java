package com.soecode.lyf.datasource;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class CacheAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheAspect.class);

    Cache<String,String> cacheGuava = CacheBuilder.newBuilder()
            .expireAfterWrite(35, TimeUnit.MINUTES)
            .maximumSize(1024)
            .build();

    @Pointcut("execution(* com.soecode.lyf.manager.*.*(..))")
    private void pointCut(){}

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint  jpt) throws Throwable {
        com.soecode.lyf.annotation.Cache cache = ((MethodSignature)jpt.getSignature()).getMethod().getAnnotation(com.soecode.lyf.annotation.Cache.class);
        if(cache == null){
            return jpt.proceed();
        }
        Class resultType = ((MethodSignature)jpt.getSignature()).getMethod().getReturnType();
        String classPath = jpt.getTarget().getClass().getName();
        String methodName = jpt.getSignature().getName();
        String[] paramNames = ((MethodSignature)jpt.getSignature()).getParameterNames();
        Object[] paramValues = jpt.getArgs();

        StringBuilder key = new StringBuilder("");
        key.append(classPath).append("_").append(methodName).append("_");
        if(paramNames != null && paramNames.length > 0){
            for(int i=0;i < paramNames.length;i++){
                key.append(paramNames[i]).append("_").append(paramValues[i]).append("_");
            }
        }
        String json = cacheGuava.asMap().get(key.toString().substring(0,key.length() - 1));
        if(!StringUtils.isEmpty(json)){
            return JSON.parseObject(json,resultType);
        }
        Object object = jpt.proceed();
        if(object != null){
            cacheGuava.put(key.toString().substring(0,key.length() - 1),JSON.toJSONString(object));
        }
        return object;
    }
}
