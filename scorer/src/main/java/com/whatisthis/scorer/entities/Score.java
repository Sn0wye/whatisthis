package com.whatisthis.scorer.entities;

import com.whatisthis.scorer.consumers.ScoreEvent;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.uuid.UuidGenerator;
import java.util.UUID;

@Data
@Entity
@Table(name = "scores")
public class Score {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "UUID", type = UuidGenerator.class)
    private UUID id;
    private String userId;
    private double income;
    private double debt;
    private double propertyValue;

    public Score(ScoreEvent scoreEvent) {
        this.userId = scoreEvent.userId();
        this.income = scoreEvent.income();
        this.debt = scoreEvent.debt();
        this.propertyValue = scoreEvent.propertyValue();
    }
}