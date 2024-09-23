package com.whatisthis.scorer.services;

import com.whatisthis.scorer.model.dto.AssetScore;
import org.springframework.stereotype.Service;

@Service
public class CalculateAssetsScoreService {

    private static final long EXPENSIVE_ASSETS_THRESHOLD = 500_000_00L; // 500K in cents
    private static final long MEDIUM_ASSETS_THRESHOLD = 200_000_00L;   // 200K in cents
    private static final int HIGH_ASSETS_SCORE = 300;
    private static final int MEDIUM_ASSETS_SCORE_MIN = 150;
    private static final int MEDIUM_ASSETS_SCORE_MAX = 300;
    private static final int LOW_ASSETS_SCORE = 0;

    public AssetScore calculate(long assetsValueInCents) {
        // Ensure that the input value is handled in cents consistently
        if (assetsValueInCents < 0) {
            throw new IllegalArgumentException("Assets value cannot be negative.");
        }

        if (assetsValueInCents > EXPENSIVE_ASSETS_THRESHOLD) {
            return new AssetScore(HIGH_ASSETS_SCORE);
        }

        if (assetsValueInCents > MEDIUM_ASSETS_THRESHOLD) {
            double proportion = (double) (assetsValueInCents - MEDIUM_ASSETS_THRESHOLD) /
                    (EXPENSIVE_ASSETS_THRESHOLD - MEDIUM_ASSETS_THRESHOLD);
            int score = (int) (MEDIUM_ASSETS_SCORE_MIN + proportion * (MEDIUM_ASSETS_SCORE_MAX - MEDIUM_ASSETS_SCORE_MIN));
            return new AssetScore(score);
        }

        if (assetsValueInCents > 0) {
            double proportion = (double) assetsValueInCents / MEDIUM_ASSETS_THRESHOLD;
            int score = (int) (proportion * MEDIUM_ASSETS_SCORE_MIN);
            return new AssetScore(score);
        }

        return new AssetScore(LOW_ASSETS_SCORE);
    }
}
