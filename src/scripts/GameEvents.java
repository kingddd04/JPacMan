package scripts;

import java.util.Arrays;
import javax.swing.Timer;

/**
 * The {@code GameEvents} class handles various game mechanics and events 
 * in the JPacMan game, such as spawning ghosts, checking for game over 
 * conditions, teleporting characters through portals, and determining 
 * victory states.
 * 
 * @author kingddd04
 * @version 1.0.0
 * @since 1.2.0
 */
public class GameEvents {

    /**
     * Spawns ghosts at designated intervals and updates the game state.
     * Ghosts are spawned in sequence, with each ghost being added to the game
     * when the cooldown timer resets. The method also provides a warning message
     * to the player when ghosts are about to appear. Ghosts are positional in the array that contains them
     * 
     * @param ghosts An array of {@code Ghost} objects representing the enemies in the game.
     * @param userGui The {@code GUI} object used to update the game UI.
     * @param ghostSpawnerCooldown The cooldown timer that determines when ghosts are spawned.
     */
    public void ghostSpawner(Ghost[] ghosts, GUI userGui, int ghostSpawnerCooldown) {
        if (ghosts[0] == null) {
            userGui.updateLifesLabelText("Ghosts are Coming, HURRY!");
        }
        if (ghostSpawnerCooldown == 0 && ghosts[0] == null) {
            ghosts[0] = new Ghost(new int[]{10, 13}, new int[]{1, 0}, "r");
            Game.ghostSpawnerCooldownReset();
            ghostSpawnerCooldown = 1;
        } else if (ghostSpawnerCooldown == 0 && ghosts[1] == null) {
            ghosts[1] = new Ghost(new int[]{10, 13}, new int[]{-1, 0}, "p");
            Game.ghostSpawnerCooldownReset();
            ghostSpawnerCooldown = 1;
        } else if (ghostSpawnerCooldown == 0 && ghosts[2] == null) {
            ghosts[2] = new Ghost(new int[]{10, 13}, new int[]{1, 0}, "o");
            Game.ghostSpawnerCooldownReset();
            ghostSpawnerCooldown = 1;
        } else if (ghostSpawnerCooldown == 0 && ghosts[3] == null) {
            ghosts[3] = new Ghost(new int[]{10, 13}, new int[]{-1, 0}, "b");
            Game.ghostSpawnerCooldownReset();
            ghostSpawnerCooldown = 1;
        } else {
            if (ghostSpawnerCooldown == 0) {
                Game.ghostSpawnerCooldownReset();
            }
        }
    }

    /**
     * Checks if Pac-Man has lost by verifying collisions between Pac-Man and ghosts.
     * If Pac-Man collides with a ghost, lives are reduced, and characters are teleported 
     * to their default positions. If no lives remain, the game clock stops, and a "GAME OVER" 
     * message is displayed.
     * 
     * @param pacman The {@code PacMan} object representing the main character.
     * @param ghosts An array of {@code Ghost} objects representing all ghosts in the game.
     * @param gameClock The primary game timer, running three times per second to process events.
     * @param userGui The {@code GUI} object used to update the game UI.
     * @param invincibleModeCooldown The cooldown timer for Pac-Man's invincibility mode.
     * @param lives The number of lives remaining for Pac-Man.
     * @param gameBoard The 2D array representing the game board layout.
     */
    public void checkGameOver(PacMan pacman, Ghost[] ghosts, Timer gameClock, GUI userGui, int invincibleModeCooldown, int lives, String[][] gameBoard) {
        int[] pacmanCoordinnatesXY = pacman.getCoordinatesXY();
        for (int i = 0; i < ghosts.length; i++) {
            if (ghosts[i] != null) {
                int[] ghostCollisionCoordinatesXY = ghosts[i].getCoordinatesXY();
                if (Arrays.equals(pacmanCoordinnatesXY, ghostCollisionCoordinatesXY)) {
                    if (invincibleModeCooldown == 0) {
                        Game.decreaseLife();
                        SoundPlayer.playSound("/Sounds/pacManDefeat.wav");
                        Game.ghostSpawnerCooldownReset();
                        for (Ghost ghostToBeDeleted : ghosts) {
                            if (ghostToBeDeleted != null) {
                                ghostToBeDeleted.removeGhostIcon(gameBoard);
                            }
                        }
                        Arrays.fill(ghosts, null);
                        int[] pacmanDefaultCoordinatesXY = pacman.getDefaultCoordinatesXY();
                        pacman.updateDirection(new int[]{0, 0});
                        pacman.teleportAt(gameBoard, pacmanDefaultCoordinatesXY);
                    } else if (invincibleModeCooldown > 0) {
                        ghosts[i].removeGhostIcon(gameBoard);
                        ghosts[i] = null;
                        Game.killedGhostScoreIncrease();
                        SoundPlayer.playSound("/Sounds/ghostDefeated.wav");
                    }
                }
            }
        }
        if (lives == 0) {
            gameClock.stop();
            userGui.updateLifesLabelText("GAME OVER");
        }
    }

