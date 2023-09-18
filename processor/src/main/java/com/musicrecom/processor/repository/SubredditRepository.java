package com.musicrecom.processor.repository;

import com.musicrecom.processor.entity.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit, String> {
    // Additional custom query methods can be defined here if needed
}
