package com.ws.rabbitMQ.dead.ttl;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ws.rabbitMQ.utils.RabbitUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Consumer01 {
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    private static final String DEAD_EXCHANGE = "dead_exchange";
    private static final String NORMAL_QUEUE = "normal_queue";
    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        // 建立channel
        Channel channel = RabbitUtil.getChannel();
        // 声明死信和普通交换机，类型为direct 在出现异常情况之后，通过死信交换机转发到相应的死信处理队列中
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 声明普通队列
        // 正常队列绑定死信队列信息
        Map<String, Object> params = new HashMap<>();
        // 正常队列设置死信交换机，key是固定值
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 正常队列设置死信routing-key，key是固定值
        params.put("x-dead-letter-routing-key", "lisi");
//        params.put("x-max-length", 6);
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, params);
        //////////////////////////////////////////////////////////////////////////////////////////////
        //死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        //绑定死信交换机和死信队列
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");
        //绑定普通交换机和普通队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");

        System.out.println("等待接收信息。。。");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            if (message.equals("info5")) {
                System.out.println(message + "此消息是被拒绝的");
                //不再放回普通队列
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("Consumer01 接收到信息： " + message);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }

        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("接收失败");
        };

        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, cancelCallback);
    }
}
