package com.rocketmq.rocketmq.demo;

import com.rocketmq.rocketmq.constant.MqConstant;
import io.netty.channel.DefaultChannelId;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.junit.Test;

/**
 * 单向消息生产者
 */
public class ConewayTest {

    @Test
    public void testOnewayProducer() throws Exception {
        // 创建默认的生产者
        DefaultMQProducer producer = new DefaultMQProducer("oneway-test-group");
        // 设置nameServer地址
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);

        DefaultChannelId.newInstance();
        // 启动实例
        producer.start();
        Message msg = new Message("onewayTopicTest", ("日志xxx").getBytes());
        // 发送单向消息
        producer.sendOneway(msg);

        System.out.println("成功");
        // 关闭实例
        producer.shutdown();
    }

}
