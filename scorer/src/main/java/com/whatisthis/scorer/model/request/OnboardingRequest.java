package com.whatisthis.scorer.model.request;

public record OnboardingRequest(
        int incomeCents,
        int debtCents,
        int assetsValueCents
) {
}
