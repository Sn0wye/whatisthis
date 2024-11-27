package com.whatisthis.scorer.services;

import com.whatisthis.scorer.model.dto.AssetScore;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalculateAssetsScoreServiceTest {

    private final CalculateAssetsScoreService service = new CalculateAssetsScoreService();

    @Test
    void shouldReturnHighAssetsScoreForExpensiveAssets() {
        long assetsValue = 600_000_00L; // Above the expensive assets threshold
        AssetScore result = service.calculate(assetsValue);
        assertThat(result.getScore()).isEqualTo(300);
    }

    @Test
    void shouldReturnMediumAssetsScoreWithinMediumRange() {
        long assetsValue = 350_000_00L; // Between 200K and 500K
        AssetScore result = service.calculate(assetsValue);

        // Proportionally calculated score
        double proportion = (double) (assetsValue - 200_000_00L) / (500_000_00L - 200_000_00L);
        int expectedScore = (int) (150 + proportion * (300 - 150));

        assertThat(result.getScore()).isEqualTo(expectedScore);
    }

    @Test
    void shouldReturnLowAssetsScoreForNoAssets() {
        long assetsValue = 0; // No assets
        AssetScore result = service.calculate(assetsValue);
        assertThat(result.getScore()).isEqualTo(0);
    }

    @Test
    void shouldReturnZeroForNegativeAssetsValue() {
        long assetsValue = -1; // Negative assets

        assertThatThrownBy(() -> service.calculate(assetsValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Assets value cannot be negative.");
    }

    @Test
    void shouldReturnScoreProportionalToLowAssetsValue() {
        long assetsValue = 100_000_00L; // Below 200K threshold
        AssetScore result = service.calculate(assetsValue);

        // Proportionally calculated score
        double proportion = (double) assetsValue / 200_000_00L;
        int expectedScore = (int) (proportion * 150);

        assertThat(result.getScore()).isEqualTo(expectedScore);
    }

    @Test
    void shouldReturnMediumScoreForBoundaryOfMediumAssets() {
        long assetsValue = 200_000_00L; // Exactly on the medium threshold
        AssetScore result = service.calculate(assetsValue);

        assertThat(result.getScore()).isEqualTo(150); // Lower bound of medium range
    }

    @Test
    void shouldReturnHighScoreForBoundaryOfExpensiveAssets() {
        long assetsValue = 500_000_00L; // Exactly on the expensive threshold
        AssetScore result = service.calculate(assetsValue);

        assertThat(result.getScore()).isEqualTo(300); // Upper bound of medium range
    }
}
