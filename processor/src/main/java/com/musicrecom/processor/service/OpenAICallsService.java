package com.musicrecom.processor.service;

import com.musicrecom.processor.dto.SubredditDTO;
import com.musicrecom.processor.entity.*;
import com.musicrecom.processor.repository.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.*;


@Service
public class OpenAICallsService {

    private static final Logger logger = LoggerFactory.getLogger(OpenAICallsService.class);

    @Autowired
    private SpotifyCallsService spotifyCallsService;

    @Autowired
    private SongsSubredditRepository songsSubredditRepository;

    @Autowired
    private SubredditRepository subredditRepository;

    @Autowired
    private RedditorSubredditService redditorSubredditService;

    @Autowired
    private RedditorSubredditRepository redditorSubredditRepository;

    @Autowired
    private SongsSubredditService songsSubredditService;



    public void runBatchJob(String userId) {
           logger.info("batch job triggered");
           Boolean AreSubredditsExtracted=redditorSubredditService.isRedditorProcessedForSubreddits(userId);

           List<SubredditDTO> subRedditList=new ArrayList<>();
           if(!AreSubredditsExtracted){
                //extract
                logger.info("subreddit extraction triggered");
                subRedditList= extractSaveSubreddits(userId);
               logger.info("subreddits extracted");
                fetchSubredditInfo(subRedditList);
                //save subs and user
                SaveSubsUser(subRedditList,userId);
            }
            else{
                try{
                    List<RedditorSubreddit> redsubs=redditorSubredditRepository.findByRedditorId(userId);
                    for(RedditorSubreddit redsub:redsubs){
                        Optional<Subreddit> optsub=subredditRepository.findById(redsub.getId().getSubredditId());
                        Subreddit sub=optsub.get();
                        SubredditDTO subDTO=new SubredditDTO();
                        subDTO.setDescription(sub.getDescription());
                        subDTO.setName(sub.getName());
                        subDTO.setId(sub.getId());
                        subRedditList.add(subDTO);
                    }
                }
                catch (Exception e) {
                    logger.error("An error occurred while fetching subreddits for user {} from database", userId);
                }
            }
            for(SubredditDTO subreddit:subRedditList){
                logger.info("subreddit processing for {}" +  subreddit.getId());
                Boolean isSubProcessed=songsSubredditService.isSubredditProcessed(subreddit.getId());
                List<Song> subSongs=new ArrayList<>();
                logger.info("Is Subreddit Processed {}", isSubProcessed);
                if(!isSubProcessed){
                    logger.info("fetching songs for subreddit");
                    getSongs(subreddit,userId);
                }
                else{
                    List<SongsSubreddit> songsubentries=songsSubredditRepository.findBySubredditId(subreddit.getId());
                    List<String> savedSongs= new ArrayList<>();
                    for(SongsSubreddit entry:songsubentries){
                        savedSongs.add(entry.getId().getSongId());
                    }
                    spotifyCallsService.saveSongsUser(userId,savedSongs);
                }
            }
    }

