package com.apnihaveli.artiStick.services;

import com.apnihaveli.artiStick.consumers.ImageBufferConsumer;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.auth.Auth;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class QueueService {

    private final ImageBufferConsumer imageBufferConsumer;

    private final Flickr adminFlickr;

    private final Auth adminFlickrAuth;

    public QueueService(ImageBufferConsumer imageBufferConsumer, Flickr adminFlickr, Auth adminFlickrAuth) {
        this.imageBufferConsumer = imageBufferConsumer;
        this.adminFlickr = adminFlickr;
        this.adminFlickrAuth = adminFlickrAuth;
    }

    public static void addToQueueMultiple(Channel channel, String queueName, ArrayList<String> messages) throws IOException {
        for (String message: messages) {
            addToQueue(channel, queueName, message);
        }
    }

    public static void addToQueue(Channel channel, String queueName, String message) throws IOException {
        channel.queueDeclare(queueName, false, false, false, null);
        channel.basicPublish("", queueName, null, message.getBytes());
    }

    public void addConsumer(Channel channel, String queueName) throws IOException {

        channel.queueDeclare(queueName, false, false, false, null);

        switch (QueueNames.valueOf(queueName)) {
            case DB_TO_FLICKR:
                channel.basicConsume(queueName, true, imgBufferCallback, consumerTag -> {
                });
                break;
        }
    }

    private DeliverCallback imgBufferCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody());
        //TODO: how to this without bypassing the compiler.
        (this).imageBufferConsumer.consume(message, (this).adminFlickr, (this).adminFlickrAuth);

    };
}
