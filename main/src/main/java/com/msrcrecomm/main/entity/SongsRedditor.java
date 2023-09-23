package com.msrcrecomm.main.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "redditor_song")
public class SongsRedditor {

    @EmbeddedId
    private SongsRedditorId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",insertable = false, updatable = false)
    private Redditor redditor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id",insertable = false, updatable = false)
    private Song song;

    public SongsRedditor() {
    }


    public Song getSong() {
        return song;
    }

}

