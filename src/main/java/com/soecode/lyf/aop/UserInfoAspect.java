package com.soecode.lyf.aop;

import com.soecode.lyf.global.RequestContent;
import com.soecode.lyf.global.RequestContentManager;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Aspect
@Component
@Order(1)
public class UserInfoAspect {
    private Logger logger = LoggerFactory.getLogger(UserInfoAspect.class);

    @Before(value = "execution(* com.soecode.lyf.web..*.*(..)) && @annotation(requestMapping)")
    public void doBefore(RequestMapping requestMapping){
        RequestContent requestContent = RequestContentManager.get();
        requestContent.setUserId(100);
        requestContent.setUserName("xzx");
    }

}
