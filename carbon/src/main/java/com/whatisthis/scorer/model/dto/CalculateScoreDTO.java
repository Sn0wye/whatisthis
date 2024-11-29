package com.whatisthis.scorer.model.dto;

import java.math.BigDecimal;

public record CalculateScoreDTO(
        BigDecimal income,
        BigDecimal debt,
        BigDecimal assetsValue
) {
}
