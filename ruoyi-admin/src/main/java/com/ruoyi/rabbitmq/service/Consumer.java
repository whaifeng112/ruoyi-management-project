package com.ruoyi.rabbitmq.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.ruoyi.rabbitmq.annotation.RabbitMqAnnotation;

public class Consumer {
    private static final String QUEUE_NAME = "whm_dlx_queue";

    private static final Consumer consumer = new Consumer();

    private String manager;

    private Consumer() {

    }
    public static  Consumer getConsumer() {

        return consumer;
    }



    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    //创建队列并消费
    public  void consumpting(String[] args) {
        try (Connection connection = RabbitMqAnnotation.getConnection();
             Channel channel = RabbitMqAnnotation.createChannel(connection)) {

            // 声明队列（确保队列存在）
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            // 创建一个消费者
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            };
            // 开始消费消息
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> setManager(consumerTag));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //消费以有队列
    public  void consumptingno(String[] args) {
        try (Connection connection = RabbitMqAnnotation.getConnection();
             Channel channel = RabbitMqAnnotation.createChannel(connection)) {
            // 创建一个消费者
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("收到消息: '" + message + "'");

                // 在消息被处理完后，手动确认该消息
                long deliveryTag = delivery.getEnvelope().getDeliveryTag();
                channel.basicAck(deliveryTag, false);  // 确认该消息已被处理
                // 在这里可以进行消息的处理
            };

            // 消费消息，第二个参数为 false，表示不自动确认消息
            // 设置一个消费者消费队列中的消息
            channel.basicConsume(QUEUE_NAME, false, deliverCallback, (consumerTag, delivery) -> {
//                System.out.println(consumerTag+"消费结束");
            });
//            channel.basicConsume(QUEUE_NAME, true, (com.rabbitmq.client.Consumer) deliverCallback);

            // 保持应用运行，消费者会一直监听消息
            // 你可以在此做其他处理
            // 例如，调用 Thread.sleep() 或其他方式保持应用运行
            System.out.println("消费者正在等待消息...");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    private final static String QUEUE_NAME = "testQueue";
    private final static String HOST = "localhost";
    private static Channel channel;
    private static Connection connection;

    public static void main(String[] argv) {
        try {
            // 初始化连接和通道
            connect();
            consume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 连接到 RabbitMQ
    private static void connect() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
    }

    // 消费消息
    private static void consume() throws Exception {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("Received message: " + message);

                // 处理消息后手动确认
                long deliveryTag = delivery.getEnvelope().getDeliveryTag();
                channel.basicAck(deliveryTag, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        // 设置消费者（手动确认）
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {});

        // 阻塞线程等待消费消息
        System.out.println("Waiting for messages...");
    }

    // 关闭连接和通道
    private static void close() {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
