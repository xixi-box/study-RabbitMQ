package com.ws.rabbitMQ.WorkQueues_TaskQueues.helloworld;

import com.rabbitmq.client.Channel;
import com.ws.rabbitMQ.utils.RabbitUtil;

import java.util.Scanner;

public class NewTask {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        // 建立连接和管道
        try (Channel channel = RabbitUtil.getChannel()) {
            // 参数一：声明我们要发送的队列是谁（QUEUE_NAME），其他参数这里先不用关注
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            // 发送消息
            // 如果你是用shell测试的，用这条语句，用于放入参数：
            // String message = String.join(" ", argv); // ***主要改了这里***
            // 如果你是在idea里用控制台测试的，用这条语句：
            String message = new Scanner(System.in).nextLine();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] 发送消息 '" + message + "'");
        }
    }
}