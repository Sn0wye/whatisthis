package com.whatisthis.scorer.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public record ScoreEvent(
        String userId,
        int income,
        int debt,
        int assetsValue
) {
    public ScoreEvent(String json) throws JsonProcessingException {
        this(new ObjectMapper().readValue(json, ScoreEvent.class));
    }

    private ScoreEvent(ScoreEvent scoreEvent) {
        this(scoreEvent.userId(), scoreEvent.income(), scoreEvent.debt(), scoreEvent.assetsValue());
    }
}