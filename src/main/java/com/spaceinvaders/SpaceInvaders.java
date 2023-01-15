package com.spaceinvaders;

import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class SpaceInvaders extends JFrame {

    public SpaceInvaders() {

        initUI();
    }

    private void initUI() {

        add(new Board());

        setTitle("Space Invaders");
        setSize(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("src/images/icon.png").getImage());
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            SpaceInvaders ex = new SpaceInvaders();
            ex.setVisible(true);
        });
    }
}
