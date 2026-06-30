package com.bridgelabz.fundoo.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public UserEventProducer(
            @org.springframework.beans.factory.annotation.Autowired(required = false) KafkaTemplate<String, String> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(String topic, String key, String message) {
        log.info("Attempting to publish message to topic '{}': key={}, payload={}", topic, key, message);
        try {
            if (kafkaTemplate != null) {
                kafkaTemplate.send(topic, key, message);
                log.info("Message successfully published to Kafka topic '{}'", topic);
            } else {
                log.warn("KafkaTemplate is null. Skipping Kafka publishing (Kafka profile might be inactive).");
            }
        } catch (Exception e) {
            log.error("Failed to publish message to Kafka topic '{}' (Kafka broker might be offline): {}", topic, e.getMessage());
        }
    }
}
