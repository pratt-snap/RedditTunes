package com.msrcrecomm.main.repository;

import com.msrcrecomm.main.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, String> {
    // Additional custom query methods can be defined here if needed
}