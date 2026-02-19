package scripts;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.*;

/**
 * The {@code GUI} class represents the graphical user interface for the JPacMan game.
 * It extends {@link JFrame} and provides the visual layout for displaying the game board,
 * score, and lives. This class initializes the game window, sets up the panels used for
 * rendering the board and player information, and updates the display based on changes
 * in the game state.
 * <p>
 * The {@code GUI} is managed by the {@link Game} class and receives user input through
 * the {@link UserInput} listener.
 * </p>
 *
 * @see Game
 * @see UserInput
 * 
 * @author Davide Di Stefano
 * @version 1.0.0
 * @since 1.2.0
 */
public class GUI extends JFrame {
	
	/**
	 * Serialization identifier used to ensure compatibility
	 */
    private static final long serialVersionUID = -3773841284669397657L;

    /**
     * Label displaying the current game score.
     */
    JLabel scoreLabel;

    /**
     * Label displaying the player's remaining lives.
     */
    JLabel livesLabel;

    /**
     * Panel responsible for rendering the game board grid and all its graphical elements.
     */
    JPanel gameBoardDisplayJPanel;

    /**
     * Initializes the {@code GUI} by setting up the game window and all graphical components.
     * This includes the main frame, the top panel for score and lives, and the central panel
     * used to render the game board.
     * <p>
     * The constructor also loads the window icon and configures the frame's properties.
     * </p>
     *
     * @see UserInput
     * @see Game
     */
    public GUI() {

        setVisible(true);
        setSize(500, 500);
        setTitle("JPac-Man_by_Kingddd04");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);

        ImageIcon localPacManIcon = new ImageIcon(SpritesLoader.class.getResource("/Sprites/JpacManIcon.png"));
        Image gameIcon = localPacManIcon.getImage();
        setIconImage(gameIcon);

        // Graphic interface creation
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        JPanel contentPanel = new JPanel(new BorderLayout());
        gameBoardDisplayJPanel = new JPanel(new GridLayout(21, 21));

        scoreLabel = new JLabel("Score: 0", SwingConstants.LEFT);
        livesLabel = new JLabel("Lives: 3", SwingConstants.RIGHT);

        // Graphic interface assembly
        topPanel.add(scoreLabel);
        topPanel.add(livesLabel);
        topPanel.setBackground(Color.BLACK);
        scoreLabel.setForeground(Color.WHITE);
        livesLabel.setForeground(Color.WHITE);
        gameBoardDisplayJPanel.setBackground(Color.BLACK);

        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(gameBoardDisplayJPanel, BorderLayout.CENTER);
        setContentPane(contentPanel);
        add(gameBoardDisplayJPanel);
    }

    /**
     * Updates the game board display with the current state of the game.
     * <p>
     * This method clears the board panel and repopulates it with graphical elements
     * based on the contents of the 2D game board array. It handles rendering of
     * Pac-Man (with directional sprites), ghosts (including weakened state), food,
     * walls, and empty tiles.
     * </p>
     *
     * @param gameBoard the 2D array representing the game board and its contents.
     * @param spriteMap a map of single-character strings to {@link ImageIcon} objects,
     *                  used for rendering the correct sprite for each tile.
     * @param pacMan    the {@code PacMan} instance, used to determine the correct
     *                  directional sprite to display.
     */
    public void refreshGameScreen(String[][] gameBoard, HashMap<String, ImageIcon> spriteMap, PacMan pacMan) {
        gameBoardDisplayJPanel.removeAll();
        int invincibleModeCooldown = Game.getInvincibility();

        for (String[] row : gameBoard) {
            for (String string : row) {
                char firstChar = string.charAt(0);
                String firstCharString = String.valueOf(firstChar);

                // Pac-Man rendering
                if (firstCharString.equals("P")) {
                    int[] actualDirection = pacMan.getcurrentDirectionXY();

                    if (Arrays.equals(actualDirection, new int[]{0, -1})) {
                        gameBoardDisplayJPanel.add(new JLabel(spriteMap.get("U")));
                    } else if (Arrays.equals(actualDirection, new int[]{0, 1})) {
                        gameBoardDisplayJPanel.add(new JLabel(spriteMap.get("D")));
                    } else if (Arrays.equals(actualDirection, new int[]{1, 0})) {
                        gameBoardDisplayJPanel.add(new JLabel(spriteMap.get("P")));
                    } else if (Arrays.equals(actualDirection, new int[]{-1, 0})) {
                        gameBoardDisplayJPanel.add(new JLabel(spriteMap.get("L")));
                    } else {
                        gameBoardDisplayJPanel.add(new JLabel(spriteMap.get("P")));
                    }
                }

                // Weakened ghost rendering
                else if (invincibleModeCooldown > 0 &&
                        (firstCharString.equals("b") || firstCharString.equals("o")
                                || firstCharString.equals("p") || firstCharString.equals("r"))) {

                    gameBoardDisplayJPanel.add(new JLabel(spriteMap.get("w")));
                }

                // Standard tile rendering
                else {
                    ImageIcon localImage = spriteMap.get(firstCharString);
                    JLabel gameTileJLabel = new JLabel(localImage);
                    gameBoardDisplayJPanel.add(gameTileJLabel);
                }
            }
        }

        gameBoardDisplayJPanel.revalidate();
        gameBoardDisplayJPanel.repaint();
    }

    /**
     * Updates the lives display to reflect the current number of remaining lives.
     * Retrieves the value from the {@code Game} class and updates the {@code livesLabel}.
     */
    public void updatesLifesDisplay() {
        int lives = Game.getLives();
        livesLabel.setText("Lives : " + lives);
    }

    /**
     * Updates the score display to reflect the current game score.
     * Retrieves the value from the {@code Game} class and updates the {@code scoreLabel}.
     */
    public void updateScoreDisplay() {
        int score = Game.getScore();
        scoreLabel.setText("Score : " + score);
    }

    /**
     * Updates the lives label text with a custom string.
     * Useful for special events or temporary messages.
     *
     * @param text the custom text to display in the lives label.
     */
    public void updateLifesLabelText(String text) {
        livesLabel.setText(text);
    }
}
