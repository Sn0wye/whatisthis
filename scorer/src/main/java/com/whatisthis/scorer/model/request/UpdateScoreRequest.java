package com.whatisthis.scorer.model.request;

public record UpdateScoreRequest(
        float income,
        float debt,
        float assetsValue
) {
}
