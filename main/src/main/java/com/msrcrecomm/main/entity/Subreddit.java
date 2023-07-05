package com.msrcrecomm.main.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "subreddits")
public class Subreddit {
    @Id
    @Column(name = "subreddit_id")
    private String id;

    @Column(name = "subreddit_description")
    private String description;

    @Column(name="subreddit_name")
    private String name;

    @OneToMany(mappedBy = "subreddit", cascade = CascadeType.ALL)
    private List<Song> songs;

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }
}
