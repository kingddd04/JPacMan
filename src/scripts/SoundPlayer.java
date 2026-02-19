package scripts;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * Utility class responsible for loading and playing sound effects in the game.
 * 
 * The {@code SoundPlayer} provides a single static method that plays audio files
 * packaged within the application's resources. Sounds are played asynchronously
 * in a separate thread to avoid blocking the main game loop.
 *
 * @author Davide Di Stefano
 * @version 1.0.0
 * @since 1.0.0
 */
public class SoundPlayer {

    /**
     * Plays a sound effect from the given file path.
     * <p>
     * The method runs asynchronously in a separate thread, allowing the game to
     * continue running without interruption while the sound is being played.
     * </p>
     *
     * @param filePath the path to the sound file within the application's resources.
     *                 For example: {@code "/Sounds/pacman_chomp.wav"}.
     */
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

            } catch (UnsupportedAudioFileException | IOException |
                     LineUnavailableException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