    /**
     * Teleports Pac-Man and ghosts when they reach a portal.
     * If a character enters Portal A, they are transported to Portal B, and vice versa.
     * 
     * @param gameBoard A 2D array representing the game board layout.
     * @param pacman The {@code PacMan} object controlled by the player via arrow keys, teleported upon reaching a portal.
     * @param ghosts An array of {@code Ghost} objects, each checked for portal interactions to ensure proper teleportation.
     * 
     * @see CharacterActions#teleportAt(String[][], int[]) method to teleport or reset ghosts
     */
    public void PortalTeleport(String[][] gameBoard, PacMan pacman, Ghost[] ghosts) {
        int[] portalAxy = new int[]{20, 10};
        int[] portalBxy = new int[]{1, 10};

        int[] pacmanXY = pacman.getCoordinatesXY();
        boolean portalCrossed = false;

        if (Arrays.equals(pacmanXY, portalAxy)) {
            pacman.teleportAt(gameBoard, portalBxy);
            portalCrossed = true;
        } else if (Arrays.equals(pacmanXY, portalBxy)) {
            pacman.teleportAt(gameBoard, portalAxy);
            portalCrossed = true;
        }
        for (Ghost ghost : ghosts) {
            if (ghost != null) {
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
        if (portalCrossed) {
            SoundPlayer.playSound("/Sounds/portalTeleport.wav");
        }
    }

    /**
     * Checks whether the player has achieved victory by scanning the game board for remaining food tiles.
     * If no food squares ("." characters) are found, the game state resets to its initial configuration.
     * Here are modified just ghost and pacMan the board is resetted in the Game class.
     * 
     * Upon victory, The game board is reloaded from a file using {@code MatrixExtractor} from {@code MatrixFromFileExtractor}.
	 *The positions of Pac-Man and all ghosts are reset to their default coordinates.
     * A prize tile ("f") is added to grant the player an extra life.
     * 
     * @param pacman The {@code PacMan} object representing the player's character, used to reset its position and direction upon victory.
     * @param ghosts An array of {@code Ghost} objects, each representing an enemy ghost, referenced to reset their positions when the game state is reset.
     * @param userGui The {@code GUI} object used to update the game UI.
     * @param gameBoard The 2D array representing the game board layout.
     * @return {@code true} if the victory condition is met; {@code false} otherwise.
     * 
     * @see Game#resetGameBoard()
     * @see Gsme#
     */
    public boolean checkVictory(PacMan pacman, Ghost[] ghosts, GUI userGui, String[][] gameBoard) {
        boolean victoryArchieved = true;
        for (String[] row : gameBoard) {
            for (String element : row) {
                if (!victoryArchieved) {
                    break;
                }
                if (element.contains(".")) {
                    victoryArchieved = false;
                    break;
                }
            }
        }

        if (victoryArchieved) {
            int[] defaultPacManCoordinatesXY = pacman.getDefaultCoordinatesXY();
            pacman.teleportAt(gameBoard, defaultPacManCoordinatesXY);
            pacman.updateDirection(new int[]{0, 0});
            Game.ghostSpawnerCooldownReset();
            for (Ghost ghostToBeDeleted : ghosts) {
                if (ghostToBeDeleted != null) {
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