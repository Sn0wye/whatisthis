package com.whatisthis.scorer.services;

import com.whatisthis.scorer.model.PropertyScore;
import org.springframework.stereotype.Service;

// Property Ownership Score:
// Owns Expensive Property: 300 (Value above $500,000)
// Owns Medium-Priced Property: 150 (Value between $200,000 and $500,000)
// Low-Priced / Does Not Own Property: 0 (Below $200,000)

@Service
public class CalculatePropertyScoreService {

    public PropertyScore calculate(double propertyValue) {
        if (propertyValue > 500000) {
            return PropertyScore.HIGH();
        } else if (propertyValue > 200000) {
            double proportion = (propertyValue - 200000) / (500000 - 200000);
            int score = (int) (150 + proportion * (300 - 150));
            return new PropertyScore(score);
        } else if (propertyValue > 0) {
            double proportion = propertyValue / 200000;
            int score = (int) (proportion * 150);
            return new PropertyScore(score);
        } else {
            return PropertyScore.LOW();
        }
    }
}