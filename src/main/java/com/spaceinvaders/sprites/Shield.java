package com.spaceinvaders.sprites;

import java.awt.Color;
import java.awt.Graphics;

public class Shield {

    private int x;
    private int y;
    private int width;
    private int height;
    private int[][] blocks;

    public Shield(int x, int y, int width, int height) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.blocks = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                blocks[i][j] = 1;
            }
        }
    }

    public void draw(Graphics g) {

        g.setColor(Color.green);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if (blocks[i][j] == 1) {
                    g.fillRect(x + j * 3, y + i * 3, 3, 3);
                }
            }
        }
    }

    public boolean hit(int hitX, int hitY) {

        int col = (hitX - x) / 3;
        int row = (hitY - y) / 3;

        if (row >= 0 && row < height && col >= 0 && col < width) {

            if (blocks[row][col] == 1) {
                blocks[row][col] = 0;
                return true;
            }
        }

        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPixelWidth() {
        return width * 3;
    }

    public int getPixelHeight() {
        return height * 3;
    }

    public void reset() {

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                blocks[i][j] = 1;
            }
        }
    }
}
