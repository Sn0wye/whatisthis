package com.whatisthis.scorer.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CalculateScoreRequest(
        @NotNull(message = "Income cannot be null.")
        @Min(value = 1, message = "Income must be greater than zero.")
        int incomeCents,

        @NotNull(message = "Debt cannot be null.")
        @Min(value = 0, message = "Debt cannot be negative.")
        int debtCents,

        @NotNull(message = "Assets value cannot be null.")
        @Min(value = 0, message = "Assets value cannot be negative.")
        int assetsValueCents
) {
}
