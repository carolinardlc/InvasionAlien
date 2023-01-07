package com.spaceinvaders.sprites;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class ScorePopup {

    private int x;
    private int y;
    private int score;
    private int framesLeft;
    private static final int DURATION = 30;

    public ScorePopup(int x, int y, int score) {

        this.x = x;
        this.y = y;
        this.score = score;
        this.framesLeft = DURATION;
    }

    public void tick() {

        y--;
        framesLeft--;
    }

    public boolean isExpired() {

        return framesLeft <= 0;
    }

    public void draw(Graphics g) {

        float alpha = (float) framesLeft / DURATION;
        int alphaInt = Math.max(0, Math.min(255, (int) (alpha * 255)));

        g.setColor(new Color(255, 255, 0, alphaInt));
        g.setFont(new Font("Helvetica", Font.BOLD, 10));
        g.drawString("+" + score, x, y);
    }
}
