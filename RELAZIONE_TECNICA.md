# Relazione Tecnica - JPacMan

## Informazioni Generali
- **Progetto**: JPacMan
- **Autore**: Davide Di Stefano (kingddd04)
- **Linguaggio**: Java 24
- **Framework GUI**: Java Swing
- **Versione**: 1.2.0
- **Licenza**: MIT License

---

## 1. Introduzione

JPacMan è un'implementazione in Java del classico gioco arcade Pac-Man. Il progetto è stato sviluppato come progetto universitario per l'Università "La Sapienza" di Roma e rappresenta un sistema completo di gioco con interfaccia grafica, gestione degli eventi, intelligenza artificiale per i nemici e riproduzione audio.

L'obiettivo principale del progetto è fornire un'esperienza di gioco coinvolgente mantenendo una struttura del codice modulare, estensibile e ben documentata, che può servire come strumento didattico per chi studia la programmazione orientata agli oggetti in Java.

---

## 2. Architettura del Sistema

### 2.1 Panoramica Macroscopica

Il sistema è organizzato secondo un'architettura **Model-View-Controller (MVC)** parziale, con una chiara separazione tra:

- **Model**: Classi `Character`, `PacMan`, `Ghost` - rappresentano lo stato del gioco
- **View**: Classe `GUI` - gestisce la presentazione grafica
- **Controller**: Classi `Game`, `GameEvents`, `UserInput` - coordinano la logica di gioco

### 2.2 Componenti Principali

Il sistema è composto da 12 classi Java principali, organizzate nel package `scripts`:

#### Classi Core del Gioco
1. **Main** - Punto d'ingresso dell'applicazione
2. **Game** - Orchestratore principale della logica di gioco
3. **GameEvents** - Gestione eventi di gioco (spawn, collisioni, vittoria)

#### Classi di Presentazione
4. **GUI** - Interfaccia grafica utente basata su Swing
5. **SpritesLoader** - Caricamento e gestione delle immagini
6. **SoundPlayer** - Riproduzione effetti sonori

#### Classi dei Personaggi
7. **Character** (astratta) - Classe base per i personaggi
8. **PacMan** - Implementazione del personaggio giocatore
9. **Ghost** - Implementazione dei nemici
10. **CharacterActions** (interface) - Contratto per le azioni dei personaggi

#### Classi Utility
11. **UserInput** - Gestione input da tastiera
12. **MatrixFromFileExtractor** - Caricamento mappa da file

### 2.3 Diagramma delle Dipendenze

```
Main
 └─> Game
      ├─> GUI
      ├─> PacMan (extends Character, implements CharacterActions)
      ├─> Ghost[] (extends Character, implements CharacterActions)
      ├─> GameEvents
      ├─> UserInput
      ├─> MatrixFromFileExtractor
      ├─> SpritesLoader
      └─> SoundPlayer
```

---

## 3. Scelte Progettuali

### 3.1 Pattern Architetturali

#### 3.1.1 Ereditarietà e Polimorfismo
La gerarchia `Character -> PacMan/Ghost` sfrutta il polimorfismo per gestire personaggi con comportamenti diversi:

```java
public abstract class Character implements CharacterActions {
    protected int[] currentCoordinatesXY;
    protected int[] startingCoordinatesXY;
    protected int[] currentDirectionXY;
}
```

**Motivazione**: Condividere attributi comuni (posizione, direzione) riducendo duplicazione del codice.

#### 3.1.2 Interface per Azioni
L'interface `CharacterActions` definisce un contratto comune:

```java
public interface CharacterActions {
    public void checkCollisionAndMove(String[][] gameBoard);
    public void teleportAt(String[][] gameBoard, int[] targetCoordinatesXY);
}
```

**Motivazione**: Garantire che tutti i personaggi implementino le operazioni fondamentali (movimento, teletrasporto).

#### 3.1.3 Timer-Based Game Loop
Utilizzo di `javax.swing.Timer` con frequenza di 300ms (3 FPS):

```java
gameClock = new Timer(300, (ActionEvent e) -> {
    // Logica di gioco
});
```

**Motivazione**: 
- Separazione tra logica di gioco e rendering
- Sincronizzazione semplice e predicibile
- Compatibilità con l'Event Dispatch Thread di Swing

### 3.2 Gestione dello Stato di Gioco

