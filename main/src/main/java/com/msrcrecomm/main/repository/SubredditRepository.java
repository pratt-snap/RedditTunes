package com.msrcrecomm.main.repository;

import com.msrcrecomm.main.entity.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit, String> {
    // Additional custom query methods can be defined here if needed
}
