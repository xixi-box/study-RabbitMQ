package com.ws.rabbitMQ.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: 王顺
 * @Date: 2023/5/25 21:08
 * @Version 1.0
 * @Description 生产者 发消息
 */
public class Producer {
    public static final String QUEUE_NAME = "hello";


    public static void main(String[] args) throws IOException, TimeoutException {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂IP 连接队列
        factory.setHost("192.168.200.135");
        factory.setUsername("ws");
        factory.setPassword("123");
        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        //生成一个队列
        //1.队列名称
        //2.队列消息是否持久化 默认存储在内存中
        //3.该队列是否只供一个消费者 默认是false，多个消费者
        //4.是否自动删除，最后一个消费者断开后 true自动删除 false不删除
        //5.其他参数
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
    }
}
