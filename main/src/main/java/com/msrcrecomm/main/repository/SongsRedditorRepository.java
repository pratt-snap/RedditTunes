package com.msrcrecomm.main.repository;

import com.msrcrecomm.main.entity.SongsRedditor;
import com.msrcrecomm.main.entity.SongsRedditorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongsRedditorRepository extends JpaRepository<SongsRedditor, SongsRedditorId> {
    List<SongsRedditor> findByRedditorId(String userId);
}
