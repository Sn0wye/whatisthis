package com.whatisthis.scorer.controllers;

import com.whatisthis.scorer.entities.Score;
import com.whatisthis.scorer.model.response.ScoreResponse;
import com.whatisthis.scorer.repositories.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public class ScoreController {

    @Autowired
    private ScoreRepository scoreRepository;

    @GetMapping("/score/{userId}")
    public ResponseEntity<ScoreResponse> getScore(@Param("userId") String userId) {
        Score score = scoreRepository.findByUserId(userId).orElse(null);

        if (score == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                new ScoreResponse(
                        score.getUserId(),
                        score.getCreditScore()
                )
        );
    }
}
