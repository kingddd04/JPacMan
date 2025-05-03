package scripts;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class UserInput implements KeyListener {
	PacMan pacMan;
	String[][] localgameBoard;

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
    
    public void setLocalGameBoard(String[][] gameBoard) {
    	localgameBoard = gameBoard;
	}
}