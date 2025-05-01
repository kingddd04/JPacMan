package scripts;

/**
 * Defines the core actions that any character in the game must implement.Like being moved or teleported.
 * 
 * @author Davide Di Stefano
 * @version 1.0.0
 * @since 1.0.0
 */
public interface CharacterActions {

    /**
     * Checks for collisions and updates the character's position on the game board.
     * Implementations handle interactions with different tile types like food or fruits for pac-man
     *
     * @param gameBoard a 2D array representing the game board where the character is moved.
     * 
     * @see PacMan#checkCollisionAndMove(String[][]) implementation for PacMan
     * @see Ghost#checkCollisionAndMove(String[][]) implementation for Ghost
     */
    public void checkCollisionAndMove(String[][] gameBoard);

    /**
     * Teleports the character to a specified set of coordinates on the game board.
     * Useful to reset the character position or teleport it with portals
     *
     * @param gameBoard a 2D array representing the game board.
     * @param targetCoordinatesXY an integer array (x, y) specifying the destination coordinates.
     * 
     * @see PacMan#teleportAt(String[][], int[]) implementation for PacMan
     * @see Ghost#teleportAt(String[][], int[]) implementation for Ghost
     */
    public void teleportAt(String[][] gameBoard, int[] targetCoordinatesXY);
}