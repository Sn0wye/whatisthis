package com.whatisthis.scorer.services;

import com.whatisthis.scorer.model.dto.IncomeScore;
import com.whatisthis.scorer.model.dto.DebtScore;
import com.whatisthis.scorer.model.dto.AssetScore;
import com.whatisthis.scorer.model.request.CalculateScoreRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CalculateCreditScoreService {
    @Autowired
    private CalculateIncomeScoreService calculateIncomeScoreService;
    @Autowired
    private CalculateDebtScoreService calculateDebtScoreService;
    @Autowired
    private CalculateAssetsScoreService calculateAssetsScoreService;

    public int execute(
            BigDecimal income,
            BigDecimal debt,
            BigDecimal assetsValue
    ) {
        IncomeScore incomeScore = this.calculateIncomeScoreService.calculate(income);
        DebtScore debtScore = this.calculateDebtScoreService.calculate(income, debt);
        AssetScore assetScore = this.calculateAssetsScoreService.calculate(assetsValue);

        return incomeScore.getScore() + debtScore.getScore() + assetScore.getScore();
    }
}
