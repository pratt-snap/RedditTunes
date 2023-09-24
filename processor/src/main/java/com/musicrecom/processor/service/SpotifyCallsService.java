package com.musicrecom.processor.service;

import com.musicrecom.processor.dto.SongsDTO;
import com.musicrecom.processor.entity.*;
import com.musicrecom.processor.repository.SongRepository;
import com.musicrecom.processor.repository.SongsRedditorRepository;
import com.musicrecom.processor.repository.SongsSubredditRepository;
import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    private static final Logger logger = LoggerFactory.getLogger(SpotifyCallsService.class);

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private SongsSubredditRepository songsSubredditRepository;

    @Autowired
    private SongsRedditorRepository songsRedditorRepository;

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
    }

    public void CreateSongsArray(String content,String subId,String userId) {
        logger.info("Creating songs array for user {} and subreddit {}", userId,subId);
        List<SongsDTO> songs=new ArrayList<>();
        String[] arr= content.split("\n");
        StringBuilder str=new StringBuilder();
        SetAccessTokenAndCode();
        for(String line:arr){
            SongsDTO song=SearchSong(line);
            logger.info("Song title " + song.getTitle());
            songs.add(song);
        }
        List<String> savedSongs=saveSongs(songs);
        saveSubSongs(subId,savedSongs);
        saveSongsUser(userId,savedSongs);
    }

    public void saveSubSongs(String subId, List<String> savedSongIds) {
        try {
            logger.info("Saving songs for subreddit: {}", subId);
            for (String id : savedSongIds) {
                SongsSubredditId songsSubredditId = new SongsSubredditId(subId, id);
                if(!songsSubredditRepository.existsById(songsSubredditId)){
                    SongsSubreddit songsSubreddit = new SongsSubreddit(songsSubredditId);
                    songsSubredditRepository.save(songsSubreddit);
                }
            }
            logger.info("Songs saved successfully for subreddit: {}", subId);
        } catch (Exception e) {
            logger.error("An error occurred while saving songs for subreddit {}: {}", subId, e.getMessage());
        }
    }

    public void saveSongsUser(String userId, List<String> savedSongIds){
        try {
            logger.info("Number of songs saving for User: {}", savedSongIds.size());
            for (String songId : savedSongIds) {
                SongsRedditorId id = new SongsRedditorId(userId, songId);
                if(!songsRedditorRepository.existsById(id)){
                    SongsRedditor songRedditor = new SongsRedditor(id);
                    songsRedditorRepository.save(songRedditor);
                }
            }
            logger.info("Songs saved successfully for User: {}", userId);
        } catch (Exception e) {
            logger.error("An error occurred while saving songs for User {}: {}", userId, e.getMessage());
        }
    }

    public List<String> saveSongs(List<SongsDTO> songs) {
        List<Song> newSongs = new ArrayList<>();
        List<String> songsReturnIds = new ArrayList<>();
        List<Song> Savedlist=new ArrayList<>();
        try {
            for (SongsDTO Song : songs) {
                if(Song.getId()!=null){
                    songsReturnIds.add(Song.getId());
                    // Check if the song already exists in the database
                    if (!songRepository.existsById(Song.getId())) {
                        Song song=new Song();
                        song.setId(Song.getId());
                        song.setName(Song.getTitle());
                        song.setUrl(Song.getUrl());
                        song.setArtistName(Song.getArtistName());
                        song.setAlbumName(Song.getAlbumName());
                        newSongs.add(song);
                    }
                }
            }
            if (!newSongs.isEmpty()) {
                Savedlist=songRepository.saveAll(newSongs);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return songsReturnIds;
    }

    public SpotifyApi getAccessToken(String code){
        URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/callback");
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

            System.out.println("Expires in: " + spotifyApi.getAccessToken());
            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return spotifyApi;
    }

    public SongsDTO SearchSong(String searchQuery) {
        SongsDTO song=new SongsDTO();
        if(searchQuery!=null){
            SpotifyApi spotifyApi = new SpotifyApi.Builder()
                    .setAccessToken(SPOTIFY_ACCESS_TOKEN)
                    .build();
            SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(searchQuery).build();
            try {
                Track[] tracks = searchTracksRequest.execute().getItems();
                song.setTitle(tracks[0].getName());
                song.setArtistName(tracks[0].getArtists()[0].getName());
                song.setId(tracks[0].getId());
                song.setAlbumName(tracks[0].getAlbum().getName());
                song.setUrl(tracks[0].getExternalUrls().get("spotify"));
            } catch (ParseException | IOException | SpotifyWebApiException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return song;
    }
}
