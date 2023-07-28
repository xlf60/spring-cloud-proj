package com.rocketmq;

import io.netty.channel.DefaultChannelId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CRocketmqBootCApplication {
    public static void main(String[] args) {

        DefaultChannelId.newInstance();
        SpringApplication.run(CRocketmqBootCApplication.class, args);
    }
}