package scripts;

import javax.swing.*;

import javax.swing.Timer;

import java.awt.event.*;
import java.util.*;

/**
 * The {@code Game} class represents the main PacMan game logic and GUI.
 * It handles game board rendering, player and ghost movements,
 * score, life tracking, and game events like defeat or victory.
 * 
 * @author Davide Di Stefano
 * @version 1.0.0
 * @since 1.0.0
 */
public class Game{
	private PacMan pacman;
	private Ghost[] ghosts;
	private GUI userGui;
	private UserInput userInput;
	private GameEvents gameEvents;
	String[][] gameBoard; 
	HashMap<String, ImageIcon> spriteMap;
	private static int score;
	private static int invincibleModeCooldown = 0;
	private static int  lives = 3;
	private static int ghostSpawnerCooldown = 24;
	private Timer gameClock;

	
    /**
     * Constructs the Game object, initialises  game components, 
     * like the GUI, the game logic backbone, the characters (ghosts and pacman)
     * initialises the game clock that process all events in the game 
     * 
     * @see MatrixFromFileExtractor#MatrixFromFileExtractor()
     * @see SpritesLoader#SpritesMapLoader()
     */
	public Game() {
		gameBoard = MatrixFromFileExtractor.MatrixExtractor("/Files/TileMap.txt");
		spriteMap = SpritesLoader.SpritesMapLoader(); 
		
		// Creations of game objects pacman and ghosts
		pacman = new PacMan(new int[]{10, 19}, new int[]{0, 0});
		ghosts = new Ghost[4];
		
		
		userGui = new GUI();
		userInput = new UserInput(pacman);
		gameEvents = new GameEvents();

		// Buttons listener adding 
		userGui.addKeyListener(userInput);
		
		//Creation of a clock that each 0,3 s process game events
		gameClock = new Timer(300, (ActionEvent a) -> {
			
			// refresh of score and lives number
			userGui.updateScoreDisplay();
			userGui.updatesLifesDisplay();
			
			gameEvents.ghostSpawner(ghosts, userGui, ghostSpawnerCooldown);

        	// Pac-man is moved in the direction given by keys
			userInput.setLocalGameBoard(gameBoard);
        	pacman.checkCollisionAndMove(gameBoard);
        	
        	//If pacman has met a ghost is defeated
        	gameEvents.checkGameOver(pacman, ghosts, gameClock, userGui, invincibleModeCooldown, lives, gameBoard);
        	
        	// All ghosts are moved if they are spawned
        	for(Ghost ghost : ghosts) {
        		if(ghost != null) {
        			ghost.checkCollisionAndMove(gameBoard);}
        	}
        	
        	//If pacman has met a ghost is defeated
        	gameEvents.checkGameOver(pacman, ghosts, gameClock, userGui, invincibleModeCooldown, lives, gameBoard);
        	
        	//Ifb is in invincible mode it's duration is decreased
        	//Ifb is in invincible mode it's duration is decreased
        	if(invincibleModeCooldown > 0) {invincibleModeCooldown--;}
        	if(ghostSpawnerCooldown > 0) {ghostSpawnerCooldown--;}
        	
    		// if any character is near to one of two portal at sides of the map is teleported to the other
        	gameEvents.PortalTeleport(gameBoard, pacman ,  ghosts);
        	
        	userGui.refreshGameScreen(gameBoard, spriteMap, pacman);
        	
        	// Check if victory is achieved
        	boolean victoryArchieved = gameEvents.checkVictory(pacman, ghosts,userGui, gameBoard);
        	
        	if (victoryArchieved == true) {
        		resetGameBoard();
        		spawnExtraLifeCherry();
        	}
        	
        	// Game screen is refreshed
        });
		gameClock.start();   // clock is started
	}

	
	/**
	 * Increases the score static instance variable by 2.
	 */
	public static void foodsScoreIncrease() {
		score += 2;
	}
	
	public static void killedGhostScoreIncrease() {
		score += 200;
	}
	
	public void spawnExtraLifeCherry() {
		gameBoard[15][10] = "f";
	}
	
	public void resetGameBoard() {
		gameBoard = MatrixFromFileExtractor.resetGameMap();
	}
	public static void ghostSpawnerCooldownReset() {
		ghostSpawnerCooldown += 24;
	}
	
	
	/**
	 * Increases the invincibility static instance variable by 30 frames or 10 seconds
	 * garanting the player some time 
	 */
	public static void increaseInvincibilityTime() {
		invincibleModeCooldown = 30 ;
	}
	
	public static void  decreaseLife() {
		lives-= 1;
	}
	
	/**
	 * Increases the lives instance variable by 1.
	 * This method updates the lives variable, granting an additional life to the player.
	 */
	public static void increaseLifes() {
		lives += 1;
	}

	public static int getLives() {
		return lives;
	}
	
	public static int getScore() {
		return score;
	}
	public static int getInvincibility() {
		return invincibleModeCooldown;
	} 
}