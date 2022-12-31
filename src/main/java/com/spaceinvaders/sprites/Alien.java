package com.spaceinvaders.sprites;

import javax.swing.ImageIcon;

public class Alien extends Sprite {

    private Bomb bomb;
    private int type;

    public Alien(int x, int y, int type) {

        this.type = type;
        initAlien(x, y);
    }

    public int getType() {

        return type;
    }

    private void initAlien(int x, int y) {

        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);

        String[] alienImages = {
            "src/images/alien3.png",
            "src/images/alien2.png",
            "src/images/alien1.png",
            "src/images/alien.png"
        };
        String alienImg = alienImages[type];
        ImageIcon ii = new ImageIcon(alienImg);

        setImage(ii.getImage());
    }

    public void act(int direction) {

        this.x += direction;
    }

    public Bomb getBomb() {

        return bomb;
    }

    public class Bomb extends Sprite {

        private boolean destroyed;

        public Bomb(int x, int y) {

            initBomb(x, y);
        }

        private void initBomb(int x, int y) {

            setDestroyed(true);

            this.x = x;
            this.y = y;

            String bombImg = "src/images/bomb.png";
            ImageIcon ii = new ImageIcon(bombImg);
            setImage(ii.getImage());
        }

        public void setDestroyed(boolean destroyed) {

            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {

            return destroyed;
        }
    }
}
