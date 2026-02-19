package scripts;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/**
 * This Class handles the input from keys by the user gets added to the gui by the game class
 * @author Davide Di Stefano
 * @version 1.2.0
 * @since 1.0.0
 */
public class UserInput implements KeyListener {
	/**
	 * Reference to the Pac-Man instance whose direction will be updated based on user input. 
	 */
	PacMan pacMan;
	/** Local reference to the current game board matrix. 
	 * This is passed to Pac-Man when validating direction changes. 
	 */
	String[][] localgameBoard;

	/**
	 * Creates A new userImput project useful to execute methods on it to move pacman
	 * 
	 * @param pacMan a reference to the pacMan object useful to apply on it methods
	 */
	public UserInput(PacMan pacMan){
		this.pacMan = pacMan;
	}
	

	/**
	 * Handles key press events to update Pac-Man's movement direction.
	 * Depending on the arrow key pressed, the direction is set accordingly.
	 *
	 * @param e The KeyEvent that contains information about the key press.
	 *
	 * @see PacMan#verifyDirectionUpdate(String[][], int[])  
	 * @see GUI 
	 * @see Game
	 */
    @Override
    public void keyPressed(KeyEvent e) {
        int[] pacmanInputDirectionXY = null;

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacmanInputDirectionXY = new int[]{0, -1};
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacmanInputDirectionXY = new int[]{0, 1};
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacmanInputDirectionXY = new int[]{-1, 0};
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacmanInputDirectionXY = new int[]{1, 0};
        }
        pacMan.verifyDirectionUpdate(localgameBoard, pacmanInputDirectionXY); 
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not needed
    }
    
    /**
     * This method is useful to store locally the game array to pass as argument to  
     * {@code pacMan.verifyDirectionUpdate()} variable is passed by reference
     * 
     * @param gameBoard this 2d array symbolises the current situation in the game 
     */
    public void setLocalGameBoard(String[][] gameBoard) {
    	localgameBoard = gameBoard;
	}
}