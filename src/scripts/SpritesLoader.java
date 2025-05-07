package scripts;


import java.util.*;

import javax.swing.*;


/**
 * Loads and manages sprite images for the game.
 * This class creates a mapping between game elements (Pac-Man, ghosts, walls, food, etc.)
 * and their corresponding images, allowing the game to reference sprites easily.
 * 
 * @author Davide Di Stefano
 * @version 1.0.0
 * @since 1.0.0
 */

public class SpritesLoader {

    /**
     * Loads the game sprites and stores them in a HashMap.
     * This method associates each game element with its corresponding image icon,
     * making it easier to retrieve and display sprites.
     *
     * @return A HashMap where the keys represent game elements (pacman, walls, ghosts, etc.)
     *         and the values are their respective ImageIcon objects.
     */
	public static HashMap<String, ImageIcon>  SpritesMapLoader() {
		
		// Map elements sprites
        ImageIcon wallTile = new ImageIcon(SpritesLoader.class.getResource("/Sprites/wall.png"));
        ImageIcon foodTile = new ImageIcon(SpritesLoader.class.getResource("/Sprites/regularFood.png"));
        ImageIcon fruitTile	= new ImageIcon(SpritesLoader.class.getResource("/Sprites/cherry.png"));
        ImageIcon emptyTile = new ImageIcon(SpritesLoader.class.getResource("/Sprites/emptyTile.png"));
        ImageIcon portalTileA = new ImageIcon(SpritesLoader.class.getResource("/Sprites/portalTileA.png"));
        ImageIcon portalTileB = new ImageIcon(SpritesLoader.class.getResource("/Sprites/portalTileB.png"));
        ImageIcon powerUpTile = new ImageIcon(SpritesLoader.class.getResource("/Sprites/powerUp.png"));
        
        // Ghosts sprites
        ImageIcon blueGhostImage = new ImageIcon(SpritesLoader.class.getResource("/Sprites/blueGhost.png"));
        ImageIcon orangeGhostImage = new ImageIcon(SpritesLoader.class.getResource("/Sprites/orangeGhost.png"));
        ImageIcon pinkGhostImage = new ImageIcon(SpritesLoader.class.getResource("/Sprites/pinkGhost.png"));
        ImageIcon redGhostImage = new ImageIcon(SpritesLoader.class.getResource("/Sprites/redGhost.png"));
        ImageIcon weaknedGhosImage = new ImageIcon(SpritesLoader.class.getResource("/Sprites/scaredGhost.png"));
        
        // Pacman sprites
        ImageIcon pacmanUpImage = new ImageIcon(SpritesLoader.class.getResource("/Sprites/pacmanUp.gif"));
        ImageIcon pacmanDownImage = new ImageIcon(SpritesLoader.class.getResource("/Sprites/pacmanDown.gif"));
        ImageIcon pacmanLeftImage = new ImageIcon(SpritesLoader.class.getResource("/Sprites/pacmanLeft.gif"));
        ImageIcon pacmanRightImage = new ImageIcon(SpritesLoader.class.getResource("/Sprites/pacmanRight.gif"));

		
	HashMap<String, ImageIcon> spriteMap = new HashMap<>();
		
	spriteMap.put("P", pacmanRightImage);
	spriteMap.put("U", pacmanUpImage);    
	spriteMap.put("D", pacmanDownImage);   
	spriteMap.put("L", pacmanLeftImage);   
	spriteMap.put("0", portalTileA);
	spriteMap.put("O", portalTileB);
	spriteMap.put("W", wallTile);
	spriteMap.put(".", foodTile);
	spriteMap.put("f", fruitTile);
	spriteMap.put("x", powerUpTile);
	spriteMap.put(" ", emptyTile);
		

	spriteMap.put("b", blueGhostImage);   
	spriteMap.put("o", orangeGhostImage); 
	spriteMap.put("p", pinkGhostImage);   
	spriteMap.put("r", redGhostImage);    
	spriteMap.put("w", weaknedGhosImage);
	    
	    
	return spriteMap;
	}

}
