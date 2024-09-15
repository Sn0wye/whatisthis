package com.whatisthis.scorer.controllers;

import com.whatisthis.scorer.entities.Score;
import com.whatisthis.scorer.model.request.CalculateScoreRequest;
import com.whatisthis.scorer.model.request.UpdateScoreRequest;
import com.whatisthis.scorer.model.response.ScoreResponse;
import com.whatisthis.scorer.repositories.ScoreRepository;
import com.whatisthis.scorer.services.CalculateCreditScoreService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<ScoreResponse> calculateScore(HttpServletRequest request, @RequestBody CalculateScoreRequest body) {
        String userId = request.getAttribute("userId").toString();

        int creditScore = calculateCreditScoreService.execute(
                body.income(),
                body.debt(),
                body.assetsValue()
        );

        Score score = new Score();
        score.setUserId(userId);
        score.setIncome(body.income());
        score.setDebt(body.debt());
        score.setAssetsValue(body.assetsValue());
        score.setCreditScore(creditScore);

        scoreRepository.save(score);

        return ResponseEntity.ok(
                new ScoreResponse(
                        score.getCreditScore()
                )
        );
    }

    @PostMapping("/score/update")
    public ResponseEntity<ScoreResponse> updateScore(HttpServletRequest request, @RequestBody UpdateScoreRequest body) {
        String userId = request.getAttribute("userId").toString();

        Score existingScore = scoreRepository.findByUserId(userId).orElse(null);

        if (existingScore == null) {
            return ResponseEntity.notFound().build();
        }

        existingScore.setIncome(body.income());
        existingScore.setDebt(body.debt());
        existingScore.setAssetsValue(body.assetsValue());

        int creditScore = calculateCreditScoreService.execute(
                existingScore.getIncome(),
                existingScore.getDebt(),
                existingScore.getAssetsValue()
        );

        existingScore.setCreditScore(creditScore);

        scoreRepository.save(existingScore);

        return ResponseEntity.ok(
                new ScoreResponse(
                        existingScore.getCreditScore()
                )
        );
    }

    @GetMapping("/score")
    public ResponseEntity<ScoreResponse> getScore(HttpServletRequest request) {
        String userId = request.getAttribute("userId").toString();

        Score score = scoreRepository.findByUserId(userId).orElse(null);

        if (score == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                new ScoreResponse(
                        score.getCreditScore()
                )
        );
    }
}
