package com.whatisthis.scorer.services;

import com.whatisthis.scorer.model.dto.IncomeScore;
import org.springframework.stereotype.Service;

@Service
public class CalculateIncomeScoreService {

    private static final double HIGH_INCOME_THRESHOLD = 100000.0;
    private static final double MEDIUM_INCOME_THRESHOLD = 50000.0;
    private static final int HIGH_INCOME_SCORE = 300;
    private static final int MEDIUM_INCOME_SCORE_MIN = 150;
    private static final int MEDIUM_INCOME_SCORE_MAX = 300;
    private static final int LOW_INCOME_SCORE = 0;

    public IncomeScore calculate(double income) {
        if (income > HIGH_INCOME_THRESHOLD) {
            return new IncomeScore(HIGH_INCOME_SCORE);
        }

        if (income > MEDIUM_INCOME_THRESHOLD) {
            double proportion = (income - MEDIUM_INCOME_THRESHOLD) / (HIGH_INCOME_THRESHOLD - MEDIUM_INCOME_THRESHOLD);
            int score = (int) (MEDIUM_INCOME_SCORE_MIN + proportion * (MEDIUM_INCOME_SCORE_MAX - MEDIUM_INCOME_SCORE_MIN));
            return new IncomeScore(score);
        }

        if (income > 0) {
            double proportion = income / MEDIUM_INCOME_THRESHOLD;
            int score = (int) (proportion * MEDIUM_INCOME_SCORE_MIN);
            return new IncomeScore(score);
        }

        return new IncomeScore(LOW_INCOME_SCORE);
    }

}
