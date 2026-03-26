package com.example.logs.ai.DevopsTool.controller;

//import com.example.logs.ai.DevopsTool.service.LogConsumer;
//import com.example.logs.ai.DevopsTool.service.LogProducer;
import com.example.logs.ai.DevopsTool.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LogController {

    private final LogService logService;
//    private final LogProducer logProducer;
//    private final LogConsumer logConsumer;

    @PostMapping("/analyze")
    public String analyzeLog(@RequestBody String log) {
        return logService.analyzeLog(log);
    }

//    @PostMapping("/publish")
//    public String publish(@RequestBody String log) {
//        logProducer.sendToProducer(log);
//        String content = logService.analyzeLog(log);
////        TODO: Message in this consumer we are getting in loop, fix that
////        logProducer.sendToConsumer(content);
//        return "Log sent to Kafka";
//    }
}
