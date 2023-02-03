package com.spaceinvaders;

public interface Commons {

    int BOARD_WIDTH = 358;
    int BOARD_HEIGHT = 350;
    int BORDER_RIGHT = 30;
    int BORDER_LEFT = 5;

    int GROUND = 290;
    int BOMB_HEIGHT = 5;

    int ALIEN_HEIGHT = 12;
    int ALIEN_WIDTH = 12;
    int ALIEN_INIT_X = 150;
    int ALIEN_INIT_Y = 5;

    int GO_DOWN = 15;
    int NUMBER_OF_ALIENS_TO_DESTROY = 24;
    int CHANCE = 5;
    int DELAY = 17;
    int PLAYER_WIDTH = 15;
    int PLAYER_HEIGHT = 10;

    int SCORE_PER_ALIEN = 100;
    int[] SCORE_BY_TYPE = {400, 300, 200, 100};
    int INITIAL_LIVES = 3;
    int SPEED_INCREASE = 1;

    int NUM_SHIELDS = 4;
    int SHIELD_WIDTH = 10;
    int SHIELD_HEIGHT = 6;
    int SHIELD_Y = 250;

    int BASE_BOMB_CHANCE = 15;
    int MIN_BOMB_CHANCE = 5;

    int UFO_SCORE = 500;
    int UFO_SPAWN_CHANCE = 500;
    int UFO_WIDTH = 12;
    int UFO_HEIGHT = 12;

    int POWERUP_DROP_CHANCE = 10;
    int POWERUP_WIDTH = 10;
    int POWERUP_HEIGHT = 10;
    int POWERUP_DURATION = 300;
}
