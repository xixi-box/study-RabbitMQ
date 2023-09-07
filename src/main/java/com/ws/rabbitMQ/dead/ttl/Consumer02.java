package com.ws.rabbitMQ.dead.ttl;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ws.rabbitMQ.utils.RabbitUtil;

import java.nio.charset.StandardCharsets;

public class Consumer02 {
    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        // 建立channel
        Channel channel = RabbitUtil.getChannel();

        System.out.println("等待接收死信队列信息。。。。");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Consumer02 接收到死信队列中的信息： " + message);
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("接收失败");
        };

        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, cancelCallback);
    }
}
