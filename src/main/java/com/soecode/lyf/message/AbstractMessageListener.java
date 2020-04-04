package com.soecode.lyf.message;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @Description:TODO
 * @Author:xzx
 * @date:2020/4/4 0004
 **/
@Slf4j
public abstract class AbstractMessageListener implements MessageListenerConcurrently {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExts, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try{
            MessageExt messageExt = messageExts.get(0);
            String message = new String(messageExt.getBody(),"utf-8");
            log.debug("AbstractMessageListener#consumeMessage consume message={}",message);
            internalProcess(message);
        }catch (Exception e){
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    public abstract void internalProcess(String msg);
}