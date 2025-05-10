package scripts;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.event.*;
import java.util.*;

/**
 * The {@code Game} class contains the core game logic for JPacMan, managing 
 * the GUI, characters, events, and game state, using a simple clock that runs 3 times for second
 * 
 * @author kingddd04
 * @version 1.2.0
 * @since 1.0.0
 */
public class Game {
	
    private PacMan pacman;
    private Ghost[] ghosts;
    private GUI userGui;
    private UserInput userInput;
    private GameEvents gameEvents;
    private String[][] gameBoard;
    private HashMap<String, ImageIcon> spriteMap;
    private static int score;
    private static int invincibleModeCooldown = 0;
    private static int lives = 3;
    private static int ghostSpawnerCooldown = 18;
    private Timer gameClock;

    /**
     * Initialises the game, setting up the game board, characters, GUI, and event clock.
     */
    public Game() {
        gameBoard = MatrixFromFileExtractor.MatrixExtractor("/Files/TileMap.txt");
        spriteMap = SpritesLoader.SpritesMapLoader();

        pacman = new PacMan(new int[]{10, 19}, new int[]{0, 0});
        ghosts = new Ghost[4];

        userGui = new GUI();
        userInput = new UserInput(pacman);
        gameEvents = new GameEvents();

        userGui.addKeyListener(userInput);
        userInput.setLocalGameBoard(gameBoard);

        gameClock = new Timer(300, (ActionEvent e) -> {
        	
        	// Update of the lives and score display on screen
            userGui.updateScoreDisplay();
            userGui.updatesLifesDisplay();
            
            // if the ghost spawner cooldown reaches 0 and the ghost array has some missing ghosts a ghost is spawned
            gameEvents.ghostSpawner(ghosts, userGui, ghostSpawnerCooldown);
            
            // Move Player
            pacman.checkCollisionAndMove(gameBoard);
            
            //Check if pacMan collides with a ghost
            gameEvents.checkGameOver(pacman, ghosts, gameClock, userGui, invincibleModeCooldown, lives, gameBoard);

            // Move ghosts
            for (Ghost ghost : ghosts) {
                if (ghost != null) {
                    ghost.checkCollisionAndMove(gameBoard);
                }
            }
            
            //Check if pacMan collides with a ghost
            gameEvents.checkGameOver(pacman, ghosts, gameClock, userGui, invincibleModeCooldown, lives, gameBoard);

            // decrease variables related to time if they are more than 0
            if (invincibleModeCooldown > 0) invincibleModeCooldown--;
            if (ghostSpawnerCooldown > 0) ghostSpawnerCooldown--;

            // Teleport characters if they are on a portal tile to the other
            gameEvents.PortalTeleport(gameBoard, pacman, ghosts);
            
            // Refresh game Screen
            userGui.refreshGameScreen(gameBoard, spriteMap, pacman);

            // Check Victory
            if (gameEvents.checkVictory(pacman, ghosts, userGui, gameBoard)) {
                resetGameBoard();
                spawnExtraLifeCherry();
            }
        });
        gameClock.start();   // start game clock and game progression
    }

    /**
     * Increases the score by 2 for consuming food.
     */
    public static void foodsScoreIncrease() {
        score += 2;
    }

    /**
     * Increases the score by 200 for defeating a ghost.
     */
    public static void killedGhostScoreIncrease() {
        score += 200;
    }

    /**
     * Spawns a special "extra life" item on the game board.
     */
    public void spawnExtraLifeCherry() {
        gameBoard[15][10] = "f";
    }

    /**
     * Resets the game board to its initial state.
     */
    public void resetGameBoard() {
        gameBoard = MatrixFromFileExtractor.getGameMapCopy();
    }

    /**
     * Resets the ghost spawn cooldown variable to its default value.
     */
    public static void ghostSpawnerCooldownReset() {
        ghostSpawnerCooldown += 18;
    }

    /**
     * Increases invincibility time by 30 frames (10 seconds).
     */
    public static void increaseInvincibilityTime() {
        invincibleModeCooldown = 30;
    }

    /**
     * Decreases the player's lives by one.
     */
    public static void decreaseLife() {
        lives -= 1;
    }

    /**
     * Increases the player's lives by one.
     */
    public static void increaseLifes() {
        lives += 1;
    }

    /**
     * Retrieves the current number of lives.
     * 
     * @return The player's remaining lives.
     */
    public static int getLives() {
        return lives;
    }

    /**
     * Retrieves the current score.
     * 
     * @return The player's score.
     */
    public static int getScore() {
        return score;
    }

    /**
     * Retrieves the remaining invincibility cooldown.
     * 
     * @return The remaining invincibility frames.
     */
    public static int getInvincibility() {
        return invincibleModeCooldown;
    }
}