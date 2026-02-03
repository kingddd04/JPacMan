# JPacMan Technical Report

JPacMan is a Java-based implementation of the classic Pac-Man arcade game, developed using Java 24 and Java Swing. This report provides an in-depth technical analysis of the project's architecture, design patterns, and game mechanics.

## Table of Contents

1. [Overview](#1-overview)
2. [Project Structure](#2-project-structure)
3. [Model-View-Controller (MVC) Pattern](#3-model-view-controller-mvc-pattern)
4. [How the Game Works](#4-how-the-game-works)
   - [How Pac-Man is Moved](#41-how-pac-man-is-moved)
   - [How Ghosts are Moved and Spawned](#42-how-ghosts-are-moved-and-spawned)
   - [How the Map Works](#43-how-the-map-works)
   - [How Victory and Defeat Happens](#44-how-victory-and-defeat-happens)
   - [How GUI is Updated](#45-how-gui-is-updated)
   - [How Side Portals Work](#46-how-side-portals-work)
5. [Conclusion](#5-conclusion)

---

## 1. Overview

JPacMan is structured as a modular Java application with a clear separation of concerns following the Model-View-Controller (MVC) architectural pattern. The project successfully leverages inheritance and polymorphism to model game entities through a shared abstraction while allowing specialized behaviors in derived classes. This approach enables Pac-Man and the ghosts to share common attributes and methods defined in the base `Character` class, while each subclass overrides or extends functionality to implement distinct movement patterns and behaviors.

The game state is represented as a 2D matrix of strings (`String[][]`), where each string encodes the entities present at a specific coordinate on the board. This design elegantly supports multiple entities occupying the same position simultaneously by concatenating their identifier characters into a single string. For example, a tile containing both a ghost and food would be represented as `"r."` (red ghost on a food tile).

The core game loop runs at a fixed interval of 300 milliseconds (approximately 3 times per second) using `javax.swing.Timer`, ensuring consistent game progression. This timer-based approach provides a predictable and smooth gameplay experience while maintaining simplicity in the implementation.

JPacMan demonstrates several key object-oriented design principles:
- **Encapsulation**: Game state and logic are properly encapsulated within their respective classes
- **Inheritance**: The `Character` abstract class provides a common foundation for `PacMan` and `Ghost` subclasses
- **Polymorphism**: The `CharacterActions` interface defines a contract that both player and enemy characters must fulfill
- **Separation of Concerns**: Clear boundaries between model, view, and controller components

---

## 2. Project Structure

The JPacMan project follows a well-organized directory structure that separates code from resources:

```
JPacMan/
├── src/
│   ├── scripts/                    # Java source files
│   │   ├── Main.java               # Application entry point
│   │   ├── Game.java               # Core game controller
│   │   ├── GUI.java                # Graphical user interface
│   │   ├── Character.java          # Abstract base class for entities
│   │   ├── CharacterActions.java   # Interface for character behaviors
│   │   ├── PacMan.java             # Player character implementation
│   │   ├── Ghost.java              # Enemy AI implementation
│   │   ├── UserInput.java          # Keyboard input handler
│   │   ├── GameEvents.java         # Event processing and game logic
│   │   ├── MatrixFromFileExtractor.java  # Map loading utility
│   │   ├── SpritesLoader.java      # Asset management for sprites
│   │   └── SoundPlayer.java        # Audio playback system
│   ├── Files/                      # Game data files
│   ├── Sprites/                    # Image assets (.png, .gif)
│   ├── Sounds/                     # Audio files (.wav)
│   └── TileMap.txt                 # Game board layout definition
├── JPacManUmlClassesDiagram.pdf    # Class diagram documentation
├── TECHNICAL_REPORT.pdf            # Technical documentation (PDF)
└── README.md                       # Project overview and instructions
```

### Class Components

**Main.java**
- Entry point that bootstraps the game
- Instantiates the `Game` object to initialize and start the application
- Minimal responsibility: simply creates the game instance and allows the Game class to handle initialization

**Game.java**
- Central game controller managing the game loop, state, and coordination between components
- Maintains references to all major game objects: PacMan, ghosts, GUI, input handler, and game events
- Implements the game clock using `javax.swing.Timer` with a 300ms interval (3 frames per second)
- Manages game state variables: score, lives, cooldown timers, and the game board matrix
- Orchestrates the execution order of game operations in each frame
- Provides static methods for score updates, life management, and cooldown resets

**GUI.java**
- Graphical user interface implementation extending `JFrame`
- Responsible for rendering the game board, displaying score and lives
- Uses a `GridLayout` (21x21) to display the game board tiles
- Dynamically updates Pac-Man's sprite based on movement direction
- Implements visual feedback for invincibility mode by displaying weakened ghosts
- Refreshes the display by removing and re-adding all components each frame

**Character.java**
- Abstract base class for game entities (Pac-Man and ghosts)
- Defines common attributes: current coordinates, starting coordinates, and current direction
- Provides getter and setter methods for position and direction management
- Implements the `CharacterActions` interface, enforcing a contract for movement and teleportation
- Stores coordinates as `int[]` arrays in the format `[x, y]`

**CharacterActions.java**
- Interface defining the core actions that any character in the game must implement
- Declares two essential methods:
  - `checkCollisionAndMove(String[][] gameBoard)`: Handles collision detection and movement logic
  - `teleportAt(String[][] gameBoard, int[] targetCoordinatesXY)`: Handles instant position changes
- Ensures polymorphic behavior between different character types

**PacMan.java**
- Player-controlled character implementation extending `Character`
- Implements collision detection for food (`.`), power-ups (`x`), and fruits (`f`)
- Handles movement validation to prevent moving through walls
- Updates the game board matrix by removing Pac-Man's identifier from the old position and adding it to the new position
- Provides `verifyDirectionUpdate()` method to validate user input before changing direction
- Triggers appropriate sound effects and score updates when collecting items

**Ghost.java**
- Enemy AI implementation extending `Character`
- Each ghost is identified by a unique color letter (r=red, p=pink, o=orange, b=blue)
- Implements a simple random movement algorithm:
  - At each move, determines valid directions (forward, left, right) based on current direction
  - Filters out directions that would lead to walls
  - Randomly selects from available valid directions
  - Ghosts never move backward, creating more realistic pursuit behavior
- Uses `java.util.Random` for decision-making
- Updates the game board by moving the ghost's identifier character

**UserInput.java**
- Keyboard input handler implementing `KeyListener`
- Captures arrow key presses and translates them into direction vectors
- Maintains a local reference to the game board for validation
- Calls `PacMan.verifyDirectionUpdate()` to ensure the requested direction is valid
- Direction vectors: UP = `{0, -1}`, DOWN = `{0, 1}`, LEFT = `{-1, 0}`, RIGHT = `{1, 0}`

**GameEvents.java**
- Event processing and game logic coordinator
- Manages ghost spawning with sequential timed releases
- Implements collision detection between Pac-Man and ghosts
- Handles victory conditions by checking for remaining food tiles
- Manages portal teleportation logic
- Processes invincibility mode for temporary ghost vulnerability
- Resets game state upon victory or defeat

**MatrixFromFileExtractor.java**
- Map loading utility for reading the game board from `TileMap.txt`
- Converts file data into a 2D string array
- Implements deep copying functionality to preserve the original map layout
- Allows map reset without re-reading the file
- Uses `InputStream` and `BufferedReader` for file access from the resources directory

**SpritesLoader.java**
- Asset management for game sprites
- Loads all image resources and creates a `HashMap<String, ImageIcon>` for efficient lookup
- Maps single-character identifiers to their corresponding sprite images
- Supports directional Pac-Man sprites (up, down, left, right)
- Includes sprites for walls, food, power-ups, fruits, ghosts, and portals

**SoundPlayer.java**
- Audio playback system for game sound effects
- Plays sounds asynchronously using separate threads to avoid blocking the game loop
- Supports WAV format audio files
- Automatically manages audio resources (opening, playing, and closing clips)
- Provides a simple static method interface for playing sounds throughout the game

---

## 3. Model-View-Controller (MVC) Pattern

The JPacMan project follows an MVC-inspired architectural separation that clearly distinguishes the game's data structures, presentation layer, and input-handling logic. This organization improves modularity, maintainability, and clarity of the overall system.

### Model Layer

The Model encapsulates the game state and core gameplay logic. It defines the entities involved in the game, their behaviors, and the rules governing their interactions. The Model layer operates independently from the graphical interface and user input, focusing solely on the internal mechanics of the game.

**Components:**
- **Character.java & CharacterActions.java**: Define the base attributes and actions available to all game characters, providing a common foundation through abstraction and interface contracts.
- **Ghost.java & PacMan.java**: Specialized character implementations with specific movement algorithms and behavior logic. Pac-Man responds to user input and collects items, while ghosts implement autonomous pathfinding logic.
- **Game.java**: Manages the main game loop, state updates, collision handling, and overall game progression. Serves as the central coordinator for all game logic, maintaining the game board, score, lives, and cooldown timers.
- **GameEvents.java**: Represents and processes key gameplay events such as ghost spawning, collisions, victory conditions, and portal teleportation.
- **MatrixFromFileExtractor.java**: Loads and parses the game map from external files, converting it into the internal 2D string array data structure used throughout the game.

The Model layer maintains all game state in memory and provides methods for other layers to query and modify this state through well-defined interfaces.

### View Layer

The View is responsible for rendering the game state to the user and managing all visual and audio output. It does not contain gameplay logic but rather translates the Model's state into visual and auditory representations.

**Components:**
- **GUI.java**: The main graphical interface that displays the game board, characters, score, and lives. Renders the 21x21 grid layout and updates the display based on the current game state. Implements visual effects such as directional Pac-Man sprites and weakened ghost appearance during invincibility mode.
- **SpritesLoader.java**: Loads and manages the graphical assets used for rendering characters and objects. Creates a centralized sprite repository that the GUI can efficiently access.
- **SoundPlayer.java**: Handles sound effects and audio playback during the game. Plays appropriate sounds for eating food, collecting power-ups, defeating ghosts, victory, and defeat scenarios.

The View layer observes the Model's state and updates the display accordingly but never modifies game logic or state directly.

### Controller Layer

The Controller mediates between user actions and the game logic. It interprets input events and translates them into operations on the Model, serving as the bridge between the user and the game state.

**Components:**
- **UserInput.java**: Captures and processes keyboard input, converting arrow key presses into movement direction vectors. Validates the requested direction against the current game board state before updating Pac-Man's direction.
- **Main.java**: Initializes the application, sets up the Model and View components, and coordinates the start of the game. Acts as the application's entry point and bootstrap mechanism.

The Controller layer ensures that user interactions are properly validated and translated into appropriate Model updates, maintaining the integrity of the MVC separation.

### MVC Benefits in JPacMan

1. **Separation of Concerns**: Each layer has distinct responsibilities, making the codebase easier to understand and navigate.
2. **Maintainability**: Changes to one layer don't cascade to others. For example, modifying the ghost AI logic doesn't require changes to the GUI.
3. **Scalability**: New features can be added without major refactoring. Adding new character types only requires creating new Model classes.
4. **Testability**: The Model layer can be tested independently of the View and Controller, facilitating unit testing.
5. **Code Reusability**: The View components can be reused or replaced with minimal impact on game logic.

---

## 4. How the Game Works

When the `Main` class is executed, it instantiates a `Game` object, which serves as the central coordinator for the entire game. The initialization process follows these steps:

1. The game board matrix is loaded from `TileMap.txt` using `MatrixFromFileExtractor`
2. Sprite assets are loaded into a HashMap using `SpritesLoader`
3. A `PacMan` object is created at position `[10, 19]` with initial direction `[0, 0]` (stationary)
4. An empty `Ghost` array of size 4 is initialized (ghosts spawn later)
5. The `GUI` is instantiated and displayed
6. A `UserInput` handler is created and attached as a key listener to the GUI
7. A `GameEvents` processor is instantiated to handle game logic events
8. The game clock (timer) is started with a 300ms interval

### Game Loop Architecture

JPacMan uses a time-based game loop implemented with `javax.swing.Timer`. The timer fires every 300 milliseconds (approximately 3 frames per second), executing the following sequence of operations:

```java
gameClock = new Timer(300, (ActionEvent e) -> {
    1. Update Display (Score & Lives)
    2. Spawn Ghosts (if cooldown expired)
    3. Move Pac-Man (also picks up collectibles)
    4. Check Game Over (if player collides with a ghost)
    5. Move All Ghosts
    6. Check Game Over (if player collides with a ghost)
    7. Decrement Cooldowns (invincibility and ghost spawner)
    8. Process Portal Teleportation
    9. Refresh Screen (GUI update)
    10. Check Victory Condition and Map Reset
});
```

This carefully designed sequence ensures that collisions are checked both before and after ghost movement, preventing situations where Pac-Man and a ghost swap positions without detecting a collision.

### 4.1 How Pac-Man is Moved

Pac-Man's movement system consists of two distinct phases: **direction input validation** and **actual movement execution**.

#### Direction Input Validation (UserInput & PacMan)

When the player presses an arrow key:

1. **Input Capture**: The `UserInput` class's `keyPressed()` method captures the key event
2. **Direction Translation**: The arrow key is translated into a direction vector:
   - UP: `{0, -1}` (negative Y moves up in the matrix)
   - DOWN: `{0, 1}` (positive Y moves down)
   - LEFT: `{-1, 0}` (negative X moves left)
   - RIGHT: `{1, 0}` (positive X moves right)
3. **Path Validation**: The `verifyDirectionUpdate()` method checks if the target tile is not a wall (`"W"`):
   ```java
   public void verifyDirectionUpdate(String[][] gameBoard, int[] inputDirectionXY) {
       if(!gameBoard[currentCoordinatesXY[1]+inputDirectionXY[1]]
                    [currentCoordinatesXY[0]+inputDirectionXY[0]].equals("W")) {
           updateDirection(inputDirectionXY);	
       }
   }
   ```
4. **Direction Update**: If the path is clear, Pac-Man's direction is updated immediately, even if movement hasn't occurred yet

This validation approach creates smooth, responsive controls. Players can input a direction change while Pac-Man is still moving in another direction, and the new direction will be applied on the next movement opportunity. This is crucial for navigating tight corners and making quick directional changes.

#### Movement Execution (PacMan.checkCollisionAndMove)

During each game loop iteration, Pac-Man attempts to move in its current direction:

1. **Target Tile Identification**: Calculate the target tile position:
   ```java
   String targetTileContent = gameBoard[currentCoordinatesXY[1] + currentDirectionXY[1]]
                                       [currentCoordinatesXY[0] + currentDirectionXY[0]];
   ```

2. **Collectible Processing**: Before moving, check and process any collectibles on the target tile:
   - **Food (`.`)**: Increases score by 2 points, plays eating sound
   - **Power-up (`x`)**: Grants 30 frames (10 seconds) of invincibility, plays power-up sound
   - **Fruit (`f`)**: Increases lives by 1, plays fruit eating sound

3. **Collision Detection**: Check if the target tile is passable (not a wall, spawn zone, or portal marker):
   ```java
   if(!targetTileContent.equals("W") && 
      !targetTileContent.equals("0") && 
      !targetTileContent.equals("O"))
   ```

4. **Position Update**: If the path is clear:
   - Remove Pac-Man's identifier (`"P"`) from the current position
   - Add `"P"` to the target position
   - If the old tile becomes empty, replace with a space character (`" "`)
   - Update Pac-Man's coordinate array to reflect the new position

5. **Stationary Behavior**: If the target tile is blocked, Pac-Man remains in place but retains the current direction. This allows Pac-Man to "slide" into openings when they become available.

This movement system ensures smooth gameplay where:
- Pac-Man can queue direction changes for instant response
- Collectibles are picked up automatically during movement
- Collision detection prevents moving through walls or restricted areas
- The game board matrix accurately reflects Pac-Man's position at all times

### 4.2 How Ghosts are Moved and Spawned

The ghost system in JPacMan consists of two key components: a **sequential spawn mechanism** and a **random pathfinding algorithm**.

#### Ghost Spawning System

Ghosts are spawned sequentially rather than all at once, creating a gradual difficulty increase and giving players time to collect initial food items safely.

**Spawn Sequence:**
1. **Initial Delay**: When the game starts, `ghostSpawnerCooldown` is set to 18 frames (6 seconds)
2. **Warning Message**: When no ghosts exist, the GUI displays "Ghosts are Coming, HURRY!" to warn the player
3. **Sequential Release**: When the cooldown reaches 0, ghosts spawn one at a time in this order:
   - **Red Ghost (`r`)**: Spawns first at position `[10, 13]` moving right `{1, 0}`
   - **Pink Ghost (`p`)**: Spawns second at the same position moving left `{-1, 0}`
   - **Orange Ghost (`o`)**: Spawns third at the same position moving right `{1, 0}`
   - **Blue Ghost (`b`)**: Spawns last at the same position moving left `{-1, 0}`
4. **Cooldown Reset**: After each spawn, the cooldown is reset to 18 frames (6 seconds)

The spawn position `[10, 13]` corresponds to the center ghost chamber in the map, and the alternating left/right initial directions help spread the ghosts throughout the maze naturally.

**Spawn Events:**
- Ghosts respawn after Pac-Man loses a life
- Ghosts respawn after completing a level (victory)
- Defeated ghosts (during invincibility mode) are removed from the array and respawn after the full cooldown cycle

#### Ghost Movement Algorithm

Each ghost implements a simple but effective random pathfinding AI that creates unpredictable behavior while maintaining realistic movement constraints.

**Movement Logic:**

1. **Direction Constraints**: Ghosts determine valid directions based on their current heading to prevent backward movement:
   - Moving Up `{0, -1}`: Can go forward (up), left, or right
   - Moving Down `{0, 1}`: Can go forward (down), left, or right
   - Moving Left `{-1, 0}`: Can go forward (left), up, or down
   - Moving Right `{1, 0}`: Can go forward (right), up, or down

2. **Wall Detection**: The ghost filters out any direction that would lead to a wall tile (`"W"`):
   ```java
   ArrayList<int[]> availableDirections = new ArrayList<>();
   for(int[] direction : possibleDirections) {
       if(!gameBoard[currentCoordinatesXY[1]+direction[1]]
                    [currentCoordinatesXY[0]+direction[0]].equals("W")){
           availableDirections.add(direction);
       }
   }
   ```

3. **Random Selection**: From the remaining valid directions, the ghost randomly chooses one:
   ```java
   int[] chosenDirection = availableDirections.get(random.nextInt(availableDirections.size()));
   ```

4. **Position Update**: The ghost updates the game board matrix:
   - Remove the ghost's color identifier from the old position
   - Append the color identifier to the target position (allowing multiple entities on one tile)
   - Update the ghost's coordinate array

**Key Characteristics:**
- Ghosts never reverse direction (prevents oscillating behavior)
- Ghosts can overlap with food, power-ups, and even Pac-Man (enabling collisions)
- Each ghost has an independent random generator, creating varied behavior
- Ghosts can enter and exit the central chamber freely
- The random algorithm creates unpredictable patterns that vary each playthrough

**Collision with Pac-Man:**
- Normal mode: Collision reduces Pac-Man's lives by 1 and resets all characters
- Invincibility mode: Collision removes the ghost from the game and awards 200 points

This combination of sequential spawning and random movement creates a dynamic, engaging challenge that increases in difficulty as more ghosts enter the maze while maintaining unpredictability through randomized pathfinding.

### 4.3 How the Map Works

The map system in JPacMan is built on a string-based matrix representation that elegantly handles multiple entities occupying the same position while maintaining simplicity and efficiency.

#### Map Loading and Initialization

**File Format (TileMap.txt):**
The game board is defined in a text file with space-separated character codes:
```
W W W W W W W W W W W W W W W W W W W W W W
W . . . . . W . . x . . . . . . W . . . . W
W . W W W . W . W W W W W W W . W . W W . W
...
```

Each character represents a specific tile type:
- `W`: Wall (impassable)
- `.`: Food pellet (collectible)
- `x`: Power-up (grants invincibility)
- `f`: Fruit/cherry (grants extra life)
- `O`: Portal A (left side teleporter)
- `0`: Portal B (right side teleporter)
- ` ` (space): Empty passable tile

**Loading Process:**
1. `MatrixFromFileExtractor.MatrixExtractor()` reads the file using `InputStream` and `BufferedReader`
2. Each line is split by spaces to create a row array
3. Rows are assembled into an `ArrayList<String[]>` during reading
4. The ArrayList is converted to a `String[][]` array for faster access
5. A deep copy of the original map is stored for reset functionality

This approach allows the game to reset the map without re-reading the file, improving performance during level restarts and victories.

#### Dynamic Entity Representation

The map matrix serves dual purposes:
1. **Static Environment**: Walls, portals, and spawn zones remain constant
2. **Dynamic State**: Character positions and collectible status are continuously updated

**Multi-Entity Support:**
The string-based approach allows multiple entities on a single tile through string concatenation:
- A ghost on a food tile: `"r."` (red ghost + food)
- Pac-Man on an empty tile: `"P"`
- An empty tile after food collection: `" "`

When entities move:
- Their identifier is removed from the source tile using `replace(identifier, "")`
- Their identifier is added to the target tile (appended or standalone)
- Empty tiles are represented as `" "` to maintain matrix consistency

**Example Movement Sequence:**
```
Before: ["P", "."]  // Pac-Man at [0], food at [1]
After:  [" ", "P"]  // Pac-Man moved right, food consumed
```

#### Map Dimensions and Layout

The game board is a 21×21 matrix (441 tiles total):
- **Borders**: The entire perimeter consists of wall tiles (`"W"`), creating a bounded play area
- **Ghost Chamber**: Located at the center (`[10, 13]`), surrounded by walls with openings
- **Portals**: Positioned at `[1, 10]` and `[20, 10]`, enabling horizontal teleportation
- **Food Distribution**: Approximately 180-200 food pellets scattered throughout passable corridors
- **Power-ups**: Typically 3-4 power-ups placed at strategic corners
- **Open Areas**: Various corridor widths create different chase dynamics

#### Map Reset Mechanism

When Pac-Man completes a level (all food collected):
1. `Game.resetGameBoard()` retrieves the stored deep copy of the original map
2. The game board reference is replaced with a fresh copy
3. All food pellets are restored to their original positions
4. Power-ups are reset
5. A special fruit tile is spawned at `[15, 10]` as a bonus reward

This reset preserves the original map structure while allowing the game board to be dynamically modified during gameplay.

#### Collision Detection Using the Map

The string-based map enables efficient collision detection:
```java
String targetTile = gameBoard[y][x];
if (targetTile.equals("W")) {
    // Wall collision - block movement
}
if (targetTile.contains(".")) {
    // Food detected - collect and score
}
if (targetTile.contains("r") || targetTile.contains("p") || ...) {
    // Ghost detected - check for collision
}
```

This approach allows for:
- Quick tile type checking using `equals()` and `contains()`
- Support for multiple entities per tile
- Efficient string manipulation for entity movement
- Simple visualization in the GUI through character-to-sprite mapping

The map system's design demonstrates an effective balance between simplicity and functionality, using basic string operations to manage complex game state efficiently.

### 4.4 How Victory and Defeat Happens

JPacMan implements clear win and lose conditions with appropriate state resets and player feedback.

#### Victory Condition

**Trigger:** Victory is achieved when Pac-Man collects all food pellets (`.` characters) from the game board.

**Detection Process:**
The `GameEvents.checkVictory()` method scans the entire game board matrix after each frame:
```java
boolean victoryArchieved = true;
for (String[] row : gameBoard) {
    for (String element : row) {
        if (element.contains(".")) {
            victoryArchieved = false;
            break;
        }
    }
}
```

This nested loop examines every tile, checking if any contain the food character (`.`). If even one food pellet remains, victory is not yet achieved.

**Victory Sequence:**
When all food is collected:
1. **Character Reset**: Pac-Man is teleported back to starting position `[10, 19]`
2. **Direction Reset**: Pac-Man's direction is set to `{0, 0}` (stationary)
3. **Ghost Removal**: All ghosts are removed from the board and the ghost array is cleared
4. **Cooldown Reset**: Ghost spawner cooldown is reset to 18 frames (6 seconds)
5. **Map Reload**: The game board is reset from the stored original map copy
6. **Bonus Spawn**: A fruit tile (`f`) is spawned at `[15, 10]` granting an extra life
7. **Visual Feedback**: The lives label displays "YOU WIN, Congrats!"
8. **Audio Feedback**: The victory sound effect plays

**Continuous Gameplay:**
Unlike some implementations that end the game, JPacMan allows continuous play:
- The player retains their current score
- Lives remain unchanged (plus the bonus life from the fruit)
- Difficulty increases slightly as ghosts respawn quickly
- This creates an endless gameplay loop with increasing score potential

#### Defeat Conditions

**Trigger:** Defeat occurs when Pac-Man collides with a ghost while not in invincibility mode and runs out of lives.

**Collision Detection:**
The `GameEvents.checkGameOver()` method compares coordinates after every character movement:
```java
int[] pacmanCoordinnatesXY = pacman.getCoordinatesXY();
for (Ghost ghost : ghosts) {
    if (ghost != null) {
        int[] ghostCollisionCoordinatesXY = ghost.getCoordinatesXY();
        if (Arrays.equals(pacmanCoordinnatesXY, ghostCollisionCoordinatesXY)) {
            // Collision detected
        }
    }
}
```

**Collision is checked twice per frame:**
1. After Pac-Man moves but before ghosts move
2. After all ghosts move

This dual-check system prevents the edge case where Pac-Man and a ghost swap positions without detecting contact.

**Normal Mode Collision (Lives Remaining):**
When Pac-Man and a ghost collide without invincibility:
1. **Life Deduction**: Lives are decreased by 1
2. **Sound Effect**: The defeat sound plays
3. **Ghost Reset**: All ghosts are removed from the board and array
4. **Pac-Man Reset**: Pac-Man teleports back to starting position `[10, 19]`
5. **Direction Reset**: Movement direction set to `{0, 0}` (stationary)
6. **Respawn Delay**: Ghost spawner cooldown resets, providing a brief safe period
7. **Gameplay Continues**: The game loop continues running with reduced lives

**Invincibility Mode Collision:**
When Pac-Man collides with a ghost during invincibility:
1. **Ghost Defeated**: The ghost is removed from the game board and array
2. **Score Increase**: Player receives 200 points
3. **Sound Effect**: Ghost defeat sound plays
4. **Pac-Man Unharmed**: No life lost, Pac-Man continues moving
5. **Ghost Respawn**: The defeated ghost will respawn after the full cooldown cycle

**Game Over (0 Lives):**
When lives reach zero:
1. **Game Clock Stop**: The timer is stopped using `gameClock.stop()`
2. **Game State Freeze**: All entities freeze in their current positions
3. **Display Update**: The lives label changes to "GAME OVER"
4. **No Progression**: No further game logic executes
5. **Window Remains Open**: Player can see final score and board state
6. **Restart Required**: Player must close and relaunch the application to play again

**Life System:**
- Starting lives: 3
- Lives display: Updated in real-time in the GUI's top panel
- Extra lives: Can be gained by collecting fruit tiles (appears after victory)
- Maximum lives: No hard cap, allowing life accumulation across victories

#### Design Rationale

**Double Collision Check:**
Checking collisions before and after ghost movement prevents a critical bug where characters could "phase through" each other when moving toward each other simultaneously.

**Immediate Victory Rewards:**
Spawning a bonus fruit after victory encourages continued play and rewards skilled players with life accumulation for increasingly difficult rounds.

**Invincibility as Offense:**
The invincibility mechanic transforms Pac-Man from prey to predator, adding strategic depth where players must hunt ghosts quickly before the effect expires.

**No Auto-Restart:**
Requiring manual restart after game over allows players to review their final score and board state, providing closure to the gameplay session.

### 4.5 How GUI is Updated

The GUI update system in JPacMan uses a complete redraw approach, refreshing the entire display every frame to reflect the current game state accurately.

#### GUI Architecture

The `GUI` class extends `JFrame` and organizes the display into three main sections:

1. **Top Panel**: Displays score and lives using `JLabel` components
   - Left side: Score label (`"Score: X"`)
   - Right side: Lives label (`"Lives: X"`)
   - Background: Black with white text for classic arcade aesthetics

2. **Game Board Panel**: A 21×21 `GridLayout` containing `JLabel` elements
   - Each grid cell represents one tile from the game board matrix
   - Labels contain `ImageIcon` objects for visual representation
   - Background: Black to match the classic Pac-Man aesthetic

3. **Content Panel**: A `BorderLayout` container organizing the top panel and game board

#### Update Cycle

The GUI update process occurs in three distinct phases during each game loop iteration:

**Phase 1: Score and Lives Update (Before Gameplay)**
```java
userGui.updateScoreDisplay();
userGui.updatesLifesDisplay();
```

These methods query the `Game` class for current values:
- `updateScoreDisplay()`: Retrieves score via `Game.getScore()` and updates the score label
- `updatesLifesDisplay()`: Retrieves lives via `Game.getLives()` and updates the lives label

This update happens at the beginning of each frame to ensure the display reflects any points gained or lives lost in the previous frame.

**Phase 2: Game Board Refresh (After All Game Logic)**
```java
userGui.refreshGameScreen(gameBoard, spriteMap, pacMan);
```

The `refreshGameScreen()` method performs a complete redraw:

1. **Clear Existing Display**:
   ```java
   gameBoardDisplayJPanel.removeAll();
   ```
   All existing `JLabel` components are removed from the game board panel.

2. **Iterate Through Game Board Matrix**:
   The method processes each tile in the 21×21 matrix row by row:
   ```java
   for(String[] row : gameBoard) {
       for(String string : row) {
           char firstChar = string.charAt(0);
           String firstCharString = String.valueOf(firstChar);
           // Process tile...
       }
   }
   ```

3. **Pac-Man Rendering (Directional Sprites)**:
   When a `"P"` character is detected, the method determines which sprite to display based on Pac-Man's current direction:
   ```java
   int[] actualDirection = pacMan.getcurrentDirectionXY();
   if(Arrays.equals(actualDirection, new int[]{0, -1})) {
       // Display pacmanUp.gif
   } else if(Arrays.equals(actualDirection, new int[]{0, 1})) {
       // Display pacmanDown.gif
   } else if(Arrays.equals(actualDirection, new int[]{1, 0})) {
       // Display pacmanRight.gif
   } else if(Arrays.equals(actualDirection, new int[]{-1, 0})) {
       // Display pacmanLeft.gif
   }
   ```
   
   This creates the visual effect of Pac-Man facing his movement direction. The sprites are animated GIFs, providing the classic mouth-chomping animation.

4. **Ghost Rendering (Invincibility Visual Feedback)**:
   When a ghost identifier is detected (`r`, `p`, `o`, `b`), the method checks invincibility status:
   ```java
   int invincibleModeCooldown = Game.getInvincibility();
   if(invincibleModeCooldown > 0 && (firstCharString.equals("b") || ...)) {
       // Display weakened ghost sprite
   } else {
       // Display normal ghost sprite
   }
   ```
   
   During invincibility mode, all ghosts are rendered with a blue "scared" sprite instead of their normal colors, providing immediate visual feedback that ghosts are vulnerable.

5. **Standard Tile Rendering**:
   For all other tiles, the method looks up the corresponding sprite from the sprite map:
   ```java
   ImageIcon localImage = spriteMap.get(firstCharString);
   JLabel gametileJLabel = new JLabel(localImage);
   gameBoardDisplayJPanel.add(gametileJLabel);
   ```

6. **Component Refresh**:
   After all tiles are added, the panel is refreshed:
   ```java
   gameBoardDisplayJPanel.revalidate();
   gameBoardDisplayJPanel.repaint();
   ```
   
   - `revalidate()`: Recalculates the layout and positions of all components
   - `repaint()`: Triggers a visual redraw of the panel

**Phase 3: Special Message Updates (Event-Driven)**

The lives label serves double duty as a status message display:
- `updateLifesLabelText(String text)`: Allows custom messages to replace the lives count

Used for:
- `"Ghosts are Coming, HURRY!"`: Warning before first ghost spawn
- `"YOU WIN, Congrats!"`: Victory message
- `"GAME OVER"`: Defeat message

#### Performance Considerations

**Complete Redraw Approach:**
While removing and re-adding all 441 components (21×21) might seem inefficient, this approach is justified because:

1. **Simplicity**: Avoids complex state tracking for changed tiles
2. **Correctness**: Guarantees the display matches game state exactly
3. **Performance**: At 3 FPS (300ms intervals), the redraw cost is negligible on modern hardware
4. **Swing Optimization**: Swing batches component additions and repaints efficiently

**Sprite Caching:**
All sprites are loaded once during initialization and stored in a `HashMap`:
- No file I/O during gameplay
- Instant lookup by character identifier
- Shared `ImageIcon` instances across frames (no memory duplication)

#### Visual Features

**Animated Sprites:**
- Pac-Man sprites are animated GIFs, providing continuous mouth animation
- Ghost sprites are static PNGs
- Animation occurs automatically once GIFs are displayed

**Dynamic Visual Feedback:**
- Directional Pac-Man sprites create the illusion of responsive control
- Invincibility mode ghost color change provides immediate strategic information
- Smooth sprite transitions despite low frame rate

**Classic Aesthetics:**
- Black background mimics the original arcade machine
- High-contrast colored sprites for visibility
- Simple, clean layout focusing attention on gameplay

The GUI update system effectively balances simplicity and visual quality, providing a classic Pac-Man experience with minimal computational overhead.

### 4.6 How Side Portals Work

The portal system in JPacMan implements horizontal teleportation across the game board, allowing characters to instantly travel from one side of the maze to the other. This mechanic adds strategic depth by providing escape routes and creating tactical positioning opportunities.

#### Portal Locations and Representation

**Physical Positions:**
- **Portal A** (`O`): Located at coordinates `[1, 10]` on the left side of the board
- **Portal B** (`0`): Located at coordinates `[20, 10]` on the right side of the board

Both portals are positioned in the middle row (Y=10) of the 21×21 matrix, creating a horizontal corridor that spans the entire width of the maze.

**Visual Representation:**
- Portal A is rendered with a distinct portal sprite (typically showing a swirling entrance)
- Portal B uses a similar but visually distinguishable sprite
- Both portals are clearly visible against the black background

**Map Configuration:**
The portals are defined in `TileMap.txt` and occupy passable tiles, meaning characters can move through them as they would normal corridors. The portal characters (`O` and `0`) are treated as special tiles that trigger teleportation logic without blocking movement.

#### Teleportation Logic

The portal teleportation system is handled by the `GameEvents.PortalTeleport()` method, which is called during each game loop iteration after character movement but before GUI update.

**Process Flow:**

1. **Portal Coordinate Definition**:
   ```java
   int[] portalAxy = new int[]{20, 10};  // Right portal
   int[] portalBxy = new int[]{1, 10};   // Left portal
   ```

2. **Pac-Man Portal Detection**:
   ```java
   int[] pacmanXY = pacman.getCoordinatesXY();
   if (Arrays.equals(pacmanXY, portalAxy)) {
       pacman.teleportAt(gameBoard, portalBxy);
       portalCrossed = true;
   } else if (Arrays.equals(pacmanXY, portalBxy)) {
       pacman.teleportAt(gameBoard, portalAxy);
       portalCrossed = true;
   }
   ```
   
   The method compares Pac-Man's current coordinates with both portal positions. When a match is found:
   - Pac-Man is instantly teleported to the opposite portal
   - A flag is set to trigger the portal sound effect

3. **Ghost Portal Detection**:
   ```java
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
   ```
   
   Each ghost is individually checked for portal usage. Ghosts can randomly wander into portals and will be teleported, creating unpredictable chase dynamics.

4. **Audio Feedback**:
   ```java
   if (portalCrossed) {
       SoundPlayer.playSound("/Sounds/portalTeleport.wav");
   }
   ```
   
   A single sound effect plays regardless of how many characters used portals in the same frame, preventing audio stacking.

#### Teleportation Implementation

Both `PacMan` and `Ghost` classes implement the `teleportAt()` method defined in the `CharacterActions` interface, but with slightly different implementations:

**Pac-Man Teleportation:**
```java
public void teleportAt(String[][] gameBoard, int[] targetCoordinatesXY) {
    // Remove Pac-Man from current position
    gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] = 
        gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]].replace("P", "");
    
    // Clean up empty tile
    if(gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]].length() == 0){
        gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] = " ";
    }
    
    // Update coordinates
    currentCoordinatesXY = targetCoordinatesXY;
    
    // Place Pac-Man at new position
    gameBoard[currentCoordinatesXY[1]][currentCoordinatesXY[0]] = "P";
}
```

**Ghost Teleportation:**
```java
public void teleportAt(String[][] gameBoard, int[] targetCoordinatesXY) {
    // Remove ghost identifier from current position
    removeGhostIcon(gameBoard);
    
    // Place ghost at new position
    gameBoard[targetCoordinatesXY[1]][targetCoordinatesXY[0]] = ghostColorLetter;
    
    // Update coordinates
    currentCoordinatesXY = targetCoordinatesXY;
}
```

Both methods ensure the game board matrix accurately reflects the character's new position, maintaining the string-based entity tracking system.

#### Strategic Implications

**Escape Mechanism:**
- When chased by ghosts, players can use portals to create distance
- Portals provide a quick escape from dead-end corridors
- The horizontal positioning allows crossing the entire board instantly

**Ghost Pursuit:**
- Ghosts can follow Pac-Man through portals due to random pathfinding
- Portal usage by ghosts creates unpredictable pursuit patterns
- Sometimes ghosts use portals to accidentally position themselves advantageously

**Risk vs. Reward:**
- Using portals can position Pac-Man near uncollected food
- However, ghosts may emerge from the opposite portal unexpectedly
- Players must be aware of the entire corridor when using portals

#### Technical Considerations

**Timing:**
Portal teleportation occurs after character movement but before GUI update, ensuring:
- Characters appear at the new location immediately on the next frame
- No visual "jumping" or flickering occurs
- Collision detection on the next frame uses the updated positions correctly

**Bidirectional:**
Portals work equally in both directions—entering from either side teleports to the opposite portal. This symmetry makes the mechanic intuitive.

**Multiple Characters:**
The system handles multiple characters using portals simultaneously without conflicts. Each character's teleportation is processed independently, and the sound effect plays once per frame regardless of how many characters teleported.

**Direction Preservation:**
Characters retain their movement direction after teleportation, allowing smooth continued movement through the destination portal's corridor.

The portal system demonstrates effective use of coordinate comparison and the `teleportAt()` interface method, adding a classic Pac-Man mechanic while maintaining clean, maintainable code structure.

---

## 5. Conclusion

JPacMan successfully demonstrates the implementation of a classic arcade game using modern software engineering principles and object-oriented design patterns. The project showcases several key achievements that make it both an effective learning tool and an enjoyable gaming experience.

### Architectural Strengths

**Model-View-Controller Separation:**
The clear distinction between game logic (Model), visual presentation (View), and user interaction (Controller) creates a maintainable and extensible codebase. This separation allows developers to modify gameplay mechanics, update graphics, or change input systems independently without cascading changes across the entire project.

**Object-Oriented Design:**
The use of abstract classes (`Character`), interfaces (`CharacterActions`), and inheritance creates a flexible foundation for game entities. This approach allows Pac-Man and ghosts to share common functionality while implementing specialized behaviors, adhering to the DRY (Don't Repeat Yourself) principle and enabling easy addition of new character types.

**String-Based State Representation:**
The innovative use of a `String[][]` matrix to represent game state elegantly solves the challenge of multiple entities occupying the same tile. This approach provides simplicity, efficiency, and visual debugging capability while maintaining the flexibility to represent complex game states.

### Technical Highlights

**Time-Based Game Loop:**
The 300ms timer-based game loop provides consistent, predictable gameplay while keeping the implementation simple. This approach avoids the complexity of variable time-step loops while delivering smooth gameplay at an appropriate speed for the classic Pac-Man experience.

**Resource Management:**
The centralized loading of sprites and sounds during initialization, combined with efficient caching and reuse, ensures minimal runtime overhead. The deep copy mechanism for map resets demonstrates thoughtful optimization, avoiding unnecessary file I/O operations.

**Collision Detection:**
The dual-check collision system (before and after ghost movement) prevents edge cases while maintaining code simplicity. This demonstrates attention to detail in handling complex interaction scenarios.

### Gameplay Features

**Progressive Difficulty:**
The sequential ghost spawning system creates a natural difficulty curve, allowing players to establish position and collect initial power-ups before facing the full ghost complement. The continuous gameplay loop after victories encourages score maximization and extended play sessions.

**Strategic Depth:**
The combination of invincibility power-ups, portal teleportation, and random ghost AI creates dynamic gameplay with multiple strategic options. Players must balance aggressive food collection with defensive positioning, timing power-up usage effectively, and using portals tactically.

**Polish and Feedback:**
Directional Pac-Man sprites, invincibility visual effects, comprehensive sound effects, and immediate status updates demonstrate attention to player experience and game feel. These details elevate the project from a technical demonstration to a polished game.

### Educational Value

JPacMan serves as an excellent educational resource for learning:
- **Java programming**: Demonstrates core language features, standard library usage, and object-oriented principles
- **Swing GUI development**: Shows practical implementation of layouts, event handling, and component management
- **Game development concepts**: Illustrates game loops, state management, collision detection, and AI implementation
- **Software architecture**: Exemplifies MVC pattern, separation of concerns, and modular design

The well-commented code, clear class responsibilities, and logical project structure make it accessible for students and developers looking to understand game development fundamentals.

### Potential Enhancements

While the current implementation is complete and functional, potential future enhancements could include:
- **Advanced Ghost AI**: Implementing distinct behaviors for each ghost (e.g., direct pursuit, ambush patterns, patrol routes)
- **Difficulty Levels**: Adding difficulty settings that modify ghost speed, spawn rates, or AI aggressiveness
- **High Score Persistence**: Saving high scores to a file for tracking best performances across sessions
- **Additional Levels**: Creating multiple maze layouts with varying complexity
- **Multiplayer Mode**: Adding competitive or cooperative multiplayer functionality
- **Enhanced Graphics**: Implementing sprite animations for ghosts, particle effects for power-ups, or screen transitions

### Final Assessment

JPacMan represents a well-executed implementation of a classic game, demonstrating solid software engineering practices and thoughtful design decisions. The project balances simplicity with functionality, creating maintainable code that delivers an authentic Pac-Man experience. The clear architecture, comprehensive documentation, and attention to detail make it valuable both as a playable game and as an educational resource for aspiring game developers.

The successful application of design patterns, efficient state management, and polished gameplay mechanics demonstrate that thoughtful architecture and clean code principles can create engaging software without unnecessary complexity. JPacMan proves that classic gameplay combined with modern development practices can yield a project that is both technically sound and genuinely fun to play.
