package com.msrcrecomm.main.services;

import com.msrcrecomm.main.dto.SongsDTO;
import com.msrcrecomm.main.entity.Song;
import com.msrcrecomm.main.entity.SongsRedditor;
import com.msrcrecomm.main.repository.SongRepository;
import com.msrcrecomm.main.repository.SongsRedditorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class RedditCallsService {


    @Autowired
    private SongsRedditorService songsRedditorService;

    @Autowired
    private SongsRedditorRepository songsRedditorRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    private static final Logger logger = LoggerFactory.getLogger(RedditCallsService.class);


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
            kafkaProducerService.sendBatchJobRequest(userId);
            logger.info("message sent for user {}", userId);
            while (true) {
                Long numberOfSongsProcessed = songsRedditorService.numberSongsProcessed(userId);
                if (numberOfSongsProcessed>=10) {
                    logger.info("User Processed, breaking from poll. Number of songs processed for user {}", numberOfSongsProcessed);
                    break;
                }
                try {
                    System.out.println("polling");
                    Thread.sleep(60000); // Wait for 60 seconds before next check
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            Sort sortBySongId = Sort.by(Sort.Order.asc("id.songId"));
            //get songs in sorted order
            List<SongsRedditor> songUserEntries = songsRedditorRepository.findByRedditorId(userId, sortBySongId);
            for(SongsRedditor entry:songUserEntries){
                Optional<Song> optsong=songRepository.findById(entry.getSong().getId());
                Song song=optsong.get();
                userSongs.add(song);
                if(userSongs.size()>=10){
                    break;
                }
            }
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

