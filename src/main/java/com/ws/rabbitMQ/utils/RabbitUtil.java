package com.ws.rabbitMQ.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class RabbitUtil {
    public static Channel getChannel() throws Exception {
        // 引入配置文件
        Properties properties = new Properties();
        InputStream inputStream = new FileInputStream(new File("src/main/resources/rabbit-user.properties"));
        properties.load(inputStream);
        String host = properties.getProperty("rabbit.host");
        String username = properties.getProperty("rabbit.username");
        String password = properties.getProperty("rabbit.password");

        // 连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);

        // 建立连接和信道
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        return channel;
    }
}