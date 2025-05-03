package scripts;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {

	public static void playSound(String filePath) {
        new Thread(() -> {
            try {
                File soundFile = new File(filePath);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                
                clip.start(); // Play the sound without blocking the main thread
                
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.err.println("Error playing sound: " + e.getMessage());
            }
        }).start();
    }


    public static void main(String[] args) {
        playSound("src/Sounds/PacManEating.wav"); // Adjust path to your .wav file
    }
}