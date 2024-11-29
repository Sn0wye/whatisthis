package com.whatisthis.scorer.services;

import com.whatisthis.scorer.model.dto.IncomeScore;
import com.whatisthis.scorer.model.dto.DebtScore;
import com.whatisthis.scorer.model.dto.AssetScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class CalculateCreditScoreServiceTest {

    @InjectMocks
    private CalculateCreditScoreService calculateCreditScoreService;

    @Mock
    private CalculateIncomeScoreService calculateIncomeScoreService;

    @Mock
    private CalculateDebtScoreService calculateDebtScoreService;

    @Mock
    private CalculateAssetsScoreService calculateAssetsScoreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnSumOfAllScores() {
        // Given
        BigDecimal income = BigDecimal.valueOf(50_000);
        BigDecimal debt = BigDecimal.valueOf(20_000);
        BigDecimal assetsValue = BigDecimal.valueOf(300_000);

        // Mocking service calls
        when(calculateIncomeScoreService.calculate(income)).thenReturn(new IncomeScore(200));
        when(calculateDebtScoreService.calculate(income, debt)).thenReturn(new DebtScore(100));
        when(calculateAssetsScoreService.calculate(assetsValue)).thenReturn(new AssetScore(150));

        // When
        int result = calculateCreditScoreService.execute(income, debt, assetsValue);

        // Then
        assertThat(result).isEqualTo(450); // 200 + 100 + 150
    }

    @Test
    void shouldHandleZeroValuesCorrectly() {
        // Given
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal debt = BigDecimal.ZERO;
        BigDecimal assetsValue = BigDecimal.ZERO;

        // Mocking service calls
        when(calculateIncomeScoreService.calculate(income)).thenReturn(new IncomeScore(0));
        when(calculateDebtScoreService.calculate(income, debt)).thenReturn(new DebtScore(0));
        when(calculateAssetsScoreService.calculate(assetsValue)).thenReturn(new AssetScore(0));

        // When
        int result = calculateCreditScoreService.execute(income, debt, assetsValue);

        // Then
        assertThat(result).isEqualTo(0);
    }

    @Test
    void shouldCalculateCorrectlyWhenIncomeScoreDominates() {
        // Given
        BigDecimal income = BigDecimal.valueOf(200_000);
        BigDecimal debt = BigDecimal.valueOf(10_000);
        BigDecimal assetsValue = BigDecimal.valueOf(50_000);

        // Mocking service calls
        when(calculateIncomeScoreService.calculate(income)).thenReturn(new IncomeScore(500));
        when(calculateDebtScoreService.calculate(income, debt)).thenReturn(new DebtScore(50));
        when(calculateAssetsScoreService.calculate(assetsValue)).thenReturn(new AssetScore(25));

        // When
        int result = calculateCreditScoreService.execute(income, debt, assetsValue);

        // Then
        assertThat(result).isEqualTo(575); // 500 + 50 + 25
    }
}
