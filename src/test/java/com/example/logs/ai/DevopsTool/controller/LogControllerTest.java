package com.example.logs.ai.DevopsTool.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LogControllerTest {

    @Autowired
    LogController logController;

    @Test
    void test1(){
        var output = logController.analyzeLog("ERROR: Connection timeout while connecting to database");
        System.out.print(output);
    }

}