#### 3.2.1 Rappresentazione della Mappa
La mappa è una matrice 21x21 di stringhe (`String[][]`):

```
"W" = muro
"." = cibo normale
"x" = power-up
"P" = Pac-Man
"r","b","o","p" = fantasmi (red, blue, orange, pink)
"0","O" = portali
"f" = frutto (vita extra)
" " = casella vuota
```

**Motivazione**: 
- Flessibilità: ogni cella può contenere più elementi (es. "rP" = fantasma + PacMan)
- Facilità di serializzazione e caricamento da file
- Semplicità di rendering

#### 3.2.2 Variabili Statiche di Gioco
Uso di variabili statiche nella classe `Game`:

```java
private static int score;
private static int invincibleModeCooldown;
private static int lives;
private static int ghostSpawnerCooldown;
```

**Motivazione**: 
- Accesso globale semplificato da classi utility
- Unico punto di verità per lo stato del gioco
- Facilita l'implementazione di metodi helper statici

### 3.3 Intelligenza Artificiale dei Fantasmi

I fantasmi utilizzano un algoritmo di movimento **casuale deterministico**:

```java
// Calcola direzioni possibili senza invertire la marcia
int[][] possibleDirections = {currentDirection, left, right};

// Filtra direzioni valide (non muri)
ArrayList<int[]> availableDirections = filterValidDirections();

// Scelta casuale
int[] chosenDirection = availableDirections.get(random.nextInt());
```

**Motivazione**:
- Semplicità implementativa
- Comportamento imprevedibile per il giocatore
- Prestazioni ottimali (nessun pathfinding complesso)
- Adatto al gameplay classico di Pac-Man

### 3.4 Sistema di Caricamento Risorse

#### 3.4.1 Sprite Loader
HashMap per associazione simbolo-immagine:

```java
HashMap<String, ImageIcon> spriteMap = new HashMap<>();
spriteMap.put("P", pacmanRightImage);
spriteMap.put("W", wallTile);
```

**Motivazione**: 
- Lookup O(1) per rendering veloce
- Caricamento una tantum delle risorse
- Facilità di aggiunta nuovi sprite

#### 3.4.2 Matrix Extractor
Caricamento mappa da file testuale:

```java
try (InputStream is = MatrixFromFileExtractor.class
        .getResourceAsStream("/Files/TileMap.txt")) {
    // Parsing e conversione
}
```

**Motivazione**:
- Separazione dati/codice
- Facilità di modifica della mappa senza ricompilare
- Deep copy per reset rapido del livello

### 3.5 Gestione Audio

Thread separati per riproduzione asincrona:

```java
new Thread(() -> {
    Clip clip = AudioSystem.getClip();
    clip.open(audioStream);
    clip.start();
}).start();
```

**Motivazione**:
- Non blocca il game loop
- Permette sovrapposizione di suoni
- Gestione automatica della pulizia delle risorse

---

## 4. Funzionamento Macroscopico

### 4.1 Ciclo di Vita dell'Applicazione

```
1. Main.main()
   ↓
2. new Game() - Inizializzazione
   ├─ Carica mappa da file
   ├─ Carica sprite
   ├─ Crea PacMan e array Ghost[4]
   ├─ Inizializza GUI
   ├─ Registra UserInput listener
   └─ Avvia gameClock (Timer)
   ↓
3. Game Loop (ogni 300ms)
   ├─ Aggiorna display punteggio/vite
   ├─ Spawn fantasmi (con cooldown)
   ├─ Muove PacMan
   ├─ Controlla collisioni PacMan-Ghost
   ├─ Muove fantasmi
   ├─ Controlla collisioni (nuovamente)
   ├─ Decrementa cooldown
   ├─ Gestisce teletrasporto portali
   ├─ Refresh schermo
   └─ Controlla condizione di vittoria
   ↓
4. Eventi Speciali
   ├─ Game Over (vite = 0) → Stop timer
   ├─ Vittoria (tutti i cibi mangiati) → Reset livello
   └─ Input utente → Cambia direzione PacMan
```

### 4.2 Flusso di Input

```
Utente preme freccia
   ↓
KeyListener.keyPressed() in UserInput
   ↓
Determina direzione (int[] {dx, dy})
   ↓
pacMan.verifyDirectionUpdate(gameBoard, direction)
   ↓
Verifica se la casella target non è un muro
   ↓
Se valido: pacMan.updateDirection(direction)
   ↓
Al prossimo tick del timer: pacMan.checkCollisionAndMove()
   ↓
PacMan si muove nella nuova direzione
```

