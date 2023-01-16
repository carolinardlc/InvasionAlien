package com.spaceinvaders.sprites;

import javax.swing.ImageIcon;

public class Ufo extends Sprite {

    private boolean active;
    private int speed;

    public Ufo() {

        String ufoImg = "src/images/ufo.png";
        ImageIcon ii = new ImageIcon(ufoImg);
        setImage(ii.getImage());

        active = false;
        speed = 2;
    }

    public void spawn() {

        active = true;
        x = 0;
        y = 5;
    }

    public void act() {

        if (active) {
            x += speed;
        }
    }

    public boolean isActive() {

        return active;
    }

    public void setActive(boolean active) {

        this.active = active;
    }
}
