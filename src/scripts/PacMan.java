package scripts;

/**
 * The class {@code PacMan} create and manage a PacMan object and its attributes
 * like position coordinates, its current direction, its spawn coordinates 
 * the class represent the player controlled character,
 * this character must avoid ghosts and eat all food to win.This class manages its movements
 * and teleport 
 * 
 * @see Character
 * @see Game#keyPressed(java.awt.event.KeyEvent)
 * 
 * @author Davide Di Stefano
 * @version 1.0.0
 * @since 1.0.0
 */
public class PacMan extends Character{
	
	/**
	 * This builder creates a PacMan object taking in :
	 * 
	 * @param coordinatesXY the current pacMan coordinates at start this value is given to {@code defaultCoordinatesXY}
	 * while {@code coordinatesXY} gets updated when pacman moves 
	 * @param currentDirectionXY an integer array specifying the pac-man movement direction (x, y) given by the user.
	 */
	public PacMan(int[] currentCoordinatesXY, int[] currentDirectionXY) {
		super(currentCoordinatesXY, currentDirectionXY);
	}
	
	/**
	 * This method is used to moved pac-man across the map based on the input of user and collisions with walls
	 * 
	 * @see Game#keyPressed(java.awt.event.KeyEvent) modify the direction from user input and validate it
	 * @see PacMan#verifyDirectionUpdate(String[][], int[]) validate the direction of the movement to check if path is free makes the movement feel more fluid
	 */
	@Override
	public void checkCollisionAndMove(String[][] gameBoard) {
		
		// Square good events processing
		String targetTileContent = gameBoard[currentCoordinatesXY[1]+currentDirectionXY[1]][currentCoordinatesXY[0]+currentDirectionXY[0]];
		if(targetTileContent.contains(".")){Game.foodsScoreIncrease();}
		if(targetTileContent.contains("x")){Game.increaseInvincibilityTime();}
		if(targetTileContent.contains("f")){Game.increaseLifes();}
		
		// Pac-Man movement
		if(!targetTileContent.equals("W") && !targetTileContent.equals("0") && !targetTileContent.equals("O")) {
			gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] = gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]].replace("P", "");
			gameBoard[currentCoordinatesXY[1]+currentDirectionXY[1]][currentCoordinatesXY[0]+currentDirectionXY[0]] = "P";
			if(gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]].length() == 0){
				gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] = " ";
			}
		currentCoordinatesXY = new int[] {currentCoordinatesXY[0] + currentDirectionXY[0], currentCoordinatesXY[1] + currentDirectionXY[1]};

		}
	}
	
	/**
	 * Teleports pac-man to a position. Is used by the portals at sides of the map or to reset its position after a victory or a defeat
	 */
	@Override
	public void teleportAt(String[][] gameBoard, int[] targetCoordinatesXY) {
		
		//Pac-man movement
		gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] = gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]].replace("P", "");
		if(gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]].length() == 0){
			gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] = " ";
		}
		currentCoordinatesXY = targetCoordinatesXY;
		gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] = "P";
	}
	

	/**
	 * This method is useful to verify if the position where pac-man will be moved by the user input is not a wall
	 * this function does not handles fully the pac-man collision for this is needed the method {@code checkCollisionAndMove}
	 *  
	 * @param gameBoard this 2d Strings array represents the current state of the screen representing walls, ghosts, pac-man, food and else
	 * @param inputDirectionXY this is the desired direction chosen by user to be tested
	 * 
	 * @see #checkCollisionAndMove pacman movement
	 * @see Game#keyPressed(java.awt.event.KeyEvent) modify the direction from user input and validate it
	 */
	public void verifyDirectionUpdate(String[][] gameBoard, int[] inputDirectionXY) {
		if(!gameBoard[currentCoordinatesXY[1]+inputDirectionXY[1]][currentCoordinatesXY[0]+inputDirectionXY[0]].equals("W")) {
			updateDirection(inputDirectionXY);	
		}
		
	}	
}
