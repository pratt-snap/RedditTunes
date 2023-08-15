package com.msrcrecomm.main.repository;

import com.msrcrecomm.main.entity.RedditorSubreddit;
import com.msrcrecomm.main.entity.RedditorSubredditId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RedditorSubredditRepository extends JpaRepository<RedditorSubreddit, RedditorSubredditId> {

    List<RedditorSubreddit> findByRedditorId(String redditorId);
}
