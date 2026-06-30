package com.bridgelabz.fundoo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafka
@org.springframework.context.annotation.Profile("!test")
public class KafkaConfig {

    @Bean
    public NewTopic userEventsTopic() {
        return TopicBuilder.name("user-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic reminderAlertsTopic() {
        return TopicBuilder.name("reminder-alerts")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic auditLogsTopic() {
        return TopicBuilder.name("audit-logs")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
