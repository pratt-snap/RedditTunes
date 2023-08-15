package com.msrcrecomm.main.services;

import com.msrcrecomm.main.dto.SongsDTO;
import com.msrcrecomm.main.entity.Song;
import com.msrcrecomm.main.entity.SongsRedditor;
import com.msrcrecomm.main.entity.Subreddit;
import com.msrcrecomm.main.repository.SongRepository;
import com.msrcrecomm.main.repository.SongsRedditorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RedditCallsService {

    @Autowired
    private OpenAICallsService openAICallsService;

    @Autowired
    private SongsRedditorService songsRedditorService;

    @Autowired
    private SongsRedditorRepository songsRedditorRepository;

    @Autowired
    private SongRepository songRepository;

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void shutdownExecutorService() {
        executorService.shutdown();
    }


    public List<SongsDTO> processRedditor(String userId) {
        List<Song> userSongs=new ArrayList<>();
        Boolean isUserProcessed=songsRedditorService.isRedditorProcessed(userId);
        if(isUserProcessed){
            List<SongsRedditor> songUserEntries=songsRedditorRepository.findByRedditorId(userId);
            for(SongsRedditor entry:songUserEntries){
                Song song=songRepository.getReferenceById(entry.getSong().getId());
                userSongs.add(song);
            }
        }
        else{
            executorService.submit(() -> openAICallsService.runBatchJob(userId));
            //start polling until isUserProcessed is true
            while (true) {
                isUserProcessed = songsRedditorService.isRedditorProcessed(userId);
                if (isUserProcessed) {
                    System.out.println("User Processed, breaking from poll");
                    break;
                }
                try {
                    System.out.println("polling");
                    Thread.sleep(30000); // Wait for 10 seconds before next check
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            Sort sortBySongId = Sort.by(Sort.Order.asc("id.songId"));
            //get songs in sorted order
            List<SongsRedditor> songUserEntries = songsRedditorRepository.findByRedditorId(userId, sortBySongId);
            for(SongsRedditor entry:songUserEntries){
                Song song=songRepository.getReferenceById(entry.getSong().getId());
                userSongs.add(song);
            }

            //return response
        }
        List<SongsDTO> responseList=new ArrayList<>();
        entityToDTO(userSongs,responseList);
        return responseList;
    }
    private void entityToDTO(List<Song> songsForUser, List<SongsDTO> responseList) {
        for(Song song:songsForUser){
            SongsDTO songdto=new SongsDTO();
            songdto.setTitle(song.getName());
            songdto.setAlbumName(song.getAlbumName());
            songdto.setArtistName(song.getArtistName());
            songdto.setUrl(song.getUrl());
            responseList.add(songdto);
        }
    }
}

