package com.tyron.provider.service;

import com.tyron.provider.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @Description: 订单消费
 * @Author: tyron
 * @Date: Created in 2021/6/27
 */
@Slf4j
@Service
@RocketMQMessageListener(consumerGroup = "myConsumer", topic = "orderTopic")
public class SmsService implements RocketMQListener<Order> {
    @Override
    public void onMessage(Order order) {
        log.info("新订单{},发短信", order);
    }
}