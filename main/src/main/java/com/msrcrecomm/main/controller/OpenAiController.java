package com.msrcrecomm.main.controller;


import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class OpenAiController {

    @GetMapping(value="/testCall")
    public void simpleOpenAiCall(){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer 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");
        List<JSONObject> messages = new ArrayList<>();
        messages.add(createMessage("system", "You are a music recommendation system who returns just song names as response without courtesy message"));
        messages.add(createMessage("user", "recommend me 20 upbeat bollywood songs"));


        String requestBody = "{\"messages\": " + messages.toString() + ", \"max_tokens\": 300, \"model\": \"gpt-3.5-turbo\"}";
        //String requestBody = "{\"prompt\": \"Hello, world!\", \"max_tokens\": 5, \"model\": \"gpt-3.5-turbo\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        String url = "https://api.openai.com/v1/chat/completions";

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        int statusCode = responseEntity.getStatusCodeValue();
        String responseBody = responseEntity.getBody();
        JSONObject responseJson = new JSONObject(responseBody);

        System.out.println("Response status code: " + statusCode);
        JSONArray choicesArray = responseJson.getJSONArray("choices");
        if (choicesArray.length() > 0) {
            JSONObject firstChoice = choicesArray.getJSONObject(0);
            JSONObject message = firstChoice.getJSONObject("message");
            String content = message.getString("content");

            String[] result=processContent(content);

            System.out.println("Response content: " + content);
        } else {
            System.out.println("No choices found in the response.");
        }

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
