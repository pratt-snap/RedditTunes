package com.musicrecom.processor.entity;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


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

    public SongsRedditor(SongsRedditorId Id) {
      this.id=Id;
    }

    public Redditor getRedditor() {
        return redditor;
    }

    public void setRedditor(Redditor redditor) {
        this.redditor = redditor;
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

