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


    public String getName() {
        return name;
    }


    public String getArtistName() {
        return artistName;
    }


    public String getUrl() {
        return url;
    }


    public String getAlbumName() {
        return albumName;
    }


    public String getId() {
        return id;
    }

}
