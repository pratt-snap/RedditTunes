package com.msrcrecomm.main.controller;

import com.msrcrecomm.main.services.SpotifyCallsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@RestController
public class SpotifyController {
    @Autowired
    private SpotifyCallsService spotifyCallsService;

    @Value("${spotify.credentials.filepath}")
    private String spotifyCredentialsFilePath;

    @GetMapping(value = "/callback")
    public ResponseEntity<String> handleCallback(@RequestParam("code") String authorizationCode) {
        String accessToken=spotifyCallsService.getAccessToken(authorizationCode).getAccessToken();
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(spotifyCredentialsFilePath))) {
            writer.write(authorizationCode);
            writer.write("\n");
            writer.write(accessToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Authorization code: " + authorizationCode);
    }
}
