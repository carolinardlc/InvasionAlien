package com.spaceinvaders;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundManager {

    public static void play(String filename) {

        try {
            File file = new File("src/sounds/" + filename);

            if (file.exists()) {
                AudioInputStream audio = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audio);
                clip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
