package scripts;

import java.awt.Image;

import javax.swing.*;

/**
 * The class {@code} Main} boot the class {@code Game}.
 * Also set up basic properties of the game window.
 * @see Game
 * 
 * @author Davide Di Stefano
 * @version 1.0.0
 * @since 1.0.0
 */
public class Main {
	/**
	 * Entry point of the Pac-Man game.
	 * @param args Command line arguments (optional)
	 */
	public static void main(String[] args) {
		

		Game newGame = new Game();
		newGame.setVisible(true);
		newGame.setSize(441,441);
		newGame.setResizable(false);
		newGame.setLocationRelativeTo(null);
		newGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		ImageIcon localPacManIcon = new ImageIcon(SpritesLoader.class.getResource("/Sprites/pacmanRight.png"));
		Image gameIcon = localPacManIcon.getImage();
		newGame.setIconImage(gameIcon);
		}

}
