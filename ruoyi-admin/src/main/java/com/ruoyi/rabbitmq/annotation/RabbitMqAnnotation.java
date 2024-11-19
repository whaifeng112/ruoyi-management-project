package com.ruoyi.rabbitmq.annotation;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMqAnnotation {
    private static final String HOST = "localhost";  // RabbitMQ服务器地址
    private static final int PORT = 5672;           // RabbitMQ端口
    private static final String USERNAME = "test"; // 用户名
    private static final String PASSWORD = "111"; // 密码

    public static Connection getConnection() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(PORT);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        return factory.newConnection();
    }

    public static Channel createChannel(Connection connection) throws Exception {
        return connection.createChannel();
    }
    //创建死信队列将其于原队列相互绑定
    /*
    *        死信交换机和死信队列的配置
            String deadLetterExchange = "dlx_exchange";
            String deadLetterQueue = "dlx_queue";
            String deadLetterRoutingKey = "dlx_routing_key";

    * */

    public static void createDlxChannel(String deadLetterExchange,String deadLetterQueue,String deadLetterRoutingKey){
        try (Connection connection = RabbitMqAnnotation.getConnection();
             Channel channel = RabbitMqAnnotation.createChannel(connection)) {
            // 创建死信交换机
            channel.exchangeDeclare(deadLetterExchange, "direct");
            // 创建死信队列
            channel.queueDeclare(deadLetterQueue, true, false, false, null);
            // 死信队列绑定到死信交换机
            channel.queueBind(deadLetterQueue, deadLetterExchange, deadLetterRoutingKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
