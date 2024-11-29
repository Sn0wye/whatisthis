package com.whatisthis.scorer.entities;

import com.whatisthis.scorer.consumers.ScoreEvent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.uuid.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "scores")
public class Score {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "UUID", type = UuidGenerator.class)
    private UUID id;
    private String userId;
    private BigDecimal income;
    private BigDecimal debt;
    private BigDecimal assetsValue;
    private int creditScore = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Score(ScoreEvent scoreEvent) {
        this.userId = scoreEvent.userId();
        this.income = BigDecimal.valueOf(scoreEvent.income());
        this.debt = BigDecimal.valueOf(scoreEvent.debt());
        this.assetsValue = BigDecimal.valueOf(scoreEvent.assetsValue());
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}