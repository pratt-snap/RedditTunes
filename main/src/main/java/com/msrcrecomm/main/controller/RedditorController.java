package com.msrcrecomm.main.controller;

import com.msrcrecomm.main.services.RedditCallsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedditorController {

    private RedditCallsService redditCallsService;
    @Autowired
    public RedditorController(RedditCallsService redditCallsService){
        this.redditCallsService=redditCallsService;
    }

    @GetMapping(value="/redditor")
    public void runRedditorJob(@RequestParam("userId") String userId){
        //Integrate Python Script
        redditCallsService.processRedditor(userId);
    }

}