### 4.3 Sistema di Spawning Fantasmi

I fantasmi appaiono gradualmente con intervalli temporali:

```
t=0s: Messaggio "Ghosts are Coming"
t=6s: Spawn Ghost Red (r)
t=12s: Spawn Ghost Pink (p)
t=18s: Spawn Ghost Orange (o)
t=24s: Spawn Ghost Blue (b)
```

**Logica**: Fornisce al giocatore tempo per raccogliere power-up prima che tutti i fantasmi siano attivi.

---

## 5. Funzionamento Logico Dettagliato

### 5.1 Sistema di Movimento

#### PacMan
```java
// 1. Verifica tile target
String targetTile = gameBoard[y+dy][x+dx];

// 2. Gestione eventi tile
if (targetTile.contains(".")) → Incrementa score (+2)
if (targetTile.contains("x")) → Attiva invincibilità (30 tick)
if (targetTile.contains("f")) → Vita extra (+1)

// 3. Movimento se non è muro
if (!targetTile.equals("W")) {
    gameBoard[y][x] = gameBoard[y][x].replace("P", "");
    gameBoard[y+dy][x+dx] = "P";
    currentCoordinatesXY = {x+dx, y+dy};
}
```

#### Ghost
```java
// 1. Calcola direzioni valide (avanti, sinistra, destra)
// 2. Filtra direzioni che non portano a muri
// 3. Scelta casuale tra direzioni valide
// 4. Aggiorna posizione sulla mappa
```

### 5.2 Sistema di Collisioni

#### Fase 1: Collisione con Fantasma (Modalità Normale)
```
PacMan.coordinates == Ghost.coordinates
   ↓
invincibleModeCooldown == 0
   ↓
lives--
   ↓
Reset posizioni PacMan e fantasmi
   ↓
Rimuovi tutti i fantasmi
   ↓
Resetta spawn cooldown
```

#### Fase 2: Collisione con Fantasma (Modalità Invincibile)
```
PacMan.coordinates == Ghost.coordinates
   ↓
invincibleModeCooldown > 0
   ↓
score += 200
   ↓
Rimuovi fantasma dall'array
   ↓
Play sound "ghostDefeated.wav"
```

### 5.3 Sistema dei Portali

Due portali collegati in posizioni fisse:
- Portal A: coordinates {20, 10} (destra)
- Portal B: coordinates {1, 10} (sinistra)

```java
if (character.position == portalA) {
    character.teleportAt(gameBoard, portalB);
} else if (character.position == portalB) {
    character.teleportAt(gameBoard, portalA);
}
```

**Comportamento**: Teletrasporto istantaneo per PacMan e fantasmi.

### 5.4 Condizione di Vittoria

```java
boolean victoryAchieved = true;
for (String[] row : gameBoard) {
    for (String element : row) {
        if (element.contains(".")) {
            victoryAchieved = false;
            break;
        }
    }
}
```

**Azioni alla vittoria**:
1. Reset mappa (ricarica da copia in memoria)
2. Reset posizioni PacMan e fantasmi
3. Spawn cherry per vita extra
4. Messaggio "YOU WIN, Congrats!"
5. Gioco continua con mappa resettata

### 5.5 Gestione delle Vite

Sistema a **3 vite** iniziali:

- **Perdita vita**: Collisione con fantasma (modalità normale)
- **Guadagno vita**: Consumo cherry (f) dopo vittoria
- **Game Over**: lives == 0 → Stop timer, messaggio "GAME OVER"

---

## 6. Rendering e Interfaccia Grafica

### 6.1 Struttura GUI

```
JFrame (500x500)
├─ TopPanel (GridLayout 1x2)
│  ├─ scoreLabel (Score: X)
│  └─ livesLabel (Lives: Y)
└─ gameBoardDisplayJPanel (GridLayout 21x21)
   └─ 441 JLabel (uno per ogni tile)
```

### 6.2 Processo di Rendering

