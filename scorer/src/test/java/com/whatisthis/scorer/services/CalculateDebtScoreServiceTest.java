package com.whatisthis.scorer.services;

import com.whatisthis.scorer.model.dto.DebtScore;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalculateDebtScoreServiceTest {

    private final CalculateDebtScoreService service = new CalculateDebtScoreService();

    @Test
    void shouldReturnHighRatioScoreForLowDebtRatio() {
        // Given
        long income = 100_000_00L; // 100K in cents
        long debt = 10_000_00L;    // 10K in cents (10% ratio)

        // When
        DebtScore result = service.calculate(income, debt);

        // Then
        assertThat(result.getScore()).isEqualTo(300); // High ratio score
    }

    @Test
    void shouldReturnProportionalScoreForMediumDebtRatio() {
        // Given
        long income = 100_000_00L; // 100K in cents
        long debt = 30_000_00L;    // 30K in cents (30% ratio)

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
        long income = 100_000_00L; // 100K in cents
        long debt = 80_000_00L;    // 80K in cents (80% ratio)

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
        long income = 100_000_00L; // 100K in cents
        long debt = 20_000_00L;    // 20K in cents (20% ratio)

        // When
        DebtScore result = service.calculate(income, debt);

        // Then
        assertThat(result.getScore()).isEqualTo(150); // Lower bound of medium range
    }

    @Test
    void shouldHandleDebtRatioAtMediumThresholdBoundary() {
        // Given
        long income = 100_000_00L; // 100K in cents
        long debt = 40_000_00L;    // 40K in cents (40% ratio)

        // When
        DebtScore result = service.calculate(income, debt);

        // Then
        assertThat(result.getScore()).isEqualTo(0); // Upper bound of low range
    }

    @Test
    void shouldThrowExceptionWhenIncomeIsZero() {
        // Given
        long income = 0;
        long debt = 10_000_00L; // 10K in cents

        // Then
        assertThatThrownBy(() -> service.calculate(income, debt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Income cannot be zero.");
    }

    @Test
    void shouldReturnHighRatioScoreForZeroDebt() {
        // Given
        long income = 100_000_00L; // 100K in cents
        long debt = 0;             // No debt (0% ratio)

        // When
        DebtScore result = service.calculate(income, debt);

        // Then
        assertThat(result.getScore()).isEqualTo(300); // High ratio score
    }
}
