package com.whatisthis.scorer.services;

import com.whatisthis.scorer.model.dto.DebtScore;
import org.springframework.stereotype.Service;

@Service
public class CalculateDebtScoreService {

    private static final int LOW_RATIO_THRESHOLD_PERCENT = 20;
    private static final int MEDIUM_RATIO_THRESHOLD_PERCENT = 40;
    private static final int HIGH_RATIO_SCORE = 300;
    private static final int MEDIUM_RATIO_SCORE_MIN = 150;
    private static final int MEDIUM_RATIO_SCORE_MAX = 300;
    private static final int LOW_RATIO_SCORE = 0;

    public DebtScore calculate(long incomeCents, long debtCents) {
        if (incomeCents == 0) {
            throw new IllegalArgumentException("Income cannot be zero.");
        }

        // Calculate the ratio as a percentage
        double ratioPercent = (double) debtCents / incomeCents * 100;

        if (ratioPercent < LOW_RATIO_THRESHOLD_PERCENT) {
            return new DebtScore(HIGH_RATIO_SCORE);
        }

        if (ratioPercent < MEDIUM_RATIO_THRESHOLD_PERCENT) {
            double proportion = (ratioPercent - LOW_RATIO_THRESHOLD_PERCENT) / (MEDIUM_RATIO_THRESHOLD_PERCENT - LOW_RATIO_THRESHOLD_PERCENT);
            int score = (int) (MEDIUM_RATIO_SCORE_MIN + proportion * (MEDIUM_RATIO_SCORE_MAX - MEDIUM_RATIO_SCORE_MIN));
            return new DebtScore(score);
        }

        double proportion = (ratioPercent - MEDIUM_RATIO_THRESHOLD_PERCENT) / (100 - MEDIUM_RATIO_THRESHOLD_PERCENT);
        int score = (int) (LOW_RATIO_SCORE + proportion * (MEDIUM_RATIO_SCORE_MIN - LOW_RATIO_SCORE));
        return new DebtScore(score);
    }
}
