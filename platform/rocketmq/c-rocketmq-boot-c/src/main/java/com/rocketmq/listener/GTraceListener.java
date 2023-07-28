package com.rocketmq.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 轨迹消息
 */
@Component
@Slf4j
@RocketMQMessageListener(topic = "traceTopic",
        consumerGroup = "trace-consumer-group",
        consumeThreadNumber = 40,
        consumeMode = ConsumeMode.CONCURRENTLY,
        enableMsgTrace = true // 开启消费者方的轨迹
)
public class GTraceListener implements RocketMQListener<String> {


    @Override
    public void onMessage(String message) {
        log.info("我是消费者trace： {}", message);
//        System.out.println("我是消费者:" + message);
    }
}
