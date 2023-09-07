package com.ws.rabbitMQ.topics;

import com.rabbitmq.client.Channel;
import com.ws.rabbitMQ.utils.RabbitUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class EmitLogTopic {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        // 建立连接
        Channel channel = RabbitUtil.getChannel();

        // 声明topic交换模式的交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        Map<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.range.rabbit", "被队列Q1Q2接收到");
        bindingKeyMap.put("lazy.orange.elephant", "被队列Q1Q2接收到");
        bindingKeyMap.put("quick.orange.fox", "被队列Q1接收到");
        bindingKeyMap.put("lazy.brown.fox", "虽然满足两个绑定但只被队列Q2接收一次");
        bindingKeyMap.put("lazy.pink.rabbit", "虽然满足两个绑定但只被队列 Q2 接收一次");
        bindingKeyMap.put("quick.brown.fox", "不匹配任何绑定不会被任何队列接收到会被丢弃");
        bindingKeyMap.put("quick.orange.male.rabbit", "是四个单词不匹配任何绑定会被丢弃");
        bindingKeyMap.put("lazy.orange.male.rabbit", "是四个单词但匹配 Q2");

        for (Map.Entry<String, String> next : bindingKeyMap.entrySet()) {
            String bindingKey = next.getKey();
            String message = next.getValue();

            channel.basicPublish(EXCHANGE_NAME, bindingKey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息： " + bindingKey + "---> " + message);
        }

        channel.close();
        channel.getConnection().close();
    }
}