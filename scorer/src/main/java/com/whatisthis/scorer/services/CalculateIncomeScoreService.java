package com.whatisthis.scorer.services;

import com.whatisthis.scorer.model.IncomeScore;
import org.springframework.stereotype.Service;

// Income Score:
// High Income: 300 (Above $100,000 per year)
// Medium Income: 150 ($50,000 - $100,000 per year)
// Low Income: 0 (Below $50,000 per year)

@Service
public class CalculateIncomeScoreService {

    public IncomeScore calculate(double income) {
        if (income > 100000) {
            return IncomeScore.HIGH();
        } else if (income > 50000) {
            double proportion = (income - 50000) / (100000 - 50000);
            int score = (int) (150 + proportion * (300 - 150));
            return new IncomeScore(score);
        } else {
            double proportion = income / 50000;
            int score = (int) (proportion * 150);
            return new IncomeScore(score);
        }
    }

}