```java
public void refreshGameScreen(...) {
    // 1. Pulisci pannello
    gameBoardDisplayJPanel.removeAll();
    
    // 2. Per ogni tile della matrice
    for (String[] row : gameBoard) {
        for (String tile : row) {
            // 3. Determina sprite da usare
            if (tile == "P") {
                // Sprite PacMan basato su direzione
            } else if (isGhost && invincible) {
                // Sprite fantasma debole
            } else {
                // Sprite normale
            }
            // 4. Aggiungi JLabel al pannello
            gameBoardDisplayJPanel.add(label);
        }
    }
    
    // 5. Revalidate e repaint
    gameBoardDisplayJPanel.revalidate();
    gameBoardDisplayJPanel.repaint();
}
```

### 6.3 Animazioni

**PacMan**: Sprite animato (.gif) che cambia in base alla direzione:
- Up: pacmanUp.gif
- Down: pacmanDown.gif
- Left: pacmanLeft.gif
- Right: pacmanRight.gif

**Fantasmi**: Sprite statico, cambio a "scaredGhost.png" durante invincibilità.

---

## 7. Gestione della Persistenza

### 7.1 Formato File Mappa

Il file `TileMap.txt` contiene la mappa in formato testuale:

```
W W W W W W W W W W W W W W W W W W W W W
W . . . . . . . . . W . . . . . . . . . W
W . W W . W W W W . W . W W W W . W W . W
...
```

**Vantaggi**:
- Editabile con editor di testo
- Versionabile con git
- Nessun formato proprietario

### 7.2 Sistema di Reset

```java
// Copia profonda alla prima lettura
gameMapCopy = deepCopy(fasterGameMap);

// Reset rapido senza rileggere il file
public void resetGameBoard() {
    gameBoard = MatrixFromFileExtractor.getGameMapCopy();
}
```

**Ottimizzazione**: Evita I/O ripetuti durante il gioco.

---

## 8. Gestione Errori e Robustezza

### 8.1 Gestione Eccezioni

#### Caricamento Risorse
```java
try (InputStream is = class.getResourceAsStream(path)) {
    // Operazioni
} catch (IOException e) {
    System.out.println("Error reading file: " + e.getMessage());
}
```

#### Audio
```java
try {
    // Riproduzione audio
} catch (UnsupportedAudioFileException | IOException 
         | LineUnavailableException | InterruptedException e) {
    e.printStackTrace();
}
```

### 8.2 Controlli di Sicurezza

- **Null checks**: Verifica `ghost != null` prima di operazioni
- **Bounds checking**: Implicito con struttura matrice fissa
- **Deep copy**: Evita modifiche accidentali alla mappa originale

---

## 9. Performance e Ottimizzazioni

### 9.1 Ottimizzazioni Implementate

1. **HashMap per sprite**: Lookup O(1)
2. **Array 2D invece di ArrayList**: Accesso O(1)
3. **Copia in memoria della mappa**: Evita I/O ripetuti
4. **Timer a bassa frequenza**: 3 FPS riduce carico CPU
5. **Thread separati per audio**: Non blocca game loop

### 9.2 Complessità Temporale

- **checkCollisionAndMove()**: O(1) - accesso diretto array
- **ghostSpawner()**: O(1) - controlli sequenziali fissi
- **checkGameOver()**: O(g) - dove g = numero fantasmi (max 4)
- **checkVictory()**: O(n²) - dove n = 21 (dimensione mappa)
- **refreshGameScreen()**: O(n²) - rendering completo mappa

### 9.3 Uso Memoria

- **gameBoard**: 21 × 21 × sizeof(String) ≈ 2-3 KB
- **spriteMap**: 13 ImageIcon ≈ 100-200 KB
- **Audio clips**: Caricati/scaricati dinamicamente

---

## 10. Estensibilità e Manutenibilità

### 10.1 Punti di Estensione

#### Aggiunta Nuovi Livelli
```java
// Creare nuovo file TileMap2.txt
gameBoard = MatrixFromFileExtractor.MatrixExtractor("/Files/TileMap2.txt");
```

#### Nuovi Tipi di Fantasmi
```java
public class FastGhost extends Ghost {
    // Sovrascrivi movimento per velocità doppia
}
```

#### Nuovi Power-Up
```java
// Aggiungi in checkCollisionAndMove di PacMan
if (targetTile.contains("s")) { // Super power
    Game.increaseSuperPowerTime();
}
```

### 10.2 Documentazione

