package com.whatisthis.scorer.controllers;

import com.whatisthis.scorer.entities.Score;
import com.whatisthis.scorer.model.response.ScoreResponse;
import com.whatisthis.scorer.repositories.ScoreRepository;
import com.whatisthis.scorer.services.CalculateCreditScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class ScoreController {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private CalculateCreditScoreService calculateCreditScoreService;

    @PostMapping("/score/calculate")
    public ResponseEntity<ScoreResponse> calculateScore(@RequestBody Score score) {
        int creditScore = calculateCreditScoreService.execute(
                score.getIncome(),
                score.getDebt(),
                score.getPropertyValue()
        );

        score.setCreditScore(creditScore);

        scoreRepository.save(score);

        return ResponseEntity.ok(
                new ScoreResponse(
                        score.getUserId(),
                        score.getCreditScore()
                )
        );
    }

    @PostMapping("/score/update")
    public ResponseEntity<ScoreResponse> updateScore(@RequestBody Score score) {
        Score existingScore = scoreRepository.findByUserId(score.getUserId()).orElse(null);

        if (existingScore == null) {
            return ResponseEntity.notFound().build();
        }

        existingScore.setIncome(score.getIncome());
        existingScore.setDebt(score.getDebt());
        existingScore.setPropertyValue(score.getPropertyValue());

        int creditScore = calculateCreditScoreService.execute(
                existingScore.getIncome(),
                existingScore.getDebt(),
                existingScore.getPropertyValue()
        );

        existingScore.setCreditScore(creditScore);

        scoreRepository.save(existingScore);

        return ResponseEntity.ok(
                new ScoreResponse(
                        existingScore.getUserId(),
                        existingScore.getCreditScore()
                )
        );
    }

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
