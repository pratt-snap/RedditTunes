package com.msrcrecomm.main.dto;

public class SongsDTO {
    private String title;
    private String artistName;
    private String albumName;
    private String url;

    public SongsDTO(String title, String artistName, String albumName, String url) {
        this.title = title;
        this.artistName = artistName;
        this.albumName = albumName;
        this.url = url;
    }

    public SongsDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "SongsDTO{" +
                "title='" + title + '\'' +
                ", artistName='" + artistName + '\'' +
                ", albumName='" + albumName + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