- **Javadoc completo** su tutte le classi pubbliche
- **Commenti inline** per logiche complesse
- **UML Class Diagram** fornito (JPacManUmlClassesDiagram.pdf)
- **README.md** con istruzioni di installazione e utilizzo

---

## 11. Testing e Validazione

### 11.1 Test Funzionali Eseguiti

1. **Movimento PacMan**: ✓ Risponde correttamente ai tasti freccia
2. **Collisione con muri**: ✓ PacMan non attraversa i muri
3. **Consumo cibo**: ✓ Score incrementa, tile si svuota
4. **Power-up**: ✓ Invincibilità attivata, fantasmi vulnerabili
5. **Collisione fantasma (normale)**: ✓ Vita persa, reset posizioni
6. **Collisione fantasma (invincibile)**: ✓ Fantasma eliminato, score +200
7. **Portali**: ✓ Teletrasporto funziona per PacMan e fantasmi
8. **Vittoria**: ✓ Mappa resettata, cherry spawna
9. **Game Over**: ✓ Timer fermato, messaggio mostrato
10. **Audio**: ✓ Effetti sonori riproducono correttamente

### 11.2 Scenari di Test

#### Test 1: Partita Completa
- Start → Muovi PacMan → Raccogli tutti i cibi → Vittoria

#### Test 2: Power-Up
- Start → Raccogli power-up → Insegui fantasma → Elimina fantasma

#### Test 3: Game Over
- Start → Collidi con fantasma 3 volte → Verifica game over

#### Test 4: Portali
- Start → Entra in portal A → Verifica uscita da portal B

---

## 12. Limitazioni Conosciute

1. **AI Fantasmi Semplice**: Movimento casuale, non inseguono attivamente PacMan
2. **Frequenza Fissa**: 3 FPS non modificabile senza ricompilare
3. **Livello Singolo**: Una sola mappa, nessuna progressione
4. **Nessun Salvataggio**: Score e progressi non persistono tra sessioni
5. **Dimensione Finestra Fissa**: 500x500 non ridimensionabile
6. **Java 24 Required**: Compatibilità non garantita con versioni precedenti

---

## 13. Possibili Miglioramenti Futuri

### 13.1 Gameplay
- Implementare algoritmo A* per AI fantasmi più intelligente
- Aggiungere multiple mappe/livelli con difficoltà crescente
- Sistema di high score persistente (file o database)
- Modalità multiplayer (due giocatori)
- Power-up aggiuntivi (velocità, invisibilità, ecc.)

### 13.2 Tecnici
- Refactoring per utilizzare pattern Observer per eventi
- Separazione configurazione in file properties
- Unit test con JUnit
- Logging strutturato (Log4j)
- Parametrizzazione difficoltà (velocità fantasmi, vite iniziali)

### 13.3 UI/UX
- Menu principale con opzioni
- Pausa gioco
- Impostazioni audio regolabili
- Temi grafici alternativi
- Animazioni morte/vittoria più elaborate

---

## 14. Conclusioni

JPacMan rappresenta un'implementazione solida e ben strutturata del classico gioco Pac-Man in Java. Il progetto dimostra una buona applicazione dei principi della programmazione orientata agli oggetti, con:

- **Separazione delle responsabilità** tra classi
- **Uso efficace dell'ereditarietà** e del polimorfismo
- **Architettura estensibile** che facilita future modifiche
- **Codice ben documentato** con Javadoc completo
- **Interfaccia grafica intuitiva** basata su Swing

Le scelte progettuali sono giustificate e bilanciano correttamente semplicità implementativa e qualità del prodotto finale. Il sistema è robusto, performante e fornisce un'esperienza di gioco fedele all'originale arcade.

Il progetto raggiunge pienamente gli obiettivi didattici, fungendo da eccellente esempio di applicazione pratica dei concetti di programmazione Java e sviluppo di videogiochi.

---

## 15. Riferimenti

- **Repository**: https://github.com/kingddd04/JPacMan
- **Documentazione Javadoc**: Inclusa nel codice sorgente
- **UML Diagram**: JPacManUmlClassesDiagram.pdf
- **Java Swing Documentation**: https://docs.oracle.com/javase/tutorial/uiswing/
- **Pac-Man Game Logic**: Wikipedia - Pac-Man

---

**Documento compilato il**: 3 Febbraio 2026  
**Versione Documento**: 1.0  
**Versione Software**: 1.2.0
