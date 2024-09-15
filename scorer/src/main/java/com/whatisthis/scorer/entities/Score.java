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
    private double assetsValue;
    private int creditScore = 0;

    public Score() {
    }

    public Score(ScoreEvent scoreEvent) {
        this.userId = scoreEvent.userId();
        this.income = scoreEvent.income();
        this.debt = scoreEvent.debt();
        this.assetsValue = scoreEvent.assetsValue();
    }

    public UUID getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }

    public double getAssetsValue() {
        return assetsValue;
    }

    public void setAssetsValue(double assetsValue) {
        this.assetsValue = assetsValue;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }
}