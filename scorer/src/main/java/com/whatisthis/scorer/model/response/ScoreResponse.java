package com.whatisthis.scorer.model.response;

public record ScoreResponse(
        String userId,
        int creditScore
) {
}
