package com.soecode.lyf.listener;

import org.springframework.stereotype.Component;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/4/4 0004
 **/
@Component
public class TestMessageListener extends AbstractMessageListener {
    @Override
    public void internalProcess(String msg) {
        System.out.println(msg);
    }
}