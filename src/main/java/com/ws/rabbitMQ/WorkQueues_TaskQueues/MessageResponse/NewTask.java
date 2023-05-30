package com.ws.rabbitMQ.WorkQueues_TaskQueues.MessageResponse;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.ws.rabbitMQ.utils.RabbitUtil;

import java.util.Scanner;

public class NewTask {
    private final static String QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        // 建立连接和管道
        Channel channel = RabbitUtil.getChannel();
        // 参数一：声明我们要发送的队列是谁（QUEUE_NAME），其他参数这里先不用关注
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        // 发送消息
        String message = new Scanner(System.in).nextLine();
        //String message = String.join(" ", argv); // ***主要改了这里***
        //MessageProperties.PERSISTENT_TEXT_PLAIN消息持久化
        channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        System.out.println(" [x] 发送消息 '" + message + "'");
    }
}