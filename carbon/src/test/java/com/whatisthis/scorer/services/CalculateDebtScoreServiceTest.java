package com.whatisthis.scorer.services;

import com.whatisthis.scorer.model.dto.DebtScore;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalculateDebtScoreServiceTest {

    private final CalculateDebtScoreService service = new CalculateDebtScoreService();

    @Test
    void shouldReturnHighRatioScoreForLowDebtRatio() {
        // Given
        BigDecimal income = BigDecimal.valueOf(100_000.00); // 100K
        BigDecimal debt = BigDecimal.valueOf(10_000.00);    // 10K (10% ratio)

        // When
        DebtScore result = service.calculate(income, debt);

        // Then
        assertThat(result.getScore()).isEqualTo(300); // High ratio score
    }

    @Test
    void shouldReturnProportionalScoreForMediumDebtRatio() {
        // Given
        BigDecimal income = BigDecimal.valueOf(100_000.00); // 100K
        BigDecimal debt = BigDecimal.valueOf(30_000.00);    // 30K (30% ratio)

        // When
        DebtScore result = service.calculate(income, debt);

        // Proportional score calculation
        double ratioPercent = 30.0; // 30% debt-to-income ratio
        double proportion = (ratioPercent - 20) / (40 - 20);
        int expectedScore = (int) (150 + proportion * (300 - 150));

        // Then
        assertThat(result.getScore()).isEqualTo(expectedScore);
    }

    @Test
    void shouldReturnLowRatioScoreForHighDebtRatio() {
        // Given
        BigDecimal income = BigDecimal.valueOf(100_000.00); // 100K
        BigDecimal debt = BigDecimal.valueOf(80_000.00);    // 80K (80% ratio)

        // When
        DebtScore result = service.calculate(income, debt);

        // Proportional score calculation
        double ratioPercent = 80.0; // 80% debt-to-income ratio
        double proportion = (ratioPercent - 40) / (100 - 40);
        int expectedScore = (int) (0 + proportion * (150 - 0));

        // Then
        assertThat(result.getScore()).isEqualTo(expectedScore);
    }

    @Test
    void shouldHandleDebtRatioAtLowThresholdBoundary() {
        // Given
        BigDecimal income = BigDecimal.valueOf(100_000.00); // 100K
        BigDecimal debt = BigDecimal.valueOf(20_000.00);    // 20K (20% ratio)

        // When
        DebtScore result = service.calculate(income, debt);

        // Then
        assertThat(result.getScore()).isEqualTo(150); // Lower bound of medium range
    }

    @Test
    void shouldHandleDebtRatioAtMediumThresholdBoundary() {
        // Given
        BigDecimal income = BigDecimal.valueOf(100_000.00); // 100K
        BigDecimal debt = BigDecimal.valueOf(40_000.00);    // 40K (40% ratio)

        // When
        DebtScore result = service.calculate(income, debt);

        // Then
        assertThat(result.getScore()).isEqualTo(0); // Upper bound of low range
    }

    @Test
    void shouldThrowExceptionWhenIncomeIsZero() {
        // Given
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal debt = BigDecimal.valueOf(10_000.00); // 10K

        // Then
        assertThatThrownBy(() -> service.calculate(income, debt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Income cannot be zero.");
    }

    @Test
    void shouldReturnHighRatioScoreForZeroDebt() {
        // Given
        BigDecimal income = BigDecimal.valueOf(100_000.00); // 100K
        BigDecimal debt = BigDecimal.ZERO;                  // No debt (0% ratio)

        // When
        DebtScore result = service.calculate(income, debt);

        // Then
        assertThat(result.getScore()).isEqualTo(300); // High ratio score
    }
}
