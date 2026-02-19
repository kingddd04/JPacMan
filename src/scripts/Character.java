package scripts;

/**
 * This abstract class serves as the base model for creating the two character types
 * in the game: {@link PacMan} and {@link Ghost}. It defines their shared attributes,
 * such as starting position, current position, and current movement direction.
 * <p>
 * The class also provides most of the getter and setter methods used by both
 * character types, ensuring consistent behavior across all game entities.
 * </p>
 *
 * @see Ghost  for the ghost implementation
 * @see PacMan for the Pac-Man implementation
 *
 * @author Davide Di Stefano
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class Character implements CharacterActions {

    /**
     * The current (x, y) coordinates of the character on the game board.
     */
    protected int[] currentCoordinatesXY;

    /**
     * The initial (x, y) coordinates where the character spawns at the start of the game.
     * This value is used to reset the character to its default position.
     */
    protected int[] startingCoordinatesXY;

    /**
     * The current movement direction of the character, represented as a vector (x, y).
     */
    protected int[] currentDirectionXY;

    /**
     * Constructs a new Character instance, used as the base model for both
     * {@code PacMan} and {@code Ghost}. It initializes the starting position,
     * current position, and movement direction of the character.
     *
     * @param currentCoordinatesXY the current coordinates of the character (x, y);
     *                             this value is also used to initialize
     *                             {@code startingCoordinatesXY}, which stores
     *                             the default spawn position.
     * @param currentDirectionXY   the initial movement direction of the character (x, y).
     */
    public Character(int[] currentCoordinatesXY, int[] currentDirectionXY) {
        this.currentCoordinatesXY = currentCoordinatesXY;
        this.startingCoordinatesXY = currentCoordinatesXY;
        this.currentDirectionXY = currentDirectionXY;
    }

    /**
     * Updates the current coordinates of the character.
     *
     * @param newCoordinatesXY an array representing the new (x, y) coordinates.
     */
    public void setCoordinatesXY(int[] newCoordinatesXY) {
        currentCoordinatesXY = newCoordinatesXY;
    }

    /**
     * Retrieves the current position of the character.
     *
     * @return an integer array representing the current (x, y) coordinates.
     */
    public int[] getCoordinatesXY() {
        return currentCoordinatesXY;
    }

    /**
     * Retrieves the initial spawn position of the character.
     *
     * @return an integer array containing the default (x, y) coordinates.
     */
    public int[] getDefaultCoordinatesXY() {
        return startingCoordinatesXY;
    }

    /**
     * Updates the movement direction of the character.
     *
     * @param inputDirectionXY an integer array representing the new direction vector (x, y).
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
