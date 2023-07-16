package com.msrcrecomm.main.services;

import com.msrcrecomm.main.entity.Subreddit;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class RedditCallsService {

    public void processRedditor(String userId) {
        List<Subreddit> subreddits = new ArrayList<>();
        List<String> command = new ArrayList<>();
        command.add("python");
        command.add("src/main/resources/scripts/ui.py");
        command.add(userId);

        try {
            // Start the process and execute the Python script
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            // Read the output from the Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming the Python script outputs the subreddit information in a specific format
                if(line.contains("subreddit id -"))
                {
                    String[] subredditData = line.split("-");
                    if (subredditData.length == 2) {
                        String subredditId = subredditData[1];
                        Subreddit subreddit = new Subreddit();
                        subreddit.setId(subredditId);
                        subreddits.add(subreddit);
                    }
                }
            }

            // Wait for the process to finish and get the exit code
            int exitCode = process.waitFor();

            // Handle any errors that occurred during execution
            if (exitCode != 0) {
                // You can log an error message or handle it as needed
                System.err.println("An error occurred while executing the Python script. Exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            // Handle exceptions that occurred during execution
            e.printStackTrace();
        }
    }
}

