package com.rocketmq.rocketmq.demo;


import com.rocketmq.rocketmq.constant.MqConstant;
import io.netty.channel.DefaultChannelId;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.util.List;

/**
 * 异步发送消息
 */
public class BASyncTest {

    @Test
    public void asyncProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("async-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);

        DefaultChannelId.newInstance();
        producer.start();
        Message message = new Message("asyncTopic", "我是一个异步消息".getBytes());
        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送成功");
            }

            @Override
            public void onException(Throwable e) {
                System.err.println("发送失败:" + e.getMessage());
            }
        });
        System.out.println("我先执行");
        System.in.read();
    }


    @Test
    public void testAsyncConsumer() throws Exception {
        // 创建默认消费者组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer-group");
        // 设置nameServer地址
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        // 订阅一个主题来消费   *表示没有过滤参数 表示这个主题的任何消息
        consumer.subscribe("TopicTest", "*");
        // 注册一个消费监听 MessageListenerConcurrently是并发消费
        // 默认是20个线程一起消费，可以参看 consumer.setConsumeThreadMax()
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                // 这里执行消费的代码 默认是多线程消费
                System.out.println(Thread.currentThread().getName() + "----" + msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        DefaultChannelId.newInstance();

        consumer.start();
        System.in.read();
    }

}
