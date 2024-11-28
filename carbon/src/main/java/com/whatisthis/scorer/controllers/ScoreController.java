package com.whatisthis.scorer.controllers;

import com.whatisthis.scorer.entities.Score;
import com.whatisthis.scorer.model.request.CalculateScoreRequest;
import com.whatisthis.scorer.model.request.UpdateScoreRequest;
import com.whatisthis.scorer.model.response.ScoreResponse;
import com.whatisthis.scorer.repositories.ScoreRepository;
import com.whatisthis.scorer.services.CalculateCreditScoreService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScoreController {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private CalculateCreditScoreService calculateCreditScoreService;

    @PostMapping("/score/calculate")
    public ResponseEntity<ScoreResponse> calculateScore(HttpServletRequest request, @Valid @RequestBody CalculateScoreRequest body) {
        String userId = request.getAttribute("userId").toString();

        int creditScore = calculateCreditScoreService.execute(
                body.incomeCents(),
                body.debtCents(),
                body.assetsValueCents()
        );

        Score score = scoreRepository.findByUserId(userId).orElseGet(Score::new);

        score.setUserId(userId);
        score.setIncome(body.incomeCents());
        score.setDebt(body.debtCents());
        score.setAssetsValue(body.assetsValueCents());
        score.setCreditScore(creditScore);

        scoreRepository.save(score);

        return ResponseEntity.ok(
                new ScoreResponse(
                        score.getCreditScore()
                )
        );
    }

    @PostMapping("/score/update")
    public ResponseEntity<ScoreResponse> updateScore(HttpServletRequest request, @Valid @RequestBody UpdateScoreRequest body) {
        String userId = request.getAttribute("userId").toString();

        Score existingScore = scoreRepository.findByUserId(userId).orElse(null);

        if (existingScore == null) {
            return ResponseEntity.notFound().build();
        }

        existingScore.setIncome(body.incomeCents());
        existingScore.setDebt(body.debtCents());
        existingScore.setAssetsValue(body.assetsValueCents());

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
