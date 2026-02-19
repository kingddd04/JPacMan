package scripts;

import java.util.*;
import java.util.Random;

/**
 * Represents a ghost character in the game. This class handles ghost movement,
 * random path selection, teleportation, and interactions with the game board.
 * <p>
 * A ghost moves by choosing a random valid direction at each crossroad and can
 * be teleported either through map portals or when reset after defeat or victory.
 * </p>
 *
 * @author Davide Di Stefano
 * @version 1.0.0
 * @since 1.0.0
 */
public class Ghost extends Character {

    /**
     * A single-letter string identifying the ghost's color.
     * This letter is used to render the ghost on the game board.
     */
    private String ghostColorLetter;

    /**
     * Random number generator used to select the ghost's movement direction.
     */
    private Random random;

    /**
     * Initializes a Ghost instance with its position, direction, and unique color identifier.
     *
     * @param currentCoordinatesXY an integer array representing the ghost's starting (x, y) position.
     * @param currentDirectionXY   an integer array specifying the ghost's initial movement direction (x, y).
     * @param ghostColorLetter     a string representing the ghost's unique color identifier.
     *
     * @see Character
     */
    public Ghost(int[] currentCoordinatesXY, int[] currentDirectionXY, String ghostColorLetter) {
        super(currentCoordinatesXY, currentDirectionXY);
        this.ghostColorLetter = ghostColorLetter;
        this.random = new Random();
    }

    /**
     * Moves the ghost by selecting a random valid direction at each crossroad.
     * The ghost evaluates all possible directions based on its current movement vector,
     * filters out those blocked by walls, and randomly chooses one of the remaining options.
     * <p>
     * After selecting a direction, the ghost updates its position on the game board and
     * handles the removal and placement of its identifying letter.
     * </p>
     *
     * @param gameBoard the 2D matrix representing the current state of the game board.
     *
     * @see CharacterActions#checkCollisionAndMove(String[][])
     */
    @Override
    public void checkCollisionAndMove(String[][] gameBoard) {

        // Ghost possible directions based on the current direction
        int[][] possibleDirections = null;
        if (Arrays.equals(currentDirectionXY, new int[]{0, -1})) {
            possibleDirections = new int[][]{{0, -1}, {-1, 0}, {1, 0}};
        } else if (Arrays.equals(currentDirectionXY, new int[]{0, 1})) {
            possibleDirections = new int[][]{{0, 1}, {1, 0}, {-1, 0}};
        } else if (Arrays.equals(currentDirectionXY, new int[]{-1, 0})) {
            possibleDirections = new int[][]{{-1, 0}, {0, -1}, {0, 1}};
        } else if (Arrays.equals(currentDirectionXY, new int[]{1, 0})) {
            possibleDirections = new int[][]{{1, 0}, {0, -1}, {0, 1}};
        }

        ArrayList<int[]> availableDirections = new ArrayList<>();

        // Ghost valid positions not occupied by a wall
        for (int[] direction : possibleDirections) {
            if (!gameBoard[currentCoordinatesXY[1] + direction[1]][currentCoordinatesXY[0] + direction[0]].equals("W")) {
                availableDirections.add(direction);
            }
        }

        // Choice of a random direction between all the valid ones
        int[] chosenDirection = availableDirections.get(random.nextInt(availableDirections.size()));
        currentDirectionXY = chosenDirection;

        // Ghost movement
        gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] =
                gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]].replace(ghostColorLetter, "");

        gameBoard[currentCoordinatesXY[1] + currentDirectionXY[1]][currentCoordinatesXY[0] + currentDirectionXY[0]] =
                ghostColorLetter + gameBoard[currentCoordinatesXY[1] + currentDirectionXY[1]][currentCoordinatesXY[0] + currentDirectionXY[0]];

        if (gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]].length() == 0) {
            gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] = " ";
        }

        currentCoordinatesXY = new int[]{
                currentCoordinatesXY[0] + currentDirectionXY[0],
                currentCoordinatesXY[1] + currentDirectionXY[1]
        };
    }

    /**
     * Teleports the ghost to a target position. This method is used by map portals
     * or when resetting the ghost after a victory or defeat.
     *
     * @param gameBoard           the 2D matrix representing the game board.
     * @param targetCoordinatesXY the coordinates (x, y) where the ghost should be teleported.
     */
    @Override
    public void teleportAt(String[][] gameBoard, int[] targetCoordinatesXY) {

        // Removing ghost icon from the current tile
        removeGhostIcon(gameBoard);

        // Coordinates update
        gameBoard[targetCoordinatesXY[1]][targetCoordinatesXY[0]] = ghostColorLetter;
        currentCoordinatesXY = targetCoordinatesXY;
    }

    /**
     * Removes the ghost's identifying letter from its current tile on the game board.
     * If the tile becomes empty, it is replaced with a blank space.
     *
     * @param gameBoard the 2D matrix representing the game board.
     */
    public void removeGhostIcon(String[][] gameBoard) {
        gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] =
                gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]].replace(ghostColorLetter, "");

        if (gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]].length() == 0) {
            gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] = " ";
        }
    }

    /**
     * Retrieves the letter associated with the ghost's color.
     *
     * @return a string representing the ghost's color identifier.
     */
    public String getGhostColorLetter() {
        return ghostColorLetter;
    }
}
