package com.ws.rabbitMQ.dead.ttl;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.ws.rabbitMQ.utils.RabbitUtil;

import java.nio.charset.StandardCharsets;

public class Producer {
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {
        // 获取连接
        Channel channel = RabbitUtil.getChannel();
        // 建立一个direct模式的交换
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        // 设置消息的TTL时间
//        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();

        // 该消息是用作演示队列的个数限制
        for (int i = 0; i < 11; i++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发送信息" + message);
        }
        channel.close();
        channel.getConnection().close();
    }
}

