package com.example.consumers;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KafkaConfig {

    private static final String TOPIC_NAME = "simpleTopic";
    private static final String KAFKA_URL = "kafka://localhost:9092";

    public static String getTopic() {
        return TOPIC_NAME;
    }


    public static Map<String, Object> consumerDefaults() {
        Map<String, Object> properties = defaultKafkaProps();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        //properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        //properties.put("auto.offset.reset", "smallest");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return properties;
    }


    private static Map<String, Object> defaultKafkaProps() {
        Map<String, Object> properties = new HashMap<>();
        List<String> hostPorts = new ArrayList<>();
        try {
            URI uri = new URI(KAFKA_URL);
            hostPorts.add(String.format("%s:%d", uri.getHost(), uri.getPort()));
            properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "PLAINTEXT");
            properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, String.join(",", hostPorts));
        }catch (URISyntaxException e){
            throw new RuntimeException(e);
        }
        return properties;
    }
}
