package com.msrcrecomm.main.repository;

import com.msrcrecomm.main.entity.Redditor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedditorRepository extends JpaRepository<Redditor, String> {
}
