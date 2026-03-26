//package com.example.logs.ai.DevopsTool.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class LogProducer {
//
//    private LogConsumer logConsumer;
//    private LogService logService;
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    public void sendToProducer(String log) {
//        kafkaTemplate.send("error-logs-topic", log);
//    }
//
//    public void sendToConsumer(String response){
//        kafkaTemplate.send("logs-response-topic", response);
//    }
//}
