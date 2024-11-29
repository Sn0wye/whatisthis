package com.whatisthis.scorer.services;

import com.whatisthis.scorer.model.dto.IncomeScore;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

class CalculateIncomeScoreServiceTest {

    private final CalculateIncomeScoreService service = new CalculateIncomeScoreService();

    @Test
    void shouldReturnHighIncomeScoreForIncomeAboveHighThreshold() {
        // Given
        BigDecimal income = new BigDecimal("150000"); // 150K (income threshold)

        // When
        IncomeScore result = service.calculate(income);

        // Then
        assertThat(result.getScore()).isEqualTo(300); // High income score
    }

    @Test
    void shouldReturnProportionalScoreForMediumIncomeRange() {
        // Given
        BigDecimal income = new BigDecimal("75000"); // 75K

        // When
        IncomeScore result = service.calculate(income);

        // Proportional score calculation
        BigDecimal proportion = income.subtract(new BigDecimal("50000"))
                .divide(new BigDecimal("100000").subtract(new BigDecimal("50000")), 4, RoundingMode.FLOOR);
        int expectedScore = 150 + proportion.multiply(new BigDecimal(300 - 150)).intValue();

        // Then
        assertThat(result.getScore()).isEqualTo(expectedScore);
    }

    @Test
    void shouldReturnProportionalScoreForLowIncomeRange() {
        // Given
        BigDecimal income = new BigDecimal("25000"); // 25K

        // When
        IncomeScore result = service.calculate(income);

        // Proportional score calculation
        BigDecimal proportion = income.divide(new BigDecimal("50000"), 4, RoundingMode.FLOOR);
        int expectedScore = proportion.multiply(new BigDecimal(150)).intValue();

        // Then
        assertThat(result.getScore()).isEqualTo(expectedScore);
    }

    @Test
    void shouldReturnLowIncomeScoreForZeroIncome() {
        // Given
        BigDecimal income = new BigDecimal("0");

        // When
        IncomeScore result = service.calculate(income);

        // Then
        assertThat(result.getScore()).isEqualTo(0); // Low income score
    }

    @Test
    void shouldReturnBoundaryScoreForMediumIncomeThreshold() {
        // Given
        BigDecimal income = new BigDecimal("50000"); // Medium income threshold

        // When
        IncomeScore result = service.calculate(income);

        // Then
        assertThat(result.getScore()).isEqualTo(150); // Minimum of medium range
    }

    @Test
    void shouldReturnBoundaryScoreForHighIncomeThreshold() {
        // Given
        BigDecimal income = new BigDecimal("100000"); // High income threshold

        // When
        IncomeScore result = service.calculate(income);

        // Then
        assertThat(result.getScore()).isEqualTo(300); // High income score
    }

    @Test
    void shouldHandleIncomeJustBelowMediumThreshold() {
        // Given
        BigDecimal income = new BigDecimal("49999.99"); // Just below medium income threshold

        // When
        IncomeScore result = service.calculate(income);

        // Proportional score calculation
        BigDecimal proportion = income.divide(new BigDecimal("50000"), 4, RoundingMode.FLOOR);
        int expectedScore = proportion.multiply(new BigDecimal(150)).intValue();

        // Then
        assertThat(result.getScore()).isEqualTo(expectedScore);
    }

    @Test
    void shouldHandleIncomeJustBelowHighThreshold() {
        // Given
        BigDecimal income = new BigDecimal("99999.99"); // Just below high income threshold

        // When
        IncomeScore result = service.calculate(income);

        // Proportional score calculation
        BigDecimal proportion = income.subtract(new BigDecimal("50000"))
                .divide(new BigDecimal("100000").subtract(new BigDecimal("50000")), 4, RoundingMode.FLOOR);
        int expectedScore = 150 + proportion.multiply(new BigDecimal(300 - 150)).intValue();

        // Then
        assertThat(result.getScore()).isEqualTo(expectedScore);
    }
}
