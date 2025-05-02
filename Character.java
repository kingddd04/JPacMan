package scripts;

/**
 * This abstract class is the model to create the two classes of characters of the game
 * a class for pac-man and one for the ghosts. 
 * This class is for their common variables like starting position,current position 
 * and current direction, this class also also handles almost all their getter and setter methods.
 * 
 * @see Ghost subclass for ghosts
 * @see Pacman subclass for pacman
 * 
 * @author Davide Di Stefano
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class Character  implements CharacterActions{
	protected int[] currentCoordinatesXY;
	protected int[] startingCoordinatesXY;
	protected int[] currentDirectionXY;
	
	/**
	 * The constructor for Character class is the model to create {@code PacMan} and {@code Ghost}
	 * and relative objects, it handles the starting position of the character, their current position
	 * and the current direction.
	 * 
	 * @param currentCoordinatesXY the current coordinates of the Character object, ghost or Pac-man;
	 * code{@code currentCoordinatesXY} is used to initialize the variable {@code startingCoordinatesXY}
	 * tasked with preserving the starting position of the character.
	 * @param currentDirectionXY the current coordinates of the Character object, ghost or Pac-man.
	 */
	public Character(int[] currentCoordinatesXY, int[] currentDirectionXY) {
		this.currentCoordinatesXY = currentCoordinatesXY;
		this.startingCoordinatesXY = currentCoordinatesXY;
		this.currentDirectionXY = currentDirectionXY;
		
	}
	/**
	 * Updates the current coordinates of the Character.
	 *
	 * @param newCoordinatesXY an array representing the new coordinates (x, y)  to set for the Character instance.
	 */
	public void setCoordinatesXY(int[] newCoordinatesXY) {
	    currentCoordinatesXY = newCoordinatesXY;
	}

	/**
	 * Retrieves the current position of the Character.
	 *
	 * @return an integer array representing the current (x, y) coordinates
	 *         of the Character.
	 */
	public int[] getCoordinatesXY() {
	    return currentCoordinatesXY;
	}

	/**
	 * Retrieves the initial position of the Character, 
	 * representing its default spawn point.
	 *
	 * @return an integer array containing the default (x, y) coordinates at which the Character was originally positioned.
	 */
	public int[] getDefaultCoordinatesXY() {
	    return startingCoordinatesXY;
	}

	/**
	 * Updates the movement direction of the character.
	 * This method applies a new direction vector to adjust the character's path.
	 *
	 * @param inputDirectionXY an integer array representing the new movement direction (x, y).
	 */
	public void updateDirection(int[] inputDirectionXY) {
	    currentDirectionXY = inputDirectionXY;
	}

	/**
	 * Retrieves the current movement direction of the character.
	 *
	 * @return an integer array representing the current direction (x, y).
	 */
	public int[] getcurrentDirectionXY() {
	    return currentDirectionXY;
	}
}
