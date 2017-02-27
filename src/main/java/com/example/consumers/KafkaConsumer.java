package com.example.consumers; 

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Collections.singletonList;

public class KafkaConsumer {


    public static void main(String[] args)  {
        new KafkaConsumer().start();
    }

    private org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer;

    private ExecutorService executor;
    private final AtomicBoolean running = new AtomicBoolean();
    private CountDownLatch stopLatch;


    private TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};
    private ObjectMapper mapper = new ObjectMapper();


    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                stopLoop();
            }
        });

        running.set(true);
        executor = Executors.newSingleThreadExecutor();
        executor.submit(this::loop);
        stopLatch = new CountDownLatch(1);
    }

    public void stopLoop() {
        running.set(false);
        try {
            stopLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    private void loop() {
        System.out.println("starting consumer...");
        Properties properties = new Properties();
        properties.putAll(KafkaConfig.consumerDefaults());

        consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(properties);
        consumer.subscribe(singletonList(KafkaConfig.getTopic()));

        while (running.get()) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                try {
                    Map<String,String> recordMap = mapper.readValue(record.value(), typeRef);
                    System.out.println("consuming payload as key/value ... " + recordMap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("closing consumer...");
        consumer.close();
        stopLatch.countDown();
    }
}
