package com.musicrecom.processor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(obj, this);
    }
}
