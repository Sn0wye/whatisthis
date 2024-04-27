package com.whatisthis.scorer.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public record ScoreEvent(
        String userId,
        double income,
        double debt,
        double propertyValue
) {
    public ScoreEvent(String userId, double income, double debt, double propertyValue) {
        this.userId = userId;
        this.income = income;
        this.debt = debt;
        this.propertyValue = propertyValue;
    }

    public ScoreEvent(String json) throws JsonProcessingException {
        this(new ObjectMapper().readValue(json, ScoreEvent.class));
    }

    private ScoreEvent(ScoreEvent scoreEvent) {
        this(scoreEvent.userId(), scoreEvent.income(), scoreEvent.debt(), scoreEvent.propertyValue());
    }
}
