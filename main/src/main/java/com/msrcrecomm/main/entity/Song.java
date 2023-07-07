package com.msrcrecomm.main.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "song")
public class Song {
    @Id
    @Column(name = "song_id")
    private String id;

    @Column(name = "song_name")
    private String name;

    @Column(name="artist_name")
    private String artistName;

    @Column(name="url")
    private String url;

    @Column(name="album_name")
    private String albumName;

    public String getKey() {
        return id;
    }

    public void setKey(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
