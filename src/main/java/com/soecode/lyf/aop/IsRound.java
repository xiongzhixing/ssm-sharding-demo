package com.soecode.lyf.aop;

import java.lang.annotation.*;

/**
 * @author Administrator
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IsRound {
    /**
     * 精确到小数点几位，默认保留整数
     * @return
     */
    int index() default 0;
}

