package com.ws.rabbitMQ.PublishSubscribe;

import com.rabbitmq.client.Channel;
import com.ws.rabbitMQ.utils.RabbitUtil;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 发送者
 */
public class EmitLog {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitUtil.getChannel();
        // 声明交换名称和方式
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //从控制台接收信息
        String message = new Scanner(System.in).nextLine();

        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));

        System.out.println(" [x] 发送信息 '" + message + "'");
        channel.close();
        channel.getConnection().close();
    }
}