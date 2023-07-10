package com.msrcrecomm.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SongsSubredditId implements Serializable {

    @Column(name = "subreddit_id")
    private String subredditId;

    @Column(name = "song_id")
    private String songId;

    // Constructors, Getters, and Setters

    public SongsSubredditId() {
    }

    public SongsSubredditId(String subredditId, String songId) {
        this.subredditId = subredditId;
        this.songId = songId;
    }

    public String getSubredditId() {
        return subredditId;
    }

    public void setSubredditId(String subredditId) {
        this.subredditId = subredditId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    // Equals and HashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SongsSubredditId)) return false;
        SongsSubredditId that = (SongsSubredditId) o;
        return Objects.equals(getSubredditId(), that.getSubredditId()) &&
                Objects.equals(getSongId(), that.getSongId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSubredditId(), getSongId());
    }
}
