package scripts;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.*;


public class GUI extends JFrame{
	
	private static final long serialVersionUID = -3773841284669397657L;
	
	JLabel scoreLabel;
	JLabel livesLabel;
	JPanel gameBoardDisplayJPanel;
	
	
	public GUI() {
		
		setVisible(true);
		setSize(500,500);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setFocusable(true);


		ImageIcon localPacManIcon = new ImageIcon(SpritesLoader.class.getResource("/Sprites/pacmanRight.png"));
		Image gameIcon = localPacManIcon.getImage();
		setIconImage(gameIcon);
		
		// Graphic interface Creation
		JPanel topPanel = new JPanel(new GridLayout(1, 2));
	    JPanel contentPanel = new JPanel(new BorderLayout());
		gameBoardDisplayJPanel = new JPanel(new GridLayout(21,21));
		
		scoreLabel = new JLabel("Score: 0", SwingConstants.LEFT);
		livesLabel = new JLabel("Lives: 3", SwingConstants.RIGHT);
		
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
	public void refreshGameScreen(String[][] gameBoard, HashMap<String, ImageIcon> spriteMap, PacMan pacMan) {
		gameBoardDisplayJPanel.removeAll();
		int invincibleModeCooldown  = Game.getInvincibility();
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
	
	public void updatesLifesDisplay() {
		int lives = Game.getLives();
		livesLabel.setText("Lives : "+ lives);
		
	}
	
	public void updateScoreDisplay() {
		int score =  Game.getScore();
	    scoreLabel.setText("Score : "+ score);
	}
	
	public void updateLifesLabelText(String text) {
	    livesLabel.setText(text);
	}
	

}
