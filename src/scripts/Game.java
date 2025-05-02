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
public class Game  implements KeyListener{
	private PacMan pacman;
	private GUI userGui;
	String[][] gameBoard; 
	HashMap<String, ImageIcon> spriteMap;
	private static int score;
	private static int invincibleModeCooldown = 0;
	private static int  lives = 3;
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
		gameBoard = MatrixFromFileExtractor.MatrixExtractor("src/TileMap.txt");
		spriteMap = SpritesLoader.SpritesMapLoader(); 
		
		userGui = new GUI();

		// Creations of game objects pacman and ghosts

		pacman = new PacMan(new int[]{10, 19}, new int[]{0, 0});
		
		// Buttons listener adding 
		userGui.addKeyListener(this);
		
		Ghost[] ghosts = new Ghost[] {
			    new Ghost(new int[]{10, 11}, new int[]{0, 1}, "r"),
			    new Ghost(new int[]{9, 13}, new int[]{0, -1}, "p"),
			    new Ghost(new int[]{10, 13}, new int[]{1, 0}, "o"),
			    new Ghost(new int[]{11, 13}, new int[]{0, -1}, "b")
			};		
		
		//Creation of a clock that each 0,3 s process game events
		gameClock = new Timer(300, (ActionEvent a) -> {
			
			// refresh of score and lives number
			userGui.updateScoreDisplay();
			userGui.updatesLifesDisplay();

        	
        	// Pac-man is moved in the direction given by keys
        	pacman.checkCollisionAndMove(gameBoard);
        	
        	//If pacman has met a ghost is defeated
        	checkGameOver(ghosts, pacman , gameClock);
        	
        	// All ghosts are moved
        	for(Ghost ghost : ghosts) {	ghost.checkCollisionAndMove(gameBoard);}
        	
        	//If pacman has met a ghost is defeated
        	checkGameOver(ghosts, pacman , gameClock);
        	
        	//If is in invincible mode it's duration is decreased
    		if(invincibleModeCooldown > 0) {invincibleModeCooldown--;}
        	
    		// if any character is near to one of two portal at sides of the map is teleported to the other
        	PortalTeleport(gameBoard, pacman ,  ghosts);
        	
        	userGui.refreshGameScreen(gameBoard, spriteMap, pacman);
        	
        	// Check if victory is achieved
        	checkVictory(pacman, ghosts);
        	
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
	
	
	/**
	 * Increases the invincibility static instance variable by 30 frames or 10 seconds
	 * garanting the player some time 
	 */
	public static void increaseInvincibilityTime() {
		invincibleModeCooldown = 30 ;
	}
	
	/**
	 * Increases the lives instance variable by 1.
	 * This method updates the lives variable, granting an additional life to the player.
	 */
	public static void increaseLifes() {
		lives += 1;
	}
	
	/**
	 * Checks if pac-man has lost by verifying collisions between Pac-Man and ghosts.
	 * If Pac-Man collides with a ghost, lives are reduced, and characters are teleported to their {@code defaultGhostCoordinatesXY}
	 * If no lives remain, the game clock stops, and a "GAME OVER" message is displayed.
	 *
	 * @param ghosts an array of `Ghost` objects representing all ghosts in the game.
	 * @param pacman the `PacMan` object representing the main character.
	 * @param gameClock the primary game timer, running three times per second to process events.
	 * @param livesLabel a UI label used to display the player's remaining lives or game-over status.
	 * 
	 * @see PacMan#getCoordinatesXY()
	 * @see PacMan#getDefaultCoordinatesXY()
	 * @see PacMan#teleportAt(String[][], int[])
	 * @see Ghost#teleportAt(String[][], int[])
	 */

	public void checkGameOver(Ghost[]  ghosts, PacMan pacman , Timer gameClock) {
		
		int[] pacmanCoordinnatesXY = pacman.getCoordinatesXY();
		for(Ghost ghost : ghosts) {
			int[] ghostCollisionCoordinatesXY = ghost.getCoordinatesXY();
			if(Arrays.equals(pacmanCoordinnatesXY, ghostCollisionCoordinatesXY)) {
				if(invincibleModeCooldown == 0) {
					lives -=1;				
					for(Ghost ghostToBeResetted : ghosts) {
						int[]  defaultGhostCoordinatesXY = ghostToBeResetted.getDefaultCoordinatesXY();
						ghostToBeResetted.teleportAt(gameBoard, defaultGhostCoordinatesXY);
					}
					int[] pacmanDefaultCoordinatesXY = pacman.getDefaultCoordinatesXY();
					pacman.updateDirection(new int[] {0,0});
					pacman.teleportAt(gameBoard,pacmanDefaultCoordinatesXY);
				}
				else if(invincibleModeCooldown > 0){
					int[]  killedGhostDeafultCoordinatesXY = ghost.getDefaultCoordinatesXY();
					ghost.teleportAt(gameBoard, killedGhostDeafultCoordinatesXY);
					score += 200;
				}
			}
			if(lives == 0) {
				gameClock.stop();
				userGui.updatesLifesDisplay();
			}
		}
	}
	
	/**
	 * Checks whether the player has achieved victory by scanning the game board for remaining food tiles.
	 * If no food squares ("." characters) are found, the game state resets to its initial configuration.
	 * 
	 * Upon victory:
	 * - The game board is reloaded from a file using {@code MatrixExtractor} from {@code MatrixFromFileExtractor}.
	 * - The positions of Pac-Man and all ghosts are reset to their default coordinates.
	 * - A prize tile ("f") is added to grant the player an extra life.
	 *
	 * @param pacman the {@code PacMan} object representing the player's character, used to reset its position and direction upon victory.
	 * @param ghosts an array of {@code Ghost} objects, each representing an enemy ghost, referenced to reset their positions when the game state is resetted.
	 * 
	 * @see PacMan#updateDirection(int[])
	 * @see PacMan#teleportAt(String[][], int[])
	 * @see Ghost#teleportAt(String[][], int[])
	 */

	public void checkVictory(PacMan pacman , Ghost[] ghosts) {
		boolean victoryArchieved = true;
		for(String[] row : gameBoard) {
			for(String element : row) {
				if(victoryArchieved == false) {
					break;
				}
				if(element.contains(".")) {
					victoryArchieved = false;
					break;
				}
			}
		}
			
		if(victoryArchieved == true) {
			gameBoard = MatrixFromFileExtractor.MatrixExtractor("src/TileMap.txt");
			int[] defaultPacManCoordinatesXY = pacman.getDefaultCoordinatesXY();
			pacman.teleportAt(gameBoard, defaultPacManCoordinatesXY);
			pacman.updateDirection(new int[] {0,0});
			for(Ghost ghostToBeResetted : ghosts) {
				int[]  defaultGhostCoordinatesXY = ghostToBeResetted.getDefaultCoordinatesXY();
				ghostToBeResetted.teleportAt(gameBoard, defaultGhostCoordinatesXY);
			}	
			gameBoard[15][10] = "f";
		}
	}
	/**
	 * Teleports Pac-Man and ghosts when they reach a portal.
	 * If a character enters Portal A, they are transported to Portal B, and vice versa.
	 *
	 * @param gameBoard a 2D array representing the game board layout.
	 * @param pacman the {@code PacMan} object controlled by the player via arrow keys, teleported upon reaching a portal.
	 * @param ghosts an array of {@code Ghost} objects, each checked for portal interactions to ensure proper teleportation.
	 * 
	 * @see PacMan#teleportAt(String[][], int[])
	 * @see Ghost#teleportAt(String[][], int[])
	 * @see Ghost#getCoordinatesXY()
	 * @see PacMan#getCoordinatesXY()
	 */
	public void PortalTeleport(String[][] gameBoard, PacMan pacman, Ghost[] ghosts ) {
		int[] portalAxy = new int[] {20,10};
		int[] portalBxy = new int[] {1, 10};
		
		int[] pacmanXY = pacman.getCoordinatesXY();
		
		if(Arrays.equals(pacmanXY, portalAxy)) {
			pacman.teleportAt(gameBoard, portalBxy);
		}
		if(Arrays.equals(pacmanXY, portalBxy)) {
			pacman.teleportAt(gameBoard, portalAxy);
		}
		for(Ghost ghost : ghosts) {
			 int[] ghostXY = ghost.getCoordinatesXY();
			 if (Arrays.equals(ghostXY, portalAxy)) {
	               ghost.teleportAt(gameBoard, portalBxy);
	         } else if (Arrays.equals(ghostXY, portalBxy)) {
	            ghost.teleportAt(gameBoard, portalAxy);
	      }
	   }		
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
			pacmanInputDirectionXY =  new int[] {0, 1};
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        	pacmanInputDirectionXY =  new int[] {-1, 0};
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        	pacmanInputDirectionXY =  new int[] {1, 0};
        }
		pacman.verifyDirectionUpdate(gameBoard, pacmanInputDirectionXY);
    }
	
	/**
	 * Handles the event when a key is released.
	 * This method is triggered whenever a key is released on the keyboard.
	 *
	 * @param e the KeyEvent object containing information about the key release
	 */
	public void keyReleased(KeyEvent e) {
	    // Not needed 
	}

	/**
	 * Handles the event when a key is typed.
	 * This method is triggered whenever a key is typed, combining a key press and release.
	 *
	 * @param e the KeyEvent object containing information about the key typed
	 */
	public void keyTyped(KeyEvent e) {
	    // Not needed
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