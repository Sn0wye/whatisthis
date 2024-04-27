package com.whatisthis.scorer.model.request;

public record OnboardingRequest(
    String userId,
    float income,
    float debt,
    float propertyValue
) {
}
