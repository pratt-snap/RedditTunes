package com.musicrecom.processor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

@Embeddable
public class RedditorSubredditId implements Serializable {
    @Column(name = "redditor_id")
    private String redditorId;

    @Column(name = "subreddit_id")
    private String subredditId;

    public RedditorSubredditId(String redditorId, String subredditId) {
        this.redditorId = redditorId;
        this.subredditId = subredditId;
    }

    public RedditorSubredditId() {
    }

    public String getRedditorId() {
        return redditorId;
    }

    public void setRedditorId(String redditorId) {
        this.redditorId = redditorId;
    }

    public String getSubredditId() {
        return subredditId;
    }

    public void setSubredditId(String subredditId) {
        this.subredditId = subredditId;
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
