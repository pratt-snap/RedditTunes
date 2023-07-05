package com.msrcrecomm.main.services;

import com.msrcrecomm.main.entity.Song;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;



@Service
public class OpenAICallsService {

    @Autowired
    private SpotifyCallsService spotifyCallsService;

    public String getDescriptionFromSubreddit(){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer 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");
        List<JSONObject> messages = new ArrayList<>();
        String systemPrompt="I am giving description of reddit communities. Return in format,language it is in, country name, emotion and target audience. \n" +
                "All attributes in one or two words at max. Example of resonse format \n" +
                "language: English\n" +
                "country: N/A\n" +
                "emotion: Humor\n" +
                "target_audience: Anime/Manga fans";
        // will have to get it from database eventually
        String userPrompt="“I Think You Should Leave” on Netflix";
        messages.add(createMessage("system", systemPrompt));
        messages.add(createMessage("user", userPrompt));
        String requestBody = "{\"messages\": " + messages.toString() + ", \"max_tokens\": 300, \"model\": \"gpt-3.5-turbo\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        String url = "https://api.openai.com/v1/chat/completions";

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        int statusCode = responseEntity.getStatusCodeValue();
        String responseBody = responseEntity.getBody();
        JSONObject responseJson = new JSONObject(responseBody);
        System.out.println("Response status code: " + statusCode);
        JSONArray choicesArray = responseJson.getJSONArray("choices");
        String result="";
        if (choicesArray.length() > 0) {
            JSONObject firstChoice = choicesArray.getJSONObject(0);
            JSONObject message = firstChoice.getJSONObject("message");
            String content = message.getString("content");

            result=processContent(content);
        } else {
            System.out.println("No choices found in the response.");
        }
        return result;
    }

    public String getSongs(){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer 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");
        List<JSONObject> messages = new ArrayList<>();
        String userPrompt=getDescriptionFromSubreddit();
        Boolean languageNotEnglish=checkLanguage(userPrompt);
        Integer n=5;
        n=languageNotEnglish?10:5;
        String systemPrompt="You are a music recommendation system who returns just" + n + "song and corresponding artist name as response for people of described traits without courtesy message. example of response \n" +
                "Song - name of artist \n" +
                "Song - name of artist \n";
        messages.add(createMessage("system", systemPrompt));
        messages.add(createMessage("user", userPrompt));


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
        String content="";
        List<Song> songs=new ArrayList<>();
        if (choicesArray.length() > 0) {
            JSONObject firstChoice = choicesArray.getJSONObject(0);
            JSONObject message = firstChoice.getJSONObject("message");
            content = message.getString("content");
            songs=spotifyCallsService.CreateSongsArray(content);
            System.out.println("Response content: " + songs);
        } else {
            System.out.println("No choices found in the response.");
        }
        return content;
    }



    private Boolean checkLanguage(String userPrompt) {
        String[] arr= userPrompt.split("\n");
        for(String line:arr){
            String[] parts=line.split(":");
            if(!parts[0].trim().equals("language")){
                 if(!parts[0].trim().equals("English")) return true;
            }
        }
        return false;
    }


    private String processContent(String content) {
        String[] arr= content.split("\n");
        StringBuilder str=new StringBuilder();
        for(String line:arr){
            String[] parts=line.split(":");
            if(!parts[1].trim().equals("N/A")){
                str.append(line);
                str.append("\n");
            }
        }
        System.out.println(str);
        return str.toString();
    }

    private static JSONObject createMessage(String role, String content) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("role",role);
        jsonObject.put("content",content);
        return jsonObject;
    }


}
