package scripts;

/**
 * Represents the Pac-Man character controlled by the player.
 * The {@code PacMan} class manages Pac-Man's attributes, such as position coordinates,
 * movement direction, and spawn coordinates. Pac-Man must avoid ghosts and eat all food to win.
 * This class handles movement, collision detection, and teleportation logic.
 * 
 * @see Character
 * @see Game#keyPressed(java.awt.event.KeyEvent)
 * 
 * @author Davide Di Stefano
 * @version 1.2.0
 * @since 1.0.0
 */
public class PacMan extends Character {

    /**
     * Constructs a new Pac-Man object.
     * 
     * @param currentCoordinatesXY The initial coordinates of Pac-Man. This value is used to set
     *                             {@code defaultCoordinatesXY}, while {@code coordinatesXY}
     *                             gets updated as Pac-Man moves.
     * @param currentDirectionXY   An integer array specifying Pac-Man's initial movement
     *                             direction (x, y) based on user input.
     */
    public PacMan(int[] currentCoordinatesXY, int[] currentDirectionXY) {
        super(currentCoordinatesXY, currentDirectionXY);
    }

    /**
     * Processes Pac-Man's movement across the game board, based on user input and collision
     * detection. This method handles interactions with walls, food, power-ups, and other game
     * elements.
     * 
     * @param gameBoard A 2D array representing the current state of the game board, including
     *                  walls, food, ghosts, and other elements.
     * 
     * @see Game#keyPressed(java.awt.event.KeyEvent) Updates the movement direction based on user input.
     * @see PacMan#verifyDirectionUpdate(String[][], int[]) Validates the movement direction
     *      and ensures smooth gameplay.
     */
    @Override
    public void checkCollisionAndMove(String[][] gameBoard) {
        // Square good events processing
        String targetTileContent = gameBoard[currentCoordinatesXY[1] + currentDirectionXY[1]][currentCoordinatesXY[0] + currentDirectionXY[0]];
        if (targetTileContent.contains(".")) {
            Game.foodsScoreIncrease();
            SoundPlayer.playSound("/Sounds/pacManEating.wav");
        }
        if (targetTileContent.contains("x")) {
            Game.increaseInvincibilityTime();
            SoundPlayer.playSound("/Sounds/powerUpEaten.wav");
        }
        if (targetTileContent.contains("f")) {
            Game.increaseLifes();
            SoundPlayer.playSound("/Sounds/fruitEaten.wav");
        }

        // Pac-Man movement
        if (!targetTileContent.equals("W") && !targetTileContent.equals("0") && !targetTileContent.equals("O")) {
            gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] = gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]].replace("P", "");
            gameBoard[currentCoordinatesXY[1] + currentDirectionXY[1]][currentCoordinatesXY[0] + currentDirectionXY[0]] = "P";
            if (gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]].length() == 0) {
                gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] = " ";
            }
            currentCoordinatesXY = new int[] { currentCoordinatesXY[0] + currentDirectionXY[0], currentCoordinatesXY[1] + currentDirectionXY[1] };
        }
    }

    /**
     * Teleports Pac-Man to a specified position on the game board. This is typically used
     * for map-side portals or to reset Pac-Man's position after a victory or defeat.
     * 
     * @param gameBoard           A 2D array representing the game board's state.
     * @param targetCoordinatesXY The target coordinates where Pac-Man will be teleported.
     */
    @Override
    public void teleportAt(String[][] gameBoard, int[] targetCoordinatesXY) {
        // Pac-Man movement
        gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] = gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]].replace("P", "");
        if (gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]].length() == 0) {
            gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] = " ";
        }
        currentCoordinatesXY = targetCoordinatesXY;
        gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] = "P";
    }

    /**
     * Verifies if the desired movement direction is valid (not blocked by a wall)
     * before updating Pac-Man's direction. Note that this method does not handle full
     * collision detection; it only validates the direction.
     * 
     * @param gameBoard       A 2D array representing the current state of the game board.
     *                        This includes walls, ghosts, Pac-Man, food, and other elements.
     * @param inputDirectionXY The desired movement direction (x, y) specified by the user.
     * 
     * @see #checkCollisionAndMove(String[][]) Handles Pac-Man's actual movement and collision detection.
     * @see Game#keyPressed(java.awt.event.KeyEvent) Updates the movement direction based on user input.
     */
    public void verifyDirectionUpdate(String[][] gameBoard, int[] inputDirectionXY) {
        if (!gameBoard[currentCoordinatesXY[1] + inputDirectionXY[1]][currentCoordinatesXY[0] + inputDirectionXY[0]].equals("W")) {
            updateDirection(inputDirectionXY);
        }
    }
}
