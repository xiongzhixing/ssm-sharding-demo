package com.soecode.lyf.mq;

import com.soecode.lyf.BaseTest;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/4/4 0004
 **/
public class TestProducer extends BaseTest {
    @Resource
    private DefaultMQProducer defaultMQProducer;

    @Test
    public void test() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Message message = new Message("mytopic12456","hello world   mytopic12".getBytes());
        defaultMQProducer.send(message);

        while(true){}
    }
}