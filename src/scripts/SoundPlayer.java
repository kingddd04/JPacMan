package scripts;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;

public class SoundPlayer {

    public static void playSound(String filePath) {
        new Thread(() -> {
            try {
                InputStream soundFile = SoundPlayer.class.getResourceAsStream(filePath);
                if (soundFile == null) {
                    System.err.println("Sound file not found: " + filePath);
                    return;
                }

                BufferedInputStream bufferedStream = new BufferedInputStream(soundFile);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedStream);

                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();

                // Allow the sound to finish playing
                while (!clip.isRunning()) Thread.sleep(100);
                while (clip.isRunning()) Thread.sleep(100);

                clip.close();
                audioStream.close();
                soundFile.close();

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}