    public List<SubredditDTO> extractSaveSubreddits(String userId){
        List<SubredditDTO> subreddits = new ArrayList<>();
        List<String> command = new ArrayList<>();
        String Python = System.getenv("Python");

        command.add(Python);
        command.add("src/main/resources/scripts/ui.py");
        command.add(userId);

        try {

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read the output from the Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info("line- {}", line);
                if(line.contains("subreddit id -"))
                {
                    String[] subredditData = line.split("-");
                    if (subredditData.length == 2) {
                        String subredditId = subredditData[1];
                        SubredditDTO subreddit = new SubredditDTO();
                        subreddit.setId(subredditId);
                        subreddits.add(subreddit);
                    }
                }
            }
            int exitCode = process.waitFor();


            if (exitCode != 0) {
                System.err.println("An error occurred while executing the Python script. Exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return subreddits;
    }


    private void fetchSubredditInfo(List<SubredditDTO> subRedditList) {
        for (SubredditDTO subReddit : subRedditList) {
            try {
                logger.info("Fetching subreddit info");
                Optional<Subreddit> subopt = subredditRepository.findById(subReddit.getId());
                Subreddit sub=subopt.get();
                subReddit.setName(sub.getName());
                subReddit.setDescription(sub.getDescription());
            } catch (Exception e) {
                logger.error("An error occurred while fetching subreddit info for subreddit ID {}: {}", subReddit.getId(), e.getMessage());
            }
        }
    }


    private void SaveSubsUser(List<SubredditDTO> subsExtracted, String userId) {
        try {
            logger.info("Saving subreddits and users");
            for (SubredditDTO sub : subsExtracted) {
                RedditorSubredditId id = new RedditorSubredditId(userId, sub.getId());
                RedditorSubreddit redSub = new RedditorSubreddit(id);
                redditorSubredditRepository.save(redSub);
            }
            logger.info("Subreddits and users saved successfully");
        } catch (Exception e) {
            logger.error("An error occurred while saving subreddits and users: {}", e.getMessage());
        }
    }



    public String getDescriptionFromSubreddit(SubredditDTO subreddit){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String bearerToken = System.getenv("BEARER_TOKEN");
        headers.set("Authorization", "Bearer "+bearerToken);
        List<JSONObject> messages = new ArrayList<>();
        String systemPrompt="I am giving name and description of reddit community. Return in format, country name, emotion, target audience . \n" +
                "All attributes in one or two words at max. Example of resonse format \n" +
                "country: Country name or N/A\n" +
                "emotion: emotion name\n" +
                "target_audience: possible target audience";
        StringBuilder userPromptBuilder=new StringBuilder("Subreddit Name: "+subreddit.getName()+" description "+ subreddit.getDescription());
        String userPrompt=userPromptBuilder.toString();
        messages.add(createMessage("system", systemPrompt));
        messages.add(createMessage("user", userPrompt));
        String requestBody = "{\"messages\": " + messages.toString() + ", \"max_tokens\": 300, \"model\": \"gpt-3.5-turbo\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        String url = "https://api.openai.com/v1/chat/completions";

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        int statusCode = responseEntity.getStatusCodeValue();
        String responseBody = responseEntity.getBody();
        JSONObject responseJson = new JSONObject(responseBody);
        logger.info("Response status code: {}" , statusCode);
        JSONArray choicesArray = responseJson.getJSONArray("choices");
        String result="";
        if (choicesArray.length() > 0) {
            JSONObject firstChoice = choicesArray.getJSONObject(0);
            JSONObject message = firstChoice.getJSONObject("message");
            String content = message.getString("content");

            result=processContent(content);
        } else {
            logger.info("No choices found in the response.");
        }
        return result;
    }

    public void getSongs(SubredditDTO subreddit, String userId){
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String bearerToken = System.getenv("BEARER_TOKEN");
            headers.set("Authorization", "Bearer "+bearerToken);
            List<JSONObject> messages = new ArrayList<>();
            String userPrompt = getDescriptionFromSubreddit(subreddit);
            System.out.println(userPrompt);
            Boolean languageNotEnglish = checkLanguage(userPrompt);
            Integer n = 5;
            n = languageNotEnglish ? 10 : 5;
            String systemPrompt = "You are a music recommendation system who returns just" + n + "song and corresponding artist name each on new line as response for people of described traits without courtesy message.";
            messages.add(createMessage("system", systemPrompt));
            messages.add(createMessage("user", userPrompt));
            String requestBody = "{\"messages\": " + messages.toString() + ", \"max_tokens\": 300, \"model\": \"gpt-3.5-turbo\"}";
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            String url = "https://api.openai.com/v1/chat/completions";

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            int statusCode = responseEntity.getStatusCodeValue();
            String responseBody = responseEntity.getBody();
            JSONObject responseJson = new JSONObject(responseBody);

            logger.info("Response status code: {}" , statusCode);
            JSONArray choicesArray = responseJson.getJSONArray("choices");
            String content = "";

            if (choicesArray.length() > 0) {
                JSONObject firstChoice = choicesArray.getJSONObject(0);
                JSONObject message = firstChoice.getJSONObject("message");
                content = message.getString("content");
                spotifyCallsService.CreateSongsArray(content,subreddit.getId(),userId);
            } else {
                logger.info("No choices found in the response.");
            }
        }
        catch (HttpClientErrorException e) {
            logger.error("HTTP error occurred:  {}", e.getMessage());
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
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
