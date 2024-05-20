package com.whatisthis.scorer.model.request;

public record OnboardingRequest(
        float income,
        float debt,
        float propertyValue
) {
}
