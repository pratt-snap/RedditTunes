package com.msrcrecomm.main.entity;


import jakarta.persistence.*;


@Entity
@Table(name = "songs_subreddit")
public class SongsSubreddit {

    @EmbeddedId
    private SongsSubredditId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subreddit_id", referencedColumnName = "subreddit_id", insertable = false, updatable = false)
    private Subreddit subreddit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", referencedColumnName = "song_id", insertable = false, updatable = false)
    private Song song;

    // Constructors, Getters, and Setters

    public SongsSubreddit() {
    }

    public SongsSubreddit(SongsSubredditId id, Subreddit subreddit, Song song) {
        this.id = id;
        this.subreddit = subreddit;
        this.song = song;
    }

    public SongsSubreddit(SongsSubredditId songsSubredditId) {
        this.id=songsSubredditId;
    }

    public SongsSubredditId getId() {
        return id;
    }

    public void setId(SongsSubredditId id) {
        this.id = id;
    }

    public Subreddit getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(Subreddit subreddit) {
        this.subreddit = subreddit;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }
}

