package com.rocketmq.listener;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * tag过滤
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "bootTagTopic",
        consumerGroup = "boot-tag-consumer-group",
        selectorType = SelectorType.TAG,// tag过滤模式
        selectorExpression = "tagA || tagB"
//        selectorType = SelectorType.SQL92,// sql92过滤模式
//        selectorExpression = "a in (3,5,7)" // broker.conf中开启enbalePropertyFilter=true
)
public class CTagMsgListener implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt message) {
        String keys = message.getKeys();
        log.info("接收到的key为： " + keys);
        log.info("接收到的消息为： " + new String(message.getBody()));
    }
}
