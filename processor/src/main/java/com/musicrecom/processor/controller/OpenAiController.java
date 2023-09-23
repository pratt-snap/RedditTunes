package com.musicrecom.processor.controller;


import com.musicrecom.processor.service.OpenAICallsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OpenAiController {
    @Autowired
    private OpenAICallsService openAICallsService;

    @PostMapping(value="/batchJob")
    public void runExtractSongsJob(@RequestParam("userId") String userId) {
        openAICallsService.runBatchJob(userId);
    }
}
