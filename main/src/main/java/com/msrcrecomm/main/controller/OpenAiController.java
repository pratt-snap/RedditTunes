package com.msrcrecomm.main.controller;


import com.msrcrecomm.main.services.OpenAICallsService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.SpotifyApi;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class OpenAiController {

    @Autowired
    private OpenAICallsService openAICallsService;

    @GetMapping(value="/testCall")
    public String simpleOpenAiCall(){
        return openAICallsService.getSongs();
    }

    @GetMapping(value = "/callback")
    public ResponseEntity<String> handleCallback(@RequestParam("code") String authorizationCode) {
        // Handle the authorization code here
        // You can store it, process it, or return it to another part of your application
        return ResponseEntity.ok("Authorization code: " + authorizationCode);
    }

    @GetMapping(value = "/accessToken")
    public String getAccessToken(){
        return openAICallsService.getAccessToken("AQBb3XH50I9ElGv4NF6pfJr6OHbF_tycZN16i0w6n8LuHuO2EL7y_Ytt8gq_b05fJD0ix_6AMhPgBpmzeTqUKKinHFlmja_j7JxrAPoKRasWDxBvOyNgVMoUgpTwX_Hcn4U1nJSWHSlNnmr93dVSuge-r06Snw7Uz9IxT_oRPGTzJQ").getAccessToken();
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
