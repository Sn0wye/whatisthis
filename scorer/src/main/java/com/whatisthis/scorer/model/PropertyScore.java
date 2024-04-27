package com.whatisthis.scorer.model;

// Property Ownership Score:
// Owns Expensive Property: 300 (Value above $500,000)
// Owns Medium-Priced Property: 150 (Value between $200,000 and $500,000)
// Low-Priced / Does Not Own Property: 0 (Below $200,000)
public class PropertyScore {
    private int score;

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public PropertyScore(int score) {
        this.score = score;
    }

    public static PropertyScore HIGH() {
            return new PropertyScore(300);
    }

    public static PropertyScore MEDIUM() {
        return new PropertyScore(150);
    }

    public static PropertyScore LOW() {
        return new PropertyScore(0);
    }
}
