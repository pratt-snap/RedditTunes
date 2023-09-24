package com.msrcrecomm.main.services;

import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import java.io.IOException;
import java.net.URI;

@Service
public class SpotifyCallsService {

    private static final Logger logger = LoggerFactory.getLogger(SpotifyCallsService.class);

    private static String SPOTIFY_AUTH_CODE="";

    private static String SPOTIFY_ACCESS_TOKEN="";


    public SpotifyApi getAccessToken(String code){
        URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8081/callback");
        String SPOTIFY_CLIENT_ID=System.getenv("SPOTIFY_CLIENT_ID");
        String SPOTIFY_CLIENT_SECRETE=System.getenv("SPOTIFY_CLIENT_SECRETE");
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(SPOTIFY_CLIENT_ID)
                .setClientSecret(SPOTIFY_CLIENT_SECRETE)
                .setRedirectUri(redirectUri)
                .build();
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
                .build();
        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
            logger.info("Expires in: " + spotifyApi.getAccessToken());
            logger.info("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return spotifyApi;
    }

}
