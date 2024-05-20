package com.whatisthis.scorer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.whatisthis.scorer.entities.Score;

import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score, String> {
    Optional<Score> findByUserId(String userId);
}