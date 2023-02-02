package com.spaceinvaders.sprites;

import javax.swing.ImageIcon;

public class PowerUp extends Sprite {

    private boolean active;

    public PowerUp() {

        String img = "src/images/powerup.png";
        ImageIcon ii = new ImageIcon(img);
        setImage(ii.getImage());

        active = false;
    }

    public void spawn(int x, int y) {

        this.x = x;
        this.y = y;
        active = true;
    }

    public void act() {

        if (active) {
            y += 1;
        }
    }

    public boolean isActive() {

        return active;
    }

    public void setActive(boolean active) {

        this.active = active;
    }
}
