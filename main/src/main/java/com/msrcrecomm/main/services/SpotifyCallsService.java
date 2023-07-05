package com.msrcrecomm.main.services;

import com.msrcrecomm.main.entity.Song;
import com.msrcrecomm.main.repository.SongRepository;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpotifyCallsService {

    @Autowired
    private SongRepository songRepository;

    private static String SPOTIFY_CLIENT_ID="0a66b96585504078872c6227c7563373";

    private static String SPOTIFY_CLIENT_SECRETE="4ed907ccb2d842ab938e995eaeac3566";

    private static String SPOTIFY_AUTH_CODE="";

    private static String SPOTIFY_ACCESS_TOKEN="";

    @Value("${spotify.credentials.filepath}")
    private String spotifyCredentialsFilePath;

    private void SetAccessTokenAndCode(){
        try (BufferedReader br = new BufferedReader(new FileReader(spotifyCredentialsFilePath))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null && lineNumber < 2) {
                if (lineNumber == 0) {
                    SPOTIFY_AUTH_CODE = line;
                } else if (lineNumber == 1) {
                    SPOTIFY_ACCESS_TOKEN = line;
                }
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("access_code: " + SPOTIFY_AUTH_CODE);
        System.out.println("access_token: " + SPOTIFY_ACCESS_TOKEN);
    }

    public List<Song> CreateSongsArray(String content) {
        List<Song> songs=new ArrayList<>();
        String[] arr= content.split("\n");
        StringBuilder str=new StringBuilder();
        SetAccessTokenAndCode();
        for(String line:arr){
            Song song=SearchSong(line);
            songs.add(song);
        }
        saveSongs(songs);
        return songs;
    }

    @Transactional
    public void saveSongs(List<Song> songs) {
        List<Song> newSongs = new ArrayList<>();

        for (Song song : songs) {
            // Check if the song already exists in the database
            if (!songRepository.existsById(song.getKey())) {
                newSongs.add(song);
            }
        }

        if (!newSongs.isEmpty()) {
            songRepository.saveAll(newSongs);
        }
    }

    public SpotifyApi getAccessToken(String code){
        URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/callback");
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(SPOTIFY_CLIENT_ID)
                .setClientSecret(SPOTIFY_CLIENT_SECRETE)
                .setRedirectUri(redirectUri)
                .build();
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
                .build();
        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            // Set access and refresh token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Expires in: " + spotifyApi.getAccessToken());
            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return spotifyApi;
    }

    public Song SearchSong(String searchQuery) {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(SPOTIFY_ACCESS_TOKEN)
                .build();
        SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(searchQuery).build();
        Song song=new Song();
        try {
            // Execute the search request
            Track[] tracks = searchTracksRequest.execute().getItems();
            song.setName(tracks[0].getName());
            song.setArtistName(tracks[0].getArtists()[0].getName());
            song.setKey(tracks[0].getId());
            song.setAlbumName(tracks[0].getAlbum().getName());
            song.setUrl(tracks[0].getUri());
        } catch (ParseException | IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return song;
    }
}
