package com.msrcrecomm.main.repository;

import com.msrcrecomm.main.entity.SongsSubreddit;
import com.msrcrecomm.main.entity.SongsSubredditId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongsSubredditRepository extends JpaRepository<SongsSubreddit, SongsSubredditId> {

    List<SongsSubreddit> findBySubredditId(String subredditId);
}

