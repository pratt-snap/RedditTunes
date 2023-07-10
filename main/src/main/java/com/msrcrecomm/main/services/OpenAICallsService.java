package com.msrcrecomm.main.services;

import com.msrcrecomm.main.entity.Song;
import com.msrcrecomm.main.entity.SongsSubreddit;
import com.msrcrecomm.main.entity.SongsSubredditId;
import com.msrcrecomm.main.entity.Subreddit;
import com.msrcrecomm.main.repository.SongsSubredditRepository;
import com.msrcrecomm.main.repository.SubredditRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class OpenAICallsService {

    @Autowired
    private SpotifyCallsService spotifyCallsService;

    @Autowired
    private SongsSubredditRepository songsSubredditRepository;

    @Autowired
    private SubredditRepository subredditRepository;

    @Value("${processed.subreddit.ids}")
    private String processedIds;


    public void runBatchJob() {
        //fetch subreddit from database
        List<Subreddit> subRedditList=subredditRepository.findAll();
        Set<String> processedIdSet = readProcessedIdsFromFile();
        for(Subreddit subreddit:subRedditList){
            if(processedIdSet.contains(subreddit.getId())) continue;
            System.out.println("Processing for subreddit name "+ subreddit.getName());
            List<String> savedSongsIds=getSongs();
            saveSubSongs(subreddit,savedSongsIds);
            processedIdSet.add(subreddit.getId());
            writeProcessedIdToFile(subreddit.getId());
            try {
                Thread.sleep(60000); // Sleep for 60 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                // Handle interruption, if needed
            }
        }
    }

    private Set<String> readProcessedIdsFromFile() {
        Set<String> processedIdSet = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(processedIds))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processedIdSet.add(line.trim());
            }
        } catch (IOException e) {
            // Handle any exceptions
            e.printStackTrace();
        }

        return processedIdSet;
    }

    private void writeProcessedIdToFile(String processedId) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(processedIds, true))) {
                writer.write(processedId);
                writer.newLine();
        } catch (IOException e) {
            // Handle any exceptions
            e.printStackTrace();
        }
    }
    private void saveSubSongs(Subreddit subreddit, List<String> savedSongIds) {
        for(String Id: savedSongIds){
            SongsSubredditId songsSubredditId=new SongsSubredditId(subreddit.getId(),Id);
            SongsSubreddit songsSubreddit=new SongsSubreddit();
            songsSubreddit.setId(songsSubredditId);
            songsSubredditRepository.save(songsSubreddit);
        }
    }

    public String getDescriptionFromSubreddit(){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer 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");
        List<JSONObject> messages = new ArrayList<>();
        String systemPrompt="I am giving description of reddit communities. Return in format,language it is in, country name, emotion and target audience. \n" +
                "All attributes in one or two words at max. Example of resonse format \n" +
                "language: Language name\n" +
                "country: Country name or N/A\n" +
                "emotion: emotion name\n" +
                "target_audience: possible target audience";
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

    public List<String> getSongs(){
        List<String> songs = new ArrayList<>();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer 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");
            List<JSONObject> messages = new ArrayList<>();
            String userPrompt = getDescriptionFromSubreddit();
            System.out.println(userPrompt);
            Boolean languageNotEnglish = checkLanguage(userPrompt);
            Integer n = 5;
            n = languageNotEnglish ? 10 : 5;
            String systemPrompt = "You are a music recommendation system who returns just" + n + "song and corresponding artist name each on new line as response for people of described traits without courtesy message.";
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
            String content = "";

            if (choicesArray.length() > 0) {
                JSONObject firstChoice = choicesArray.getJSONObject(0);
                JSONObject message = firstChoice.getJSONObject("message");
                content = message.getString("content");
                songs = spotifyCallsService.CreateSongsArray(content);
            } else {
                System.out.println("No choices found in the response.");
            }
        }
        catch (HttpClientErrorException e) {
            System.out.println("HTTP error occurred: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
        return songs;
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
        return str.toString();
    }

    private static JSONObject createMessage(String role, String content) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("role",role);
        jsonObject.put("content",content);
        return jsonObject;
    }



}
