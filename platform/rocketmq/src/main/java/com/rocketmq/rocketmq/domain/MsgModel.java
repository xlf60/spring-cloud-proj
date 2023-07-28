package com.rocketmq.rocketmq.domain;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class MsgModel {


    private String orderSn;
    private Integer userId;
    private String desc; // 下单 短信 物流

    // xxx


    public MsgModel() {
    }
}
