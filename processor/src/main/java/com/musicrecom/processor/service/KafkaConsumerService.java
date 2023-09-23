package com.musicrecom.processor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;


@Service
public class KafkaConsumerService {

    @Autowired
    private OpenAICallsService openAICallsService;

@KafkaListener(topics = "user_processing", groupId = "user-processor-group")
public void consume(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                    @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                    @Header(KafkaHeaders.RECEIVED_KEY) String RECEIVED_KEY) {
    System.out.println("Received message from topic " + topic + " and partition " + partition + ":");
    System.out.println("RECEIVED_KEY: " + RECEIVED_KEY);
    System.out.println("Value: " + message);

    openAICallsService.runBatchJob(message);
}
}


