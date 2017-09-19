package com.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {
    private final static String IN_QUEUE_NAME = "q_product";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel inChannel = connection.createChannel();
        inChannel.queueDeclare(IN_QUEUE_NAME, true, false, false, null);
        inChannel.basicQos(1);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        //mysql
        String memcachedServer = "127.0.0.1";
        int memcachedPortal = 11211;
        String mysql_host = "127.0.0.1:3306";
        String mysql_db = "demo";
        String mysql_user = "root";
        String mysql_pass = "root";

        final IndexBuilder indexBuilder = new IndexBuilder(memcachedServer,memcachedPortal,mysql_host,mysql_db,mysql_user,mysql_pass);
        Consumer consumer = new DefaultConsumer(inChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                    Ad ad = objectMapper.readValue(message, Ad.class);
                    indexBuilder.buildInvertIndex(ad);
                    if(!indexBuilder.buildInvertIndex(ad) || !indexBuilder.buildForwardIndex(ad))
                    {
                        //log
                    }
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        inChannel.basicConsume(IN_QUEUE_NAME, true, consumer);
    }
}
