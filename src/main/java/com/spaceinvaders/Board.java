package com.spaceinvaders;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.spaceinvaders.sprites.Alien;
import com.spaceinvaders.sprites.Player;
import com.spaceinvaders.sprites.ScorePopup;
import com.spaceinvaders.sprites.Shield;
import com.spaceinvaders.sprites.PowerUp;
import com.spaceinvaders.sprites.Shot;
import com.spaceinvaders.sprites.Star;
import com.spaceinvaders.sprites.Ufo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Board extends JPanel {

    private Dimension d;
    private List<Alien> aliens;
    private List<Shield> shields;
    private List<ScorePopup> scorePopups = new ArrayList<>();
    private List<Star> stars = new ArrayList<>();
    private Player player;
    private Shot shot2;
    private Shot shot;
    private Ufo ufo = new Ufo();
    private PowerUp powerUp = new PowerUp();

    private int direction = -1;
    private int deaths = 0;
    private int score = 0;
    private int highScore = 0;
    private int lives = Commons.INITIAL_LIVES;
    private int level = 1;
    private boolean doubleShot = false;
    private int doubleShotTimer = 0;
    private int difficulty = Commons.DIFFICULTY_NORMAL;

    private boolean inGame = false;
    private boolean onTitle = true;
    private boolean paused = false;
    private String explImg = "src/images/explosion.png";
    private String message = "Game Over";

    private Timer timer;

    public Board() {

        initBoard();
        gameInit();
        loadHighScore();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
        setBackground(Color.black);

        timer = new Timer(Commons.DELAY, new GameCycle());

        for (int i = 0; i < 50; i++) {
            stars.add(new Star(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT));
        }

        gameInit();
    }

    private void gameInit() {

        aliens = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {

                Alien alien = new Alien(Commons.ALIEN_INIT_X + 18 * j,
                        Commons.ALIEN_INIT_Y + 18 * i, i);
                aliens.add(alien);
            }
        }

        player = new Player();
        shot = new Shot();
        shot2 = new Shot();

        shields = new ArrayList<>();
        int spacing = Commons.BOARD_WIDTH / (Commons.NUM_SHIELDS + 1);

        for (int i = 0; i < Commons.NUM_SHIELDS; i++) {

            int shieldX = spacing * (i + 1) - (Commons.SHIELD_WIDTH * 3) / 2;
            shields.add(new Shield(shieldX, Commons.SHIELD_Y,
                    Commons.SHIELD_WIDTH, Commons.SHIELD_HEIGHT));
        }
    }

    private void drawTitle(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

        String title = "SPACE INVADERS";
        Font titleFont = new Font("Helvetica", Font.BOLD, 28);
        FontMetrics fm = this.getFontMetrics(titleFont);

        g.setColor(Color.green);
        g.setFont(titleFont);
        g.drawString(title, (Commons.BOARD_WIDTH - fm.stringWidth(title)) / 2,
                Commons.BOARD_HEIGHT / 3);

        String prompt = "Press ENTER to start";
        Font promptFont = new Font("Helvetica", Font.PLAIN, 14);
        FontMetrics pfm = this.getFontMetrics(promptFont);

        g.setColor(Color.white);
        g.setFont(promptFont);
        g.drawString(prompt, (Commons.BOARD_WIDTH - pfm.stringWidth(prompt)) / 2,
                Commons.BOARD_HEIGHT / 2);

        Font controlsFont = new Font("Helvetica", Font.PLAIN, 11);
        g.setFont(controlsFont);
        g.setColor(Color.gray);

        String[] controls = {
            "LEFT / RIGHT - Move",
            "SPACE - Shoot",
            "P - Pause"
        };

        int controlsY = Commons.BOARD_HEIGHT / 2 + 40;

        for (String line : controls) {
            FontMetrics cfm = this.getFontMetrics(controlsFont);
            g.drawString(line, (Commons.BOARD_WIDTH - cfm.stringWidth(line)) / 2, controlsY);
            controlsY += 16;
        }
    }

    private void drawHud(Graphics g) {

        g.setColor(Color.white);
        g.setFont(new Font("Helvetica", Font.BOLD, 12));
        g.drawString("Score: " + score, 5, 15);
        g.drawString("Hi: " + highScore, 5, 30);
        g.drawString("Level: " + level, Commons.BOARD_WIDTH / 2 - 25, 15);
        g.drawString("Lives: " + lives, Commons.BOARD_WIDTH - 80, 15);
    }

    private void drawPaused(Graphics g) {

        String pauseMsg = "PAUSED";
        Font font = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics fm = this.getFontMetrics(font);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, Commons.BOARD_HEIGHT / 2 - 30, Commons.BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, Commons.BOARD_HEIGHT / 2 - 30, Commons.BOARD_WIDTH - 100, 50);

        g.setFont(font);
        g.drawString(pauseMsg, (Commons.BOARD_WIDTH - fm.stringWidth(pauseMsg)) / 2,
                Commons.BOARD_HEIGHT / 2);
    }

    private void drawAliens(Graphics g) {

        for (Alien alien : aliens) {

            if (alien.isVisible()) {

                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isExploding()) {

                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
                alien.tickExplosion();

                if (!alien.isExploding()) {
                    alien.die();
                }
            } else if (alien.isDying()) {

                alien.die();
            }
        }
    }

    private void drawPlayer(Graphics g) {

        if (player.isVisible()) {

            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {

            lives--;

            if (lives <= 0) {
                player.die();
                inGame = false;
            } else {
                player.setDying(false);
                player.setImage(new ImageIcon("src/images/player.png").getImage());
                player.setX(270);
            }
        }
    }

    private void drawPowerUp(Graphics g) {

        if (powerUp.isActive()) {
            g.drawImage(powerUp.getImage(), powerUp.getX(), powerUp.getY(), this);
        }
    }

    private void drawUfo(Graphics g) {

        if (ufo.isActive()) {
            g.drawImage(ufo.getImage(), ufo.getX(), ufo.getY(), this);
        }
    }

    private void drawStars(Graphics g) {

        for (Star star : stars) {
            star.draw(g);
            star.tick(Commons.BOARD_HEIGHT);
        }
    }

    private void drawScorePopups(Graphics g) {

        Iterator<ScorePopup> it = scorePopups.iterator();

        while (it.hasNext()) {
            ScorePopup popup = it.next();
            popup.draw(g);
            popup.tick();

            if (popup.isExpired()) {
                it.remove();
            }
        }
    }

    private void drawShields(Graphics g) {

        for (Shield shield : shields) {
            shield.draw(g);
        }
    }

    private void drawShot(Graphics g) {

        if (shot.isVisible()) {

            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }

        if (shot2.isVisible()) {

            g.drawImage(shot2.getImage(), shot2.getX(), shot2.getY(), this);
        }
    }

    private void drawBombing(Graphics g) {

        for (Alien a : aliens) {

            Alien.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);

        drawStars(g);

        g.setColor(Color.green);

        if (onTitle) {

            drawTitle(g);

        } else if (inGame) {

            g.drawLine(0, Commons.GROUND,
                    Commons.BOARD_WIDTH, Commons.GROUND);

            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);
            drawShields(g);
            drawUfo(g);
            drawPowerUp(g);
            drawScorePopups(g);
            drawHud(g);

            if (paused) {
                drawPaused(g);
            }

        } else {

            if (timer.isRunning()) {
                timer.stop();
                SoundManager.play("gameover.wav");

                if (score > highScore) {
                    highScore = score;
                    saveHighScore();
                }
            }

            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (Commons.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
                Commons.BOARD_WIDTH / 2);

        Font hint = new Font("Helvetica", Font.PLAIN, 11);
        g.setFont(hint);
        String restartMsg = "Press ENTER to play again";
        FontMetrics hintMetrics = this.getFontMetrics(hint);
        g.drawString(restartMsg, (Commons.BOARD_WIDTH - hintMetrics.stringWidth(restartMsg)) / 2,
                Commons.BOARD_WIDTH / 2 + 20);

        String scoreMsg = "Score: " + score + "    High Score: " + highScore;
        FontMetrics scoreFm = this.getFontMetrics(hint);
        g.drawString(scoreMsg, (Commons.BOARD_WIDTH - scoreFm.stringWidth(scoreMsg)) / 2,
                Commons.BOARD_WIDTH / 2 + 40);
    }

    private void update() {

        if (deaths == Commons.NUMBER_OF_ALIENS_TO_DESTROY) {

            level++;
            deaths = 0;
            direction = -1;
            SoundManager.play("levelclear.wav");

            aliens.clear();

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 6; j++) {

                    Alien alien = new Alien(Commons.ALIEN_INIT_X + 18 * j,
                            Commons.ALIEN_INIT_Y + 18 * i, i);
                    aliens.add(alien);
                }
            }

            for (Shield shield : shields) {
                shield.reset();
            }

            ufo.setActive(false);
        }

        // player
        player.act();

        // shot
        if (shot.isVisible()) {

            int shotX = shot.getX();
            int shotY = shot.getY();

            for (Alien alien : aliens) {

                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX)
                            && shotX <= (alienX + Commons.ALIEN_WIDTH)
                            && shotY >= (alienY)
                            && shotY <= (alienY + Commons.ALIEN_HEIGHT)) {

                        ImageIcon ii = new ImageIcon(explImg);
                        alien.setImage(ii.getImage());
                        alien.startExplosion();
                        alien.setVisible(false);
                        deaths++;
                        int points = Commons.SCORE_BY_TYPE[alien.getType()];
                        score += points;
                        scorePopups.add(new ScorePopup(alienX, alienY, points));
                        SoundManager.play("explosion.wav");

                        if (!powerUp.isActive()) {
                            Random r = new Random();
                            if (r.nextInt(Commons.POWERUP_DROP_CHANCE) == 0) {
                                powerUp.spawn(alienX, alienY);
                            }
                        }

                        shot.die();
                    }
                }
            }

            if (ufo.isActive() && shot.isVisible()) {
                int ufoX = ufo.getX();
                int ufoY = ufo.getY();

                if (shotX >= ufoX
                        && shotX <= ufoX + Commons.UFO_WIDTH
                        && shotY >= ufoY
                        && shotY <= ufoY + Commons.UFO_HEIGHT) {

                    ufo.setActive(false);
                    score += Commons.UFO_SCORE;
                    scorePopups.add(new ScorePopup(ufoX, ufoY, Commons.UFO_SCORE));
                    SoundManager.play("ufo.wav");
                    shot.die();
                }
            }

            for (Shield shield : shields) {
                if (shot.isVisible() && shield.hit(shotX, shotY)) {
                    shot.die();
                }
            }

            int y = shot.getY();
            y -= 4;

            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

        // second shot
        if (shot2.isVisible()) {

            int s2y = shot2.getY();
            s2y -= 4;

            if (s2y < 0) {
                shot2.die();
            } else {
                shot2.setY(s2y);
            }

            for (Alien alien : aliens) {
                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot2.isVisible()) {
                    if (shot2.getX() >= alienX
                            && shot2.getX() <= alienX + Commons.ALIEN_WIDTH
                            && shot2.getY() >= alienY
                            && shot2.getY() <= alienY + Commons.ALIEN_HEIGHT) {

                        ImageIcon ii = new ImageIcon(explImg);
                        alien.setImage(ii.getImage());
                        alien.startExplosion();
                        alien.setVisible(false);
                        deaths++;
                        int points = Commons.SCORE_BY_TYPE[alien.getType()];
                        score += points;
                        scorePopups.add(new ScorePopup(alienX, alienY, points));
                        shot2.die();
                    }
                }
            }
        }

        // aliens

        for (Alien alien : aliens) {

            int x = alien.getX();

            if (x >= Commons.BOARD_WIDTH - Commons.BORDER_RIGHT && direction != -1) {

                direction = -1;

                Iterator<Alien> i1 = aliens.iterator();

                while (i1.hasNext()) {

                    Alien a2 = i1.next();
                    a2.setY(a2.getY() + Commons.GO_DOWN);
                }
            }

            if (x <= Commons.BORDER_LEFT && direction != 1) {

                direction = 1;

                Iterator<Alien> i2 = aliens.iterator();

                while (i2.hasNext()) {

                    Alien a = i2.next();
                    a.setY(a.getY() + Commons.GO_DOWN);
                }
            }
        }

        int remaining = 0;

        for (Alien a : aliens) {
            if (a.isVisible()) {
                remaining++;
            }
        }

        Iterator<Alien> it = aliens.iterator();

        while (it.hasNext()) {

            Alien alien = it.next();

            if (alien.isVisible()) {

                int y = alien.getY();

                if (y > Commons.GROUND - Commons.ALIEN_HEIGHT) {
                    inGame = false;
                    message = "Invasion!";
                }

                int baseSpeed = 1 + (level - 1) * Commons.SPEED_INCREASE;
                int boost = (Commons.NUMBER_OF_ALIENS_TO_DESTROY - remaining) / 6;
                int speed = direction * (baseSpeed + boost);
                alien.act(speed);
            }
        }

        // double shot timer
        if (doubleShot) {
            doubleShotTimer--;

            if (doubleShotTimer <= 0) {
                doubleShot = false;
            }
        }

        // ufo
        Random generator = new Random();

        if (!ufo.isActive() && generator.nextInt(Commons.UFO_SPAWN_CHANCE) == 0) {
            ufo.spawn();
        }

        if (ufo.isActive()) {
            ufo.act();

            if (ufo.getX() > Commons.BOARD_WIDTH) {
                ufo.setActive(false);
            }
        }

        // power-up
        if (powerUp.isActive()) {
            powerUp.act();

            int puX = powerUp.getX();
            int puY = powerUp.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (puX >= playerX && puX <= playerX + Commons.PLAYER_WIDTH
                    && puY >= playerY && puY <= playerY + Commons.PLAYER_HEIGHT) {

                powerUp.setActive(false);
                doubleShot = true;
                doubleShotTimer = Commons.POWERUP_DURATION;
                SoundManager.play("powerup.wav");
            }

            if (puY > Commons.GROUND) {
                powerUp.setActive(false);
            }
        }

        // bombs

        for (Alien alien : aliens) {

            int bombChance = Math.max(Commons.MIN_BOMB_CHANCE,
                    Commons.BASE_BOMB_CHANCE - (level - 1));
            int shot = generator.nextInt(bombChance);
            Alien.Bomb bomb = alien.getBomb();

            if (shot == 0 && alien.isVisible() && bomb.isDestroyed()) {

                bomb.setDestroyed(false);
                bomb.setX(alien.getX());
                bomb.setY(alien.getY());
            }

            int bombX = bomb.getX();
            int bombY = bomb.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if (player.isVisible() && !bomb.isDestroyed()) {

                if (bombX >= (playerX)
                        && bombX <= (playerX + Commons.PLAYER_WIDTH)
                        && bombY >= (playerY)
                        && bombY <= (playerY + Commons.PLAYER_HEIGHT)) {

                    ImageIcon ii = new ImageIcon(explImg);
                    player.setImage(ii.getImage());
                    player.setDying(true);
                    bomb.setDestroyed(true);
                    SoundManager.play("death.wav");
                }
            }

            if (!bomb.isDestroyed()) {

                for (Shield shield : shields) {
                    if (shield.hit(bomb.getX(), bomb.getY())) {
                        bomb.setDestroyed(true);
                        break;
                    }
                }
            }

            if (!bomb.isDestroyed()) {

                bomb.setY(bomb.getY() + 1);

                if (bomb.getY() >= Commons.GROUND - Commons.BOMB_HEIGHT) {

                    bomb.setDestroyed(true);
                }
            }
        }
    }

    private void doGameCycle() {

        if (!paused) {
            update();
        }
        repaint();
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {

                if (inGame) {

                    if (!shot.isVisible()) {

                        shot = new Shot(x, y);

                        if (doubleShot && !shot2.isVisible()) {
                            shot2 = new Shot(x + 10, y);
                        }

                        SoundManager.play("shoot.wav");
                    }
                }
            }

            if (key == KeyEvent.VK_P && inGame) {

                paused = !paused;
            }

            if (key == KeyEvent.VK_ENTER) {

                if (onTitle) {
                    onTitle = false;
                    inGame = true;
                    timer.start();
                } else if (!inGame) {
                    restartGame();
                }
            }
        }
    }

    private void saveHighScore() {

        try {
            FileWriter writer = new FileWriter("highscore.dat");
            writer.write(String.valueOf(highScore));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadHighScore() {

        try {
            File file = new File("highscore.dat");

            if (file.exists()) {
                Scanner scanner = new Scanner(file);

                if (scanner.hasNextInt()) {
                    highScore = scanner.nextInt();
                }

                scanner.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void restartGame() {

        deaths = 0;
        score = 0;
        lives = Commons.INITIAL_LIVES;
        level = 1;
        direction = -1;
        inGame = true;
        onTitle = false;
        paused = false;
        message = "Game Over";

        for (Shield shield : shields) {
            shield.reset();
        }

        ufo.setActive(false);
        powerUp.setActive(false);
        doubleShot = false;
        doubleShotTimer = 0;

        gameInit();
        timer.restart();
    }
}
