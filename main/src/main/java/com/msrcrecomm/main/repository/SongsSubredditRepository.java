package com.msrcrecomm.main.repository;

import com.msrcrecomm.main.entity.SongsSubreddit;
import com.msrcrecomm.main.entity.SongsSubredditId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongsSubredditRepository extends JpaRepository<SongsSubreddit, SongsSubredditId> {
    // Add custom query methods if needed
}

