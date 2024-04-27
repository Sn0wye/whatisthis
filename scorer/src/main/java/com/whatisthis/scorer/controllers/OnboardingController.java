package com.whatisthis.scorer.controllers;

import com.whatisthis.scorer.model.request.OnboardingRequest;
import com.whatisthis.scorer.model.response.OnboardingResponse;
import com.whatisthis.scorer.services.CalculateCreditScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OnboardingController {

    @Autowired
    private CalculateCreditScoreService calculateCreditScoreService;

    @PostMapping("/onboarding")
    public ResponseEntity<OnboardingResponse> onboarding(@RequestBody OnboardingRequest request) {
        int creditScore = calculateCreditScoreService.execute(
                request.income(),
                request.debt(),
                request.propertyValue()
        );

        return ResponseEntity.ok(
                new OnboardingResponse(
                        request.userId(),
                        true,
                        creditScore
                )
        );
    }
}
