package com.ws.rabbitMQ.helloworld;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: 王顺
 * @Date: 2023/5/29 20:46
 * @Version 1.0
 */
@SuppressWarnings("all")
public class Recv {
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        // 如果你是在主机上测试，只需要这一条
        // factory.setHost("localhost");
        // 如果是在本地访问服务器来测试，需要配置账号密码。
        // 也可以写一个properties文件来读取信息，后面还会集成进Utils里，这里先直接写死测试一下能不能通
        factory.setHost("192.168.200.136");
        factory.setUsername("ws");
        factory.setPassword("123");


        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明从哪个队列接受消息
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] 等待消息. To exit press CTRL+C");

        // 接收到信息回调接口，目的是当接收到一条信息时，进行一些操作，比如可以在控制台里打印出来，以告诉程序员收到了信息。
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] 已收到 '" + message + "'");
        };
        // 取消接收的回调接口，目的是如在接收消息的时候队列被删除掉了，可以进行一些操作，例如告诉程序员接收被中断了。
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };


        // 管道接收消息
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
