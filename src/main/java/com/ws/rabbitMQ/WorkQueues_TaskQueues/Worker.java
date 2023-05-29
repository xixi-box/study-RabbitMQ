package com.ws.rabbitMQ.WorkQueues_TaskQueues;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ws.rabbitMQ.utils.RabbitUtil;

@SuppressWarnings("all")
public class Worker {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        // 建立连接和管道
        Channel channel = RabbitUtil.getChannel();
        // 声明从哪个队列接受消息
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] 等待消息. To exit press CTRL+C");

        // 接收到信息回调接口，目的是当接收到一条信息时，进行一些操作，比如可以在控制台里打印出来，以告诉程序员收到了信息。
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] 已收到 '" + message + "'");
            // ***主要改了这里***
            try {
                doWork(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(" [x] 工作任务完成！");
            }
        };

        // 取消接收的回调接口，目的是如在接收消息的时候队列被删除掉了，可以进行一些操作，例如告诉程序员接收被中断了。
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断");
        };

        // 这个参数后面会说, 详见2.3

        boolean autoAck = false;

        // 管道接收消息
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }

    // 用于模拟工作任务，输入一个点就停顿一秒
    private static void doWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }
}