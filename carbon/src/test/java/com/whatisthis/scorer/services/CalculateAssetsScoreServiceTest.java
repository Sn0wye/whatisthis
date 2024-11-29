package com.whatisthis.scorer.services;

import com.whatisthis.scorer.model.dto.AssetScore;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalculateAssetsScoreServiceTest {

    private final CalculateAssetsScoreService service = new CalculateAssetsScoreService();

    @Test
    void shouldReturnHighAssetsScoreForExpensiveAssets() {
        BigDecimal assetsValue = new BigDecimal("600000"); // Above the expensive assets threshold
        AssetScore result = service.calculate(assetsValue);
        assertThat(result.getScore()).isEqualTo(300);
    }

    @Test
    void shouldReturnMediumAssetsScoreWithinMediumRange() {
        BigDecimal assetsValue = new BigDecimal("350000"); // Between 200K and 500K
        AssetScore result = service.calculate(assetsValue);

        // Proportionally calculated score
        BigDecimal proportion = assetsValue.subtract(new BigDecimal("200000"))
                .divide(new BigDecimal("500000").subtract(new BigDecimal("200000")), 4, RoundingMode.FLOOR);
        int expectedScore = 150 + proportion.multiply(new BigDecimal(300 - 150)).intValue();

        assertThat(result.getScore()).isEqualTo(expectedScore);
    }

    @Test
    void shouldReturnLowAssetsScoreForNoAssets() {
        BigDecimal assetsValue = new BigDecimal("0"); // No assets
        AssetScore result = service.calculate(assetsValue);
        assertThat(result.getScore()).isEqualTo(0);
    }

    @Test
    void shouldReturnZeroForNegativeAssetsValue() {
        BigDecimal assetsValue = new BigDecimal("-1"); // Negative assets

        assertThatThrownBy(() -> service.calculate(assetsValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Assets value cannot be negative.");
    }

    @Test
    void shouldReturnScoreProportionalToLowAssetsValue() {
        BigDecimal assetsValue = new BigDecimal("100000"); // Below 200K threshold
        AssetScore result = service.calculate(assetsValue);

        // Proportionally calculated score
        BigDecimal proportion = assetsValue.divide(new BigDecimal("200000"), 4, RoundingMode.FLOOR);
        int expectedScore = proportion.multiply(new BigDecimal(150)).intValue();

        assertThat(result.getScore()).isEqualTo(expectedScore);
    }

    @Test
    void shouldReturnMediumScoreForBoundaryOfMediumAssets() {
        BigDecimal assetsValue = new BigDecimal("200000"); // Exactly on the medium threshold
        AssetScore result = service.calculate(assetsValue);

        assertThat(result.getScore()).isEqualTo(150); // Lower bound of medium range
    }

    @Test
    void shouldReturnHighScoreForBoundaryOfExpensiveAssets() {
        BigDecimal assetsValue = new BigDecimal("500000"); // Exactly on the expensive threshold
        AssetScore result = service.calculate(assetsValue);

        assertThat(result.getScore()).isEqualTo(300); // Upper bound of medium range
    }
}
