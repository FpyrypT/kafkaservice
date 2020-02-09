package com.ecosystem.kafkaservice.config;

import com.ecosystem.kafkaservice.Greeting;
import com.ecosystem.kafkaservice.MessageListener;
import com.ecosystem.kafkaservice.MessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {

    @Autowired
    private final MessageProducer producer;

    @Autowired
    private final MessageListener listener;

    @Scheduled(cron = "0/30 * * * * *")
    public void run() throws InterruptedException {
        producer.sendMessage("Hello, World!");
        listener.getLatch().await(10, TimeUnit.SECONDS);

        /*
         * Sending message to a topic with 5 partition,
         * each message to a different partition. But as per
         * listener configuration, only the messages from
         * partition 0 and 3 will be consumed.
         */
        for (int i = 0; i < 5; i++) {
            producer.sendMessageToPartion("Hello To Partioned Topic!", i);
        }
        listener.getPartitionLatch().await(10, TimeUnit.SECONDS);

        /*
         * Sending message to 'filtered' topic. As per listener
         * configuration,  all messages with char sequence
         * 'World' will be discarded.
         */
        producer.sendMessageToFiltered("Hello Baeldung!");
        producer.sendMessageToFiltered("Hello World!");
        listener.getPartitionLatch().await(10, TimeUnit.SECONDS);

        /*
         * Sending message to 'greeting' topic. This will send
         * and recieved a java object with the help of
         * greetingKafkaListenerContainerFactory.
         */
        producer.sendGreetingMessage(new Greeting("Greetings", "World!"));
        listener.getGreetingLatch().await(10, TimeUnit.SECONDS);
    }
}
