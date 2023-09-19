package com.musicrecom.processor.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    private OpenAICallsService openAICallsService;

    @KafkaListener(topics = "user_processing", groupId = "user-processor-group")
    public void consume(String message) {
        // Process the message received from Kafka
        System.out.println("Received message: " + message);
        openAICallsService.runBatchJob(message);
        // Add your batch processing logic here
    }
}
