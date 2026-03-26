//package com.example.logs.ai.DevopsTool.config;
//
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.TopicBuilder;
//
//@Configuration
//public class KafkaTopicCreationConfig {
//
//    @Bean
//    public NewTopic errorLogsTopic() {
//        return TopicBuilder.name("error-logs-topic")
//                .partitions(3)
//                .replicas(1) // Set this to 1 for local dev, or more for production
//                .build();
//    }
//
//}
