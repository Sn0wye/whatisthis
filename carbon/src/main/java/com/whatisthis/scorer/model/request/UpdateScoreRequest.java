package com.whatisthis.scorer.model.request;

public record UpdateScoreRequest(
        int incomeCents,
        int debtCents,
        int assetsValueCents
) {
}
