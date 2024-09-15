package com.whatisthis.scorer.model.request;

public record CalculateScoreRequest(
        float income,
        float debt,
        float assetsValue
) {
}
