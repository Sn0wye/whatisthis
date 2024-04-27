package com.whatisthis.scorer.services;

import com.whatisthis.scorer.model.IncomeScore;
import com.whatisthis.scorer.model.DebtScore;
import com.whatisthis.scorer.model.PropertyScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalculateCreditScoreService {
    @Autowired
    private CalculateIncomeScoreService calculateIncomeScoreService;
    @Autowired
    private CalculateDebtScoreService calculateDebtScoreService;
    @Autowired
    private CalculatePropertyScoreService calculatePropertyScoreService;

    public int execute(
            double income,
            double debt,
            double propertyValue
    ) {
        IncomeScore incomeScore = this.calculateIncomeScoreService.calculate(income);
        DebtScore debtScore = this.calculateDebtScoreService.calculate(income, debt);
        PropertyScore propertyScore = this.calculatePropertyScoreService.calculate(propertyValue);

        System.out.println("Income Score: " + incomeScore.getScore());
        System.out.println("Debt Score: " + debtScore.getScore());
        System.out.println("Property Score: " + propertyScore.getScore());
        return incomeScore.getScore() + debtScore.getScore() + propertyScore.getScore();
    }
}
