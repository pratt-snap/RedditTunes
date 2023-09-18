package com.musicrecom.processor.repository;

import com.musicrecom.processor.entity.SongsRedditor;
import com.musicrecom.processor.entity.SongsRedditorId;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongsRedditorRepository extends JpaRepository<SongsRedditor, SongsRedditorId> {
    List<SongsRedditor> findByRedditorId(String userId);
    List<SongsRedditor> findByRedditorId(String userId, Sort sortBySongId);
}
