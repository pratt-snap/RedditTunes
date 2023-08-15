package com.msrcrecomm.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class RedditorSubredditId implements Serializable {
    @Column(name = "redditor_id")
    private String redditorId;

    @Column(name = "subreddit_id")
    private String subredditId;

    public RedditorSubredditId(String redditorId, String subredditId) {
        this.redditorId = redditorId;
        this.subredditId = subredditId;
    }

    public RedditorSubredditId() {
    }

    public String getRedditorId() {
        return redditorId;
    }

    public void setRedditorId(String redditorId) {
        this.redditorId = redditorId;
    }

    public String getSubredditId() {
        return subredditId;
    }

    public void setSubredditId(String subredditId) {
        this.subredditId = subredditId;
    }
}
