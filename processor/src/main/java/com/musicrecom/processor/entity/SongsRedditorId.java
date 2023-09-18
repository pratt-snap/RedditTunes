package com.musicrecom.processor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

@Embeddable
public class SongsRedditorId implements Serializable {

    @Column(name = "user_id")
    private String redditorId;

    @Column(name = "song_id")
    private String songId;

    public SongsRedditorId() {
    }

    public SongsRedditorId(String redditorId, String songId) {
        this.redditorId = redditorId;
        this.songId = songId;
    }

    public String getRedditorId() {
        return redditorId;
    }

    public void setRedditorId(String redditorId) {
        this.redditorId = redditorId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
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
