package com.whatisthis.scorer.services;

import com.whatisthis.scorer.model.dto.PropertyScore;
import org.springframework.stereotype.Service;

@Service
public class CalculatePropertyScoreService {

    private static final double EXPENSIVE_PROPERTY_THRESHOLD = 500000.0;
    private static final double MEDIUM_PRICED_PROPERTY_THRESHOLD = 200000.0;
    private static final int HIGH_PROPERTY_SCORE = 300;
    private static final int MEDIUM_PROPERTY_SCORE_MIN = 150;
    private static final int MEDIUM_PROPERTY_SCORE_MAX = 300;
    private static final int LOW_PROPERTY_SCORE = 0;

    public PropertyScore calculate(double propertyValue) {
        if (propertyValue > EXPENSIVE_PROPERTY_THRESHOLD) {
            return new PropertyScore(HIGH_PROPERTY_SCORE);
        }

        if (propertyValue > MEDIUM_PRICED_PROPERTY_THRESHOLD) {
            double proportion = (propertyValue - MEDIUM_PRICED_PROPERTY_THRESHOLD) / (EXPENSIVE_PROPERTY_THRESHOLD - MEDIUM_PRICED_PROPERTY_THRESHOLD);
            int score = (int) (MEDIUM_PROPERTY_SCORE_MIN + proportion * (MEDIUM_PROPERTY_SCORE_MAX - MEDIUM_PROPERTY_SCORE_MIN));
            return new PropertyScore(score);
        }

        if (propertyValue > 0) {
            double proportion = propertyValue / MEDIUM_PRICED_PROPERTY_THRESHOLD;
            int score = (int) (proportion * MEDIUM_PROPERTY_SCORE_MIN);
            return new PropertyScore(score);
        }

        return new PropertyScore(LOW_PROPERTY_SCORE);
    }
}
