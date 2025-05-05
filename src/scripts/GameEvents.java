package scripts;


import java.util.Arrays;

import javax.swing.Timer;

public class GameEvents {
	public void ghostSpawner(Ghost[] ghosts,GUI userGui, int ghostSpawnerCooldown) {
		if (ghosts[0] == null){
			userGui.updateLifesLabelText("Ghosts are Coming, HURRY!");
		}
		if(ghostSpawnerCooldown == 0 && ghosts[0] == null) {
			ghosts[0] = new Ghost(new int[]{10, 13}, new int[]{1, 0}, "r");
			Game.ghostSpawnerCooldownReset();
			ghostSpawnerCooldown = 1;
		}else if (ghostSpawnerCooldown == 0  && ghosts[1] == null) {
			ghosts[1] = new Ghost(new int[]{10, 13}, new int[]{-1, 0}, "p");
			Game.ghostSpawnerCooldownReset();
			ghostSpawnerCooldown = 1;
		}else if(ghostSpawnerCooldown == 0  && ghosts[2] == null) {
			ghosts[2] =new Ghost(new int[]{10, 13}, new int[]{1, 0}, "o");
			Game.ghostSpawnerCooldownReset();
			ghostSpawnerCooldown = 1;
		}else if(ghostSpawnerCooldown == 0  && ghosts[3] == null) {
			ghosts[3] = new Ghost(new int[]{10, 13}, new int[]{-1, 0}, "b");
			Game.ghostSpawnerCooldownReset();
			ghostSpawnerCooldown = 1;
		}
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

	public void checkGameOver(PacMan pacman , Ghost[] ghosts, Timer gameClock, GUI userGui, int invincibleModeCooldown, int lives, String[][] gameBoard) {
		
		int[] pacmanCoordinnatesXY = pacman.getCoordinatesXY();
		for(Ghost ghost : ghosts) {
			if(ghost != null) {
				int[] ghostCollisionCoordinatesXY = ghost.getCoordinatesXY();
				if(Arrays.equals(pacmanCoordinnatesXY, ghostCollisionCoordinatesXY)) {
					if(invincibleModeCooldown == 0) {
						Game.decreaseLife();
						SoundPlayer.playSound("/Sounds/pacManDefeat.wav");
						Game.ghostSpawnerCooldownReset();
						for(Ghost ghostToBeDeleted : ghosts) {
							if(ghostToBeDeleted != null) {
								ghostToBeDeleted.removeGhostIcon(gameBoard);
							}
						}
						Arrays.fill(ghosts, null);
						int[] pacmanDefaultCoordinatesXY = pacman.getDefaultCoordinatesXY();
						pacman.updateDirection(new int[] {0,0});
						pacman.teleportAt(gameBoard,pacmanDefaultCoordinatesXY);
					}
					else if(invincibleModeCooldown > 0){
						int[]  killedGhostDeafultCoordinatesXY = ghost.getDefaultCoordinatesXY();
						ghost.teleportAt(gameBoard, killedGhostDeafultCoordinatesXY);
						Game.killedGhostScoreIncrease();
						SoundPlayer.playSound("/Sounds/ghostDefeated.wav");
					}
				}
			}
		}
		if(lives == 0) {
			gameClock.stop();
			userGui.updateLifesLabelText("GAME OVER");
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
		boolean portalCrossed = false;
		
		if(Arrays.equals(pacmanXY, portalAxy)) {
			pacman.teleportAt(gameBoard, portalBxy);
			portalCrossed = true;
		}
		else if(Arrays.equals(pacmanXY, portalBxy)) {
			pacman.teleportAt(gameBoard, portalAxy);
			portalCrossed = true;
		}
		for(Ghost ghost : ghosts) {
			if(ghost != null) {
				int[] ghostXY = ghost.getCoordinatesXY();
				if (Arrays.equals(ghostXY, portalAxy)) {
	               ghost.teleportAt(gameBoard, portalBxy);
	           	portalCrossed = true;
				} else if (Arrays.equals(ghostXY, portalBxy)) {
					ghost.teleportAt(gameBoard, portalAxy);
					portalCrossed = true;
				}
	      }
	   }
		if(portalCrossed == true) {
			SoundPlayer.playSound("/Sounds/portalTeleport.wav");
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

	public boolean checkVictory(PacMan pacman , Ghost[] ghosts, GUI userGui ,String[][] gameBoard) {
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
			gameBoard = MatrixFromFileExtractor.MatrixExtractor("/Map/TileMap.txt");
			int[] defaultPacManCoordinatesXY = pacman.getDefaultCoordinatesXY();
			pacman.teleportAt(gameBoard, defaultPacManCoordinatesXY);
			pacman.updateDirection(new int[] {0,0});
			Game.ghostSpawnerCooldownReset();
			for(Ghost ghostToBeDeleted : ghosts) {
				if(ghostToBeDeleted != null) {
					ghostToBeDeleted.removeGhostIcon(gameBoard);
				}
			}
			Arrays.fill(ghosts, null);	
			Game.ghostSpawnerCooldownReset();
			userGui.updateLifesLabelText("YOU WIN, Congrats!");
			SoundPlayer.playSound("/Sounds/victoryAchieved.wav");
		}
		return victoryArchieved;
	}

	
	
}
