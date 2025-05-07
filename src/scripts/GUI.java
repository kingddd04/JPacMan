package scripts;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.*;

/**
 * The {@code GUI} class represents the graphical user interface for the JPacMan game.
 * It extends {@link JFrame} and provides a visual layout for displaying the game board,
 * score, and lives. This class initializes the game window, sets up panels for the
 * game board and player information, and updates the display based on game state changes.
 * The class is managed by the Game class and is able to receive user input trough UserInput
 * 
 * @see Game  
 * @see UserInput
 * 
 * @author kingddd04
 * @version 1.0.0
 * @since 1.2.0
 */
public class GUI extends JFrame {

    private static final long serialVersionUID = -3773841284669397657L;

    JLabel scoreLabel;
    JLabel livesLabel;
    JPanel gameBoardDisplayJPanel;

    /**
     * Initializes the {@code GUI} class by setting up the game window and graphical elements.
     * The constructor creates the main layout, including panels for the game board,
     * score, and lives display.
     * 
     * @see UserInput
     * @see Game
     */
    public GUI() {

        setVisible(true);
        setSize(500,500);
        setTitle("JPac-Man_by_Kingddd04");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);

        ImageIcon localPacManIcon = new ImageIcon(SpritesLoader.class.getResource("/Sprites/JpacManIcon.png"));
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
     * Updates the game board display with the current state of the game.
     * The method adds graphical elements (e.g., images for Pac-Man, ghosts, and food)
     * to the {@code gameBoardDisplayJPanel} based on the contents of the 2D game board array.
     * 
     * @param gameBoard The 2D array representing the game board and its content.
     * @param spriteMap A map of single-character strings to {@link ImageIcon} objects,
     *                  used for rendering the game board graphics.
     * @param pacMan    The {@code PacMan} object, used for determining Pac-Man's direction
     *                  and rendering the appropriate sprite.
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

    /**
     * Updates the lives display to reflect the current number of lives remaining in the game.
     * This method retrieves the number of lives from the {@code Game} class and updates
     * the {@code livesLabel} to display it.
     */
    public void updatesLifesDisplay() {
        int lives = Game.getLives();
        livesLabel.setText("Lives : "+ lives);
    }

    /**
     * Updates the score display to reflect the current game score.
     * This method retrieves the score from the {@code Game} class and updates
     * the {@code scoreLabel} to display it.
     */
    public void updateScoreDisplay() {
        int score =  Game.getScore();
        scoreLabel.setText("Score : "+ score);
    }

    /**
     * Updates the lives label text with a custom string.
     * This method allows for direct manipulation of the text displayed in the {@code livesLabel}.
     * 
     * @param text The custom text to display in the lives label.
     */
    public void updateLifesLabelText(String text) {
        livesLabel.setText(text);
    }

}