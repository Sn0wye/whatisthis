package com.whatisthis.scorer.services;

import com.whatisthis.scorer.model.DebtScore;
import org.springframework.stereotype.Service;

// Debt-to-Income Ratio Score:
// Low Ratio: 300 (Below 20%)
// Medium Ratio: 150 (20% - 40%)
// High Ratio: 0 (Above 40%)

@Service
public class CalculateDebtScoreService {

    public DebtScore calculate(double income, double debt) {
        double ratioPercent = (debt / income) * 100;

        if (ratioPercent < 20) {
            return DebtScore.HIGH();
        } else if (ratioPercent < 40) {
            double proportion = (ratioPercent - 20) / (40 - 20);
            int score = (int) (150 + proportion * (300 - 150));
            return new DebtScore(score);
        } else {
            double proportion = (ratioPercent - 40) / (100 - 40);
            int score = (int) (0 + proportion * (150 - 0));
            return new DebtScore(score);
        }
    }
}

