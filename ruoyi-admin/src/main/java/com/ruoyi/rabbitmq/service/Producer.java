package com.ruoyi.rabbitmq.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.ruoyi.rabbitmq.annotation.RabbitMqAnnotation;

import java.util.HashMap;
import java.util.Map;

public class Producer {
    private static final String QUEUE_NAME = "whm_queue2";

    public static void main(String[] args) {
        try (Connection connection = RabbitMqAnnotation.getConnection();
             Channel channel = RabbitMqAnnotation.createChannel(connection)) {
            //创建死信队列
            RabbitMqAnnotation.createDlxChannel("whm_dlx_exchange","whm_dlx_queue","whm_dlx_routing_key");
            //配置死信队列的参数，于原队列相关联
            // 在源队列中设置消息过期时间
            Map<String, Object> args2 = new HashMap<>();
            args2.put("x-message-ttl", 6000);  // 设置消息过期时间为 60 秒
            args2.put("x-dead-letter-exchange", "whm_dlx_exchange");
            args2.put("x-dead-letter-routing-key", "whm_dlx_routing_key");
            // 声明一个队列（如果没有则创建）
            channel.queueDeclare(QUEUE_NAME, false, false, false, args2);
            String message = "Hello, RabbitMQ!";
            // 发送消息到指定队列
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

            System.out.println(" [x] Sent '" + message + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void creatManege(String message){
        try (Connection connection = RabbitMqAnnotation.getConnection();
             Channel channel = RabbitMqAnnotation.createChannel(connection)) {
            // 发送消息到指定队列
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
