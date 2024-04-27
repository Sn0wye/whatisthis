package com.whatisthis.scorer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.whatisthis.scorer.entities.Score;

public interface ScoreRepository extends JpaRepository<Score, String> {}