package com.msrcrecomm.main.repository;

import com.msrcrecomm.main.entity.SongsSubreddit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongsSubredditRepository extends JpaRepository<SongsSubreddit, Long> {
    // Add custom query methods if needed
}

