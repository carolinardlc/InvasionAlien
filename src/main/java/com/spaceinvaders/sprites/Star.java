package com.spaceinvaders.sprites;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Star {

    private int x;
    private int y;
    private int speed;
    private int brightness;
    private static final Random random = new Random();

    public Star(int maxWidth, int maxHeight) {

        this.x = random.nextInt(maxWidth);
        this.y = random.nextInt(maxHeight);
        this.speed = 1 + random.nextInt(2);
        this.brightness = 100 + random.nextInt(156);
    }

    public void tick(int maxHeight) {

        y += speed;

        if (y > maxHeight) {
            y = 0;
            brightness = 100 + random.nextInt(156);
        }
    }

    public void draw(Graphics g) {

        g.setColor(new Color(brightness, brightness, brightness));
        g.fillRect(x, y, 1, 1);
    }
}
