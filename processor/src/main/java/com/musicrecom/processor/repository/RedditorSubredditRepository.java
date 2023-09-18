package com.musicrecom.processor.repository;

import com.musicrecom.processor.entity.RedditorSubreddit;
import com.musicrecom.processor.entity.RedditorSubredditId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RedditorSubredditRepository extends JpaRepository<RedditorSubreddit, RedditorSubredditId> {

    List<RedditorSubreddit> findByRedditorId(String redditorId);
}
