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

    @GetMapping(value="/testCall")
    public String simpleOpenAiCall() {
       return openAICallsService.getSongs();
    }


    private JSONObject getKeywords(String subRedditDescription){
        JSONObject jsonObject = new JSONObject();

        return jsonObject;
    }

    private String[] processContent(String content) {
        String[] arr= content.split("\n");
        System.out.println(arr[4]);
        return arr;
    }

    private static JSONObject createMessage(String role, String content) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("role",role);
        jsonObject.put("content",content);
        return jsonObject;
    }
}
