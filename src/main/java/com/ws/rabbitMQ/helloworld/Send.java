package com.ws.rabbitMQ.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Author: 王顺
 * @Date: 2023/5/29 20:42
 * @Version 1.0
 */
public class Send {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        // 工厂模式，建立连接
        ConnectionFactory factory = new ConnectionFactory();
        // 如果你是在主机上测试，只需要这一条
        // factory.setHost("localhost");
        // 如果是在本地访问服务器来测试，需要配置账号密码。
        // 也可以写一个properties文件来读取信息，后面还会集成进Utils里，这里先直接写死测试一下能不能通
        factory.setHost("192.168.200.136");
        factory.setUsername("ws");
        factory.setPassword("123");

        // 建立连接和管道
        // 使用try()语句，connection和channel都实现了java.io.Closeable，所以不用显式地.close()关掉连接
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // 参数一：声明我们要发送的队列是谁（QUEUE_NAME），其他参数这里先不用关注
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //消息内容
            String message = "Hello World!~";

            //发送消息
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] 发送消息 '" + message + "'");
        }
    }
}