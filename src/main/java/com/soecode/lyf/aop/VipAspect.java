package com.soecode.lyf.aop;

import com.soecode.lyf.global.RequestContent;
import com.soecode.lyf.global.RequestContentManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(2)
public class VipAspect {
    private static final Logger logger = LoggerFactory.getLogger(VipAspect.class);
    /**
     * 环绕通知：目标方法执行前后分别执行一些代码，发生异常的时候执行另外一些代码
     *
     * @return
     */
    @Around(value = "execution(* com.soecode.lyf.web..*.*(..)) && @annotation(isVip)")
    public Object doAround(ProceedingJoinPoint jp, IsVip isVip) {
        RequestContent requestContent = RequestContentManager.get();
        logger.info("VipAspect#doAround requestContent={}",requestContent);
        if(requestContent.getUserId() != 100){
            throw new RuntimeException("不是vip");
        }
        Object result = null;
        try {
            result =  jp.proceed();
        } catch (Throwable throwable) {
            logger.error("VipAspect#doAround catch a exception.",throwable);
        }finally {
            RequestContentManager.remove();
        }
        return result;
    }
}
