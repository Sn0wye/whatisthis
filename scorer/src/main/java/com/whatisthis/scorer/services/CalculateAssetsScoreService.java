package com.whatisthis.scorer.services;

import com.whatisthis.scorer.model.dto.AssetScore;
import org.springframework.stereotype.Service;

@Service
public class CalculateAssetsScoreService {

    private static final double EXPENSIVE_ASSETS_THRESHOLD = 500000.0;
    private static final double MEDIUM_ASSETS_THRESHOLD = 200000.0;
    private static final int HIGH_ASSETS_SCORE = 300;
    private static final int MEDIUM_ASSETS_SCORE_MIN = 150;
    private static final int MEDIUM_ASSETS_SCORE_MAX = 300;
    private static final int LOW_ASSETS_SCORE = 0;

    public AssetScore calculate(double assetsValue) {
        if (assetsValue > EXPENSIVE_ASSETS_THRESHOLD) {
            return new AssetScore(HIGH_ASSETS_SCORE);
        }

        if (assetsValue > MEDIUM_ASSETS_THRESHOLD) {
            double proportion = (assetsValue - MEDIUM_ASSETS_THRESHOLD) / (EXPENSIVE_ASSETS_THRESHOLD - MEDIUM_ASSETS_THRESHOLD);
            int score = (int) (MEDIUM_ASSETS_SCORE_MIN + proportion * (MEDIUM_ASSETS_SCORE_MAX - MEDIUM_ASSETS_SCORE_MIN));
            return new AssetScore(score);
        }

        if (assetsValue > 0) {
            double proportion = assetsValue / MEDIUM_ASSETS_THRESHOLD;
            int score = (int) (proportion * MEDIUM_ASSETS_SCORE_MIN);
            return new AssetScore(score);
        }

        return new AssetScore(LOW_ASSETS_SCORE);
    }
}
