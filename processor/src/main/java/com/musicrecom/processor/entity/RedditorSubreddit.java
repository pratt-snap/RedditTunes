package com.musicrecom.processor.entity;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


@Entity
@Table(name = "redditor_subreddits")
public class RedditorSubreddit {

    @EmbeddedId
    private RedditorSubredditId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "redditor_id", insertable = false, updatable = false)
    private Redditor redditor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subreddit_id", insertable = false, updatable = false)
    private Subreddit subreddit;

    public RedditorSubreddit(RedditorSubredditId id) {
        this.id = id;
    }

    public RedditorSubreddit() {
    }

    public RedditorSubredditId getId() {
        return id;
    }

    public void setId(RedditorSubredditId id) {
        this.id = id;
    }

    public Redditor getRedditor() {
        return redditor;
    }

    public void setRedditor(Redditor redditor) {
        this.redditor = redditor;
    }

    public Subreddit getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(Subreddit subreddit) {
        this.subreddit = subreddit;
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

