package com.msrcrecomm.main.controller;

import com.msrcrecomm.main.dto.SongsDTO;
import com.msrcrecomm.main.services.RedditCallsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RedditorController {
    @Autowired
    private RedditCallsService redditCallsService;

    @PostMapping(value="/redditor")
    public List<SongsDTO> runRedditorJob(@RequestParam("userId") String userId){
       return redditCallsService.processRedditor(userId);
    }
}
