package com.msrcrecomm.main.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "songs_subreddit")
public class SongsSubreddit {
    @Id
    @ManyToOne
    @JoinColumn(name = "subreddit_id")
    private Subreddit subreddit;

    @Id
    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;


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
