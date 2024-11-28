package com.whatisthis.scorer.services;

import com.whatisthis.scorer.model.dto.IncomeScore;
import org.springframework.stereotype.Service;

@Service
public class CalculateIncomeScoreService {

    private static final long HIGH_INCOME_THRESHOLD_CENTS = 10000000L; // 100,000.00 in cents
    private static final long MEDIUM_INCOME_THRESHOLD_CENTS = 5000000L; // 50,000.00 in cents
    private static final int HIGH_INCOME_SCORE = 300;
    private static final int MEDIUM_INCOME_SCORE_MIN = 150;
    private static final int MEDIUM_INCOME_SCORE_MAX = 300;
    private static final int LOW_INCOME_SCORE = 0;

    public IncomeScore calculate(long incomeCents) {
        if (incomeCents > HIGH_INCOME_THRESHOLD_CENTS) {
            return new IncomeScore(HIGH_INCOME_SCORE);
        }

        if (incomeCents > MEDIUM_INCOME_THRESHOLD_CENTS) {
            double proportion = (double) (incomeCents - MEDIUM_INCOME_THRESHOLD_CENTS) / 
                               (HIGH_INCOME_THRESHOLD_CENTS - MEDIUM_INCOME_THRESHOLD_CENTS);
            int score = (int) (MEDIUM_INCOME_SCORE_MIN + proportion * (MEDIUM_INCOME_SCORE_MAX - MEDIUM_INCOME_SCORE_MIN));
            return new IncomeScore(score);
        }

        if (incomeCents > 0) {
            double proportion = (double) incomeCents / MEDIUM_INCOME_THRESHOLD_CENTS;
            int score = (int) (proportion * MEDIUM_INCOME_SCORE_MIN);
            return new IncomeScore(score);
        }

        return new IncomeScore(LOW_INCOME_SCORE);
    }
}
