package com.soecode.lyf.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;

@Aspect
@Component
@Order(3)
@Slf4j
public class RoundAspect {

    /**
     * 环绕通知：目标方法执行前后分别执行一些代码，发生异常的时候执行另外一些代码
     * @return
     */
    @Around(value = "execution(* com.soecode.lyf.web..*.*(..)) && @annotation(isRound)")
    public Object doAround(ProceedingJoinPoint jp, IsRound isRound) {
        Object result = null;
        try{
            result =  jp.proceed();
            changeRoung(result,isRound.index());
        } catch (Throwable throwable) {
            log.error("RoundAspect#doAround catch a exception.",throwable);
        }
        return result;
    }

    private void changeRoung(Object result,int index){
        if(result == null){
            return;
        }
        /*try{
            PropertyDescriptor[] propertyDescriptors = ReflectUtils.getBeanProperties(result.getClass());
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                Class proCls = propertyDescriptor.getPropertyType();
                if(proCls.getClassLoader() == null){
                    // JAVA 类型
                    if(proCls == BigDecimal.class){

                    }
                    if(proCls.getName().contains("List")){
                        //List 集合
                        Parameter parameter = propertyDescriptor.getWriteMethod().getParameters()[0];
                        Class paramCls = (Class) ((ParameterizedType)parameter.getParameterizedType()).getActualTypeArguments()[0];
                        if(paramCls.getClassLoader() != null){
                            //自定义类型
                            propertyDescriptor.getWriteMethod().invoke(t,Arrays.asList(construct(paramCls)));
                        }else{
                            setBaseType(t, propertyDescriptor, proCls,true);
                        }
                    }else{
                        throw new RuntimeException("未知的java类型：" + proCls.getName());
                    }
                }else{
                    //自定义类型
                    propertyDescriptor.getWriteMethod().invoke(t,changeRoung(proCls));
                }
            }
            return t;
        }catch(Exception e){
            logger.error("ConstructUtil#construct catch a exception.",e);
            return null;
        }*/
    }
}
