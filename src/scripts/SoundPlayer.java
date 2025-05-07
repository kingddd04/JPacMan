package scripts;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * A utility class for playing sound effects in the game.
 * The {@code SoundPlayer} class provides a method to play audio files in a separate thread.
 * This ensures that the game's main thread remains responsive while sounds are played.
 * 
 * <p>Note: The audio files must be accessible as resources in the application's classpath.
 * 
 * @author Davide Di Stefano
 * @version 1.0.0
 * @since 1.0.0
 */
public class SoundPlayer {

    /**
     * Plays a sound effect from the specified file path.
     * The method runs in a separate thread to avoid blocking the main game loop.
     * 
     * @param filePath The path to the audio file to be played, relative to the application's classpath.
     *                 For example, {@code "/Sounds/pacManEating.wav"}.
     * 
     * <p>Audio files must be in a format supported by {@code javax.sound.sampled.AudioSystem},
     * such as WAV.
     * 
     * @throws IllegalArgumentException If the audio file is not found at the specified path.
     * @throws RuntimeException If an error occurs while loading or playing the audio file.
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

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
