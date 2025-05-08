package scripts;

import java.util.*;
import java.util.Random;

/**
 * Represents a ghost character in the game, the class handles its movement
 * in the board the ghost can move choosing  randomly a new path from the ones Available 
 * and can be teleported when defeated
 * 
 * @author Davide Di Stefano
 * @version 1.0.0
 * @since 1.0.0
 */
public class Ghost extends Character{
	private String ghostColorLetter;
	private Random random;
	
    /**
     * Initializes a Ghost instance with its position, direction, and unique color identifier.
     *
     * @param currentCoordinatesXY an integer array representing the ghost's starting (x, y) position.
     * @param currentDirectionXY an integer array specifying the ghost's initial movement direction (x, y).
     * @param ghostColorLetter a string representing the ghost's unique color for identification.
     * 
     * @see Character
     */
	public Ghost(int[] currentCoordinatesXY, int[] currentDirectionXY, String ghostColorLetter) {
		super(currentCoordinatesXY, currentDirectionXY);
		this.ghostColorLetter = ghostColorLetter;
		this.random = new Random();
	}
	
	/**
	 * Moves the ghost in a random valid direction at each crossroad manages ghost A.I(if so can be described)
	 * 
	 * @see CharacterActions#checkCollisionAndMove(String[][])
	 */
	@Override
	public void checkCollisionAndMove(String[][] gameBoard) {
		
		// Ghost possible directions based on the current direction
		int[][] possibleDirections = null;
		if(Arrays.equals(currentDirectionXY, new int[]{0, -1})) {
			possibleDirections = new int[][] {{0, -1}, {-1, 0 }, {1, 0}};
		}
		else if(Arrays.equals(currentDirectionXY, new int[]{0, 1})) {
			possibleDirections = new int[][] {{0, 1}, {1, 0}, {-1, 0}};
		}
		else if(Arrays.equals(currentDirectionXY, new int[]{-1, 0})) {
			possibleDirections = new int[][] {{-1, 0}, {0, -1}, {0, 1}};
		}
		else if(Arrays.equals(currentDirectionXY, new int[]{1,0})) {
	        possibleDirections = new int[][] {{1, 0}, { 0, -1}, {0, 1}};
		}
		ArrayList<int[]> availableDirections = new ArrayList<>();
		
		// Ghost valid positions not occupied by a wall
		for(int[] direction : possibleDirections) {
			if(!gameBoard[currentCoordinatesXY[1]+direction[1]][currentCoordinatesXY[0]+direction[0]].equals("W")){
				availableDirections.add(direction);
			}
		}
		
		// choice of a random direction between all the valid ones
		int[] chosenDirection = availableDirections.get(random.nextInt(availableDirections.size()));
		currentDirectionXY = chosenDirection;

		// Ghost movement
		gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] = gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]].replace(ghostColorLetter, "");
		gameBoard[currentCoordinatesXY[1]+currentDirectionXY[1]][currentCoordinatesXY[0]+currentDirectionXY[0]] = ghostColorLetter + gameBoard[currentCoordinatesXY[1]+currentDirectionXY[1]][currentCoordinatesXY[0]+currentDirectionXY[0]];
		
		if(gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]].length() == 0){
			gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] = " ";
		}
		currentCoordinatesXY = new int[] {currentCoordinatesXY[0] + currentDirectionXY[0], currentCoordinatesXY[1] + currentDirectionXY[1]};

	}
	
	/**
	 * Teleports a ghost to a position. Is used by the portals at sides of the map or to reset the ghost after a victory or a defeat
	 */
	@Override
	public void teleportAt(String[][] gameBoard, int[] targetCoordinatesXY) {
		
		//removing GhostIcon
		removeGhostIcon(gameBoard);
		
		// Coordinates Update
		gameBoard[targetCoordinatesXY[1]][targetCoordinatesXY[0]] = ghostColorLetter;
		currentCoordinatesXY = targetCoordinatesXY;
		
	}
	
	public void removeGhostIcon(String[][] gameBoard) {
		gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] = gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]].replace(ghostColorLetter, "");
		if(gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]].length() == 0){
			gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] = " ";
		}
		
	}

	/**
	 * Gets the letter associated with the ghost's name.
	 *
	 * @return a string representing the ghost's name letter
	 */
	public String getGhostColorLetter() {
		return ghostColorLetter;
	}
}
