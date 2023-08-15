package com.msrcrecomm.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "redditor")
public class Redditor {
    @Id
    @Column(name = "user_id")
    private String id;

    public Redditor(){

    }

    public Redditor(String user_id) {
        this.id = user_id;
    }

    public String getUserId() {
        return id;
    }

    public void setUserId(String userId) {
        this.id = userId;
    }
}

