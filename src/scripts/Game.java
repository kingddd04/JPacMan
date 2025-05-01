package scripts;

import javax.swing.*;

import javax.swing.Timer;

import java.awt.*;
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
public class Game extends JFrame implements KeyListener{
	private static final long serialVersionUID = 1L;
	private PacMan pacman;
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
		
		// Graphic interface Creation
		JPanel topPanel = new JPanel(new GridLayout(1, 2));
	    JLabel scoreLabel = new JLabel("Score: " + score, SwingConstants.LEFT);
	    JLabel livesLabel = new JLabel("Lives: " + lives, SwingConstants.RIGHT);
	    JPanel contentPanel = new JPanel(new BorderLayout());
		JPanel gameBoardDisplayJPanel = new JPanel(new GridLayout(21,21));
		
		// Graphic interface assembly
	    topPanel.add(scoreLabel);
	    topPanel.add(livesLabel);
	    topPanel.setBackground(Color.BLACK);
	    scoreLabel.setForeground(Color.WHITE);
	    livesLabel.setForeground(Color.WHITE);
		gameBoardDisplayJPanel.setBackground(Color.black);
	    contentPanel.add(topPanel, BorderLayout.NORTH);
	    contentPanel.add(gameBoardDisplayJPanel, BorderLayout.CENTER);
	    setContentPane(contentPanel);
		add(gameBoardDisplayJPanel);
		
		// Buttons listener adding 
		addKeyListener(this);
		
		// Creations of game objects pacman and ghosts
		pacman = new PacMan(new int[]{10, 19}, new int[]{0, 0});
		
		Ghost[] ghosts = new Ghost[] {
			    new Ghost(new int[]{10, 11}, new int[]{0, 1}, "r"),
			    new Ghost(new int[]{9, 13}, new int[]{0, -1}, "p"),
			    new Ghost(new int[]{10, 13}, new int[]{1, 0}, "o"),
			    new Ghost(new int[]{11, 13}, new int[]{0, -1}, "b")
			};		
		
		//Creation of a clock that each 0,3 s process game events
		gameClock = new Timer(300, (ActionEvent a) -> {
			
			// refresh of score and lives number
        	scoreLabel.setText("Score : " +score);
        	livesLabel.setText("Lives : " + lives);
        	
        	// Pac-man is moved in the direction given by keys
        	pacman.checkCollisionAndMove(gameBoard);
        	
        	//If pacman has met a ghost is defeated
        	checkGameOver(ghosts, pacman , gameClock, livesLabel);
        	
        	// All ghosts are moved
        	for(Ghost ghost : ghosts) {	ghost.checkCollisionAndMove(gameBoard);}
        	
        	//If pacman has met a ghost is defeated
        	checkGameOver(ghosts, pacman , gameClock, livesLabel);
        	
        	//If is in invincible mode it's duration is decreased
    		if(invincibleModeCooldown > 0) {invincibleModeCooldown--;}
        	
    		// if any character is near to one of two portal at sides of the map is teleported to the other
        	PortalTeleport(gameBoard, pacman ,  ghosts);
        	
        	// Check if victory is achieved
        	checkVictory(pacman, ghosts);
        	
        	// Game screen is refreshed
        	refreshGameScreen(gameBoardDisplayJPanel, gameBoard, spriteMap, pacman);
        });
		gameClock.start();   // clock is started
	}
	
	/** 
	 * The method {@code refreshGameScreen} displays on a panel {@code gameBoardDisplayJPanel} with a layout gridLayout
	 * a 2d array of strings {@code gameBoardDisplayJPanel} the method for each string takes the first letter
	 * and extract with it as a key an imageIcon from a HashMap that gets converted in JLable object and added to 
	 * the gridLayout, the method also has specific ways to display characters with differtent images.
	 * 
	 * @param gameBoardDisplayJPanel the graphic container with a gridLayout that contains each square image in format JLable
	 * @param gameBoard the 2d array that represent the game board and its content like food and characters
	 * @param spriteMap is a HashMap that contains strings of single letters associated with an ImageIcon used to paint the screen
	 * @param pacMan is a reference to the Pac-Man object useful to execute useful methods on it here to check the direction of pac-man
	 * to display the corresponding direction image
	 * 
	 * @see Character#getcurrentDirectionXY()
	 */
	public static void refreshGameScreen(JPanel gameBoardDisplayJPanel, String[][] gameBoard, HashMap<String, ImageIcon> spriteMap, PacMan pacMan) {
		gameBoardDisplayJPanel.removeAll();
		for(String[] row : gameBoard) {
			for(String string : row) {
				char firstChar =  string.charAt(0);
				String firstCharString = String.valueOf(firstChar);
				if(firstCharString.equals("P")){
					int[] actualDirection = pacMan.getcurrentDirectionXY();
					if(Arrays.equals(actualDirection, new int[]{0, -1})) {
						ImageIcon localPacManImageU = spriteMap.get("U");
						JLabel localPacManLabelU = new JLabel(localPacManImageU);
						gameBoardDisplayJPanel.add(localPacManLabelU);
					}
						
					else if(Arrays.equals(actualDirection, new int[]{0, 1})) {
						ImageIcon localPacManImageD = spriteMap.get("D");
						JLabel localPacManLabelD = new JLabel(localPacManImageD);
						gameBoardDisplayJPanel.add(localPacManLabelD);
					}
					else if(Arrays.equals(actualDirection, new int[]{1,0})) {
						ImageIcon localPacManImageP = spriteMap.get("P");
						JLabel localPacManLabelP = new JLabel(localPacManImageP);
						gameBoardDisplayJPanel.add(localPacManLabelP);
					}
					else if(Arrays.equals(actualDirection, new int[]{-1, 0})) {
						ImageIcon localPacManImageL = spriteMap.get("L");
						JLabel localPacManLabelL = new JLabel(localPacManImageL);
						gameBoardDisplayJPanel.add(localPacManLabelL);
					}
					else {
						ImageIcon localPacManImageL = spriteMap.get("P");
						JLabel localPacManLabelL = new JLabel(localPacManImageL);
						gameBoardDisplayJPanel.add(localPacManLabelL);
 						gameBoardDisplayJPanel.add(localPacManLabelL);
					}
				}
				else if(invincibleModeCooldown > 0 && (firstCharString.equals("b") || firstCharString.equals("o") ||  firstCharString.equals("p") || firstCharString.equals("r"))){
					ImageIcon weakGhostImage = spriteMap.get("w");
					JLabel weakGhostLabel = new JLabel(weakGhostImage);
					gameBoardDisplayJPanel.add(weakGhostLabel);
				}else {
				ImageIcon localImage = spriteMap.get(firstCharString);
				JLabel gametileJLabel = new JLabel(localImage);
				gameBoardDisplayJPanel.add(gametileJLabel);
				}
			}
		}
		gameBoardDisplayJPanel.revalidate();
		gameBoardDisplayJPanel.repaint();
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

	public void checkGameOver(Ghost[]  ghosts, PacMan pacman , Timer gameClock, JLabel livesLabel) {
		
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
				livesLabel.setText("GAME OVER");
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


}