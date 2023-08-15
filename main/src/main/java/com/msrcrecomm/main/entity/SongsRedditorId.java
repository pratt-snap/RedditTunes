package com.msrcrecomm.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

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


}
