package com.spaceinvaders;

public class ScoreEntry {

    private String initials;
    private int score;

    public ScoreEntry(String initials, int score) {

        this.initials = initials;
        this.score = score;
    }

    public String getInitials() {

        return initials;
    }

    public int getScore() {

        return score;
    }

    public String toString() {

        return initials + " " + score;
    }
}
