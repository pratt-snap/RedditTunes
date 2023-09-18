package com.musicrecom.processor.repository;

import com.musicrecom.processor.entity.SongsSubreddit;
import com.musicrecom.processor.entity.SongsSubredditId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongsSubredditRepository extends JpaRepository<SongsSubreddit, SongsSubredditId> {

    List<SongsSubreddit> findBySubredditId(String subredditId);
}

