//package com.example.logs.ai.DevopsTool.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class LogConsumer {
//
//    private final LogService logService;
//    private final LogProducer logProducer;
//
//    @KafkaListener(topics = "logs-response-topic", groupId = "response-group", autoStartup = "false")
//
//    public void consume(String log) {
//        String result = logService.analyzeLog(log);
//        logProducer.sendToConsumer(result);
//        System.out.println("Consumed once: " + log);
//    }
//}
