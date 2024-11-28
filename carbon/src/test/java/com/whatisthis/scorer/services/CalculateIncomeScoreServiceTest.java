package com.whatisthis.scorer.services;

import com.whatisthis.scorer.model.dto.IncomeScore;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CalculateIncomeScoreServiceTest {

    private final CalculateIncomeScoreService service = new CalculateIncomeScoreService();

    @Test
    void shouldReturnHighIncomeScoreForIncomeAboveHighThreshold() {
        // Given
        long income = 150_000_00L; // 150K in cents

        // When
        IncomeScore result = service.calculate(income);

        // Then
        assertThat(result.getScore()).isEqualTo(300); // High income score
    }

    @Test
    void shouldReturnProportionalScoreForMediumIncomeRange() {
        // Given
        long income = 75_000_00L; // 75K in cents

        // When
        IncomeScore result = service.calculate(income);

        // Proportional score calculation
        double proportion = (double) (income - 50_000_00L) / (100_000_00L - 50_000_00L);
        int expectedScore = (int) (150 + proportion * (300 - 150));

        // Then
        assertThat(result.getScore()).isEqualTo(expectedScore);
    }

    @Test
    void shouldReturnProportionalScoreForLowIncomeRange() {
        // Given
        long income = 25_000_00L; // 25K in cents

        // When
        IncomeScore result = service.calculate(income);

        // Proportional score calculation
        double proportion = (double) income / 50_000_00L;
        int expectedScore = (int) (proportion * 150);

        // Then
        assertThat(result.getScore()).isEqualTo(expectedScore);
    }

    @Test
    void shouldReturnLowIncomeScoreForZeroIncome() {
        // Given
        long income = 0;

        // When
        IncomeScore result = service.calculate(income);

        // Then
        assertThat(result.getScore()).isEqualTo(0); // Low income score
    }

    @Test
    void shouldReturnBoundaryScoreForMediumIncomeThreshold() {
        // Given
        long income = 50_000_00L; // 50K in cents (medium income threshold)

        // When
        IncomeScore result = service.calculate(income);

        // Then
        assertThat(result.getScore()).isEqualTo(150); // Minimum of medium range
    }

    @Test
    void shouldReturnBoundaryScoreForHighIncomeThreshold() {
        // Given
        long income = 100_000_00L; // 100K in cents (high income threshold)

        // When
        IncomeScore result = service.calculate(income);

        // Then
        assertThat(result.getScore()).isEqualTo(300); // High income score
    }

    @Test
    void shouldHandleIncomeJustBelowMediumThreshold() {
        // Given
        long income = 49_999_99L; // Just below medium income threshold

        // When
        IncomeScore result = service.calculate(income);

        // Proportional score calculation
        double proportion = (double) income / 50_000_00L;
        int expectedScore = (int) (proportion * 150);

        // Then
        assertThat(result.getScore()).isEqualTo(expectedScore);
    }

    @Test
    void shouldHandleIncomeJustBelowHighThreshold() {
        // Given
        long income = 99_999_99L; // Just below high income threshold

        // When
        IncomeScore result = service.calculate(income);

        // Proportional score calculation
        double proportion = (double) (income - 50_000_00L) / (100_000_00L - 50_000_00L);
        int expectedScore = (int) (150 + proportion * (300 - 150));

        // Then
        assertThat(result.getScore()).isEqualTo(expectedScore);
    }
}
