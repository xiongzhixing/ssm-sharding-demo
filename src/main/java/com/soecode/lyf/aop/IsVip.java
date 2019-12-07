package com.soecode.lyf.aop;

import java.lang.annotation.*;

/**
 * @author Administrator
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IsVip {}

