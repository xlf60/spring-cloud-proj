package com.rocketmq.rocketmq.demo;

import com.rocketmq.rocketmq.constant.MqConstant;
import io.netty.channel.DefaultChannelId;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * @Author: DLJD
 * @Date: 2023/4/21
 */
public class DMsTest {


    @Test
    public void msProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("ms-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);

        DefaultChannelId.newInstance();
        producer.start();
        Message message = new Message("orderMsTopic", "订单号，座位号".getBytes());
        // 给消息设置一个延迟时间
        // messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h"
        message.setDelayTimeLevel(3);
        // 发延迟消息
        producer.send(message);
        System.out.println("发送时间" + new Date());
        producer.shutdown();
    }

    /**
     * 发送时间  Mon Jul 17 16:33:09 GMT+08:00 2023
     * 收到消息  Mon Jul 17 16:33:30 GMT+08:00 2023
     *
     * 第一次收到时间可能有误差，配置跟不上
     * --------------
     * 发送时间  Mon Jul 17 16:35:25 GMT+08:00 2023
     * 收到消息  Mon Jul 17 16:35:35 GMT+08:00 2023
     *
     * @throws Exception
     */
    @Test
    public void msConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ms-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("orderMsTopic", "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.println("收到消息了" + new Date());
                System.out.println(new String(msgs.get(0).getBody()));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        DefaultChannelId.newInstance();
        consumer.start();
        System.in.read();
    }


}
