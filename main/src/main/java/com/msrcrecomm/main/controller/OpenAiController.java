package com.msrcrecomm.main.controller;


import com.msrcrecomm.main.services.OpenAICallsService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


@RestController
public class OpenAiController {

    @Autowired
    private OpenAICallsService openAICallsService;

    @GetMapping(value="/batchJob")
    public void runExtractSongsJob() {
       openAICallsService.runBatchJob();
    }

}
