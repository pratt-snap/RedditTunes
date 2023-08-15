package com.msrcrecomm.main.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "redditor_subreddits")
public class RedditorSubreddit {

    @EmbeddedId
    private RedditorSubredditId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "redditor_id", insertable = false, updatable = false)
    private Redditor redditor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subreddit_id", insertable = false, updatable = false)
    private Subreddit subreddit;

    public RedditorSubreddit(RedditorSubredditId id) {
        this.id = id;
    }

    public RedditorSubreddit() {
    }

    public RedditorSubredditId getId() {
        return id;
    }

    public void setId(RedditorSubredditId id) {
        this.id = id;
    }

    public Redditor getRedditor() {
        return redditor;
    }

    public void setRedditor(Redditor redditor) {
        this.redditor = redditor;
    }

    public Subreddit getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(Subreddit subreddit) {
        this.subreddit = subreddit;
    }
}

