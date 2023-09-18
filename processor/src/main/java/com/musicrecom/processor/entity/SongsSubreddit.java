package com.musicrecom.processor.entity;


import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


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

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(obj, this);
    }
}

