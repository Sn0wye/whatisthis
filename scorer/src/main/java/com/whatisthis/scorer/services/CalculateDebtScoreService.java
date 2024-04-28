package com.whatisthis.scorer.services;

import com.whatisthis.scorer.model.DebtScore;
import org.springframework.stereotype.Service;

@Service
public class CalculateDebtScoreService {

    private static final double LOW_RATIO_THRESHOLD = 20.0;
    private static final double MEDIUM_RATIO_THRESHOLD = 40.0;
    private static final int HIGH_RATIO_SCORE = 300;
    private static final int MEDIUM_RATIO_SCORE_MIN = 150;
    private static final int MEDIUM_RATIO_SCORE_MAX = 300;
    private static final int LOW_RATIO_SCORE = 0;

    public DebtScore calculate(double income, double debt) {
        double ratioPercent = (debt / income) * 100;

        if (ratioPercent < LOW_RATIO_THRESHOLD) {
            return new DebtScore(HIGH_RATIO_SCORE);
        }

        if (ratioPercent < MEDIUM_RATIO_THRESHOLD) {
            double proportion = (ratioPercent - LOW_RATIO_THRESHOLD) / (MEDIUM_RATIO_THRESHOLD - LOW_RATIO_THRESHOLD);
            int score = (int) (MEDIUM_RATIO_SCORE_MIN + proportion * (MEDIUM_RATIO_SCORE_MAX - MEDIUM_RATIO_SCORE_MIN));
            return new DebtScore(score);
        }

        double proportion = (ratioPercent - MEDIUM_RATIO_THRESHOLD) / (100 - MEDIUM_RATIO_THRESHOLD);
        int score = (int) (LOW_RATIO_SCORE + proportion * (MEDIUM_RATIO_SCORE_MIN - LOW_RATIO_SCORE));
        return new DebtScore(score);
    }
}
