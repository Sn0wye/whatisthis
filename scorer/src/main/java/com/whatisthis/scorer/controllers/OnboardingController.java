package com.whatisthis.scorer.controllers;

import com.whatisthis.scorer.entities.Score;
import com.whatisthis.scorer.model.request.OnboardingRequest;
import com.whatisthis.scorer.model.response.OnboardingResponse;
import com.whatisthis.scorer.repositories.ScoreRepository;
import com.whatisthis.scorer.services.CalculateCreditScoreService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OnboardingController {

    @Autowired
    private CalculateCreditScoreService calculateCreditScoreService;

    @Autowired
    private ScoreRepository scoreRepository;

    @PostMapping("/onboarding")
    public ResponseEntity<OnboardingResponse> onboarding(HttpServletRequest request, @RequestBody OnboardingRequest body) {
        String userId = request.getAttribute("userId").toString();

        Score score = scoreRepository.findByUserId(userId).orElse(null);

        if (score != null) {
            return ResponseEntity.ok(
                    new OnboardingResponse(
                            userId,
                            true,
                            score.getCreditScore()
                    )
            );
        }

        int creditScore = calculateCreditScoreService.execute(
                body.income(),
                body.debt(),
                body.propertyValue()
        );

        Score newScore = new Score();
        newScore.setUserId(userId);
        newScore.setIncome(body.income());
        newScore.setDebt(body.debt());
        newScore.setPropertyValue(body.propertyValue());
        newScore.setCreditScore(creditScore);
        scoreRepository.save(newScore);

        return ResponseEntity.ok(
                new OnboardingResponse(
                        newScore.getUserId(),
                        true,
                        newScore.getCreditScore()
                )
        );
    }
}
