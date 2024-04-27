package com.whatisthis.scorer.model.response;

public record OnboardingResponse(
        String userId,
        boolean success,
        int creditScore
) {
}
