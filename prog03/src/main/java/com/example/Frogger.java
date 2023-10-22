package com.example;

import java.util.List;
import java.util.Random;
import javafx.geometry.Pos;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

public class Frogger {
  // Game Configuration params
  private static final int GAME_WIDTH = 750;
  private static final int GAME_HEIGHT = 950;
  public static final int NUM_ROWS = 25;
  public static final int ROW_SIZE = 50;
  private static final int NUM_ENEMIES = 25;

  // progress
  private Label progressLabel;
  private Canvas canvas;
  private Main main; // Gives ability to change the scene based off of game result straight from here

  // ui components
  private UI ui;
  private StackPane start;
  private BorderPane borderPane;
  private BorderPane uiPane;
  private GraphicsContext graphicsContext;

  // Logical components
  private Frog frog;
  private Random random; // Used for creating new enemies
  private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
  public ArrayList<Row> rows = new ArrayList<Row>();
  private boolean frogMoved = false;

  // Stats for solution screens
  private int numMoves = 0;
  private int numEnemiesCreated = 0;
  private long startTime = System.nanoTime();
  private long endTime;
  private double avgEnemySpeed = 0;
  private int numEnemiesRight = 0;
  private int numEnemiesLeft = 0;

  /*
   * -1 -> Loser 
   *  0 -> Game in progress
   *  1 -> Winner
   */
  private int gameOver = 0;

  // Handles all the game logic
  public Frogger(Canvas canvas, Main main) {
    // Initialize variables
    this.main = main;
    this.canvas = canvas;

    // UI stuff
    start = new StackPane();
    borderPane = new BorderPane(canvas);
    borderPane.setId("borderPane");
    uiPane = new BorderPane(canvas);
    uiPane.setId("uiPane");

    // progress box
    progressLabel = new Label("Progress");
    progressLabel.setId("progressLabel");

    // Initialize the key event handler
    canvas.setOnKeyPressed(new KeyPressHandler());

    initialize();

    // init ui after we have created the rows
    this.ui = new UI(this);
  }

  public void initialize() {
    // Set graphics Context
    graphicsContext = canvas.getGraphicsContext2D();

    // initialize rows
    double rowHeight = GAME_HEIGHT / NUM_ROWS;
    for (int i = 0; i < NUM_ROWS; i++) {
      double rowY = i * rowHeight;
      // Define safe rows
      Color rowColor = (i == 0 || i == NUM_ROWS - 1 || i == 9 || i == 10 || i == 13 || i == 4 || i == 20) ? Color.GREEN
          : Color.GRAY;
      Row row = new Row(i, rowY + rowHeight / 2, rowColor);
      rows.add(row);
    }

    this.frog = new Frog(this.canvas, rows.get(rows.size() - 1));

    // Creating enemies
    this.random = new Random();
    for (int i = 0; i < NUM_ENEMIES; i++) {
      createNewEnemy();
    }

    // initialize the ui components
    borderPane.setAlignment(progressLabel, Pos.CENTER);
    uiPane.setTop(progressLabel);
  }

  // Animation loop for the game
  public void playGame() {
    canvas.requestFocus();
    new AnimationTimer() {
      @Override
      public void handle(long now) {

        if (gameOver == 0) {
          update();   // update game logically 
          ui.update();  // update game visually
        }
      }
    }.start();
  }

  // Provides logical update to the game
  private void update() {
    // Check for collision
    List<Enemy> enemiesToRemove = new ArrayList<>();
    List<Enemy> enemiesCopy = new ArrayList<>(enemies);   // Create lists of enemies at each clock cycle

    for (Enemy enemy : enemiesCopy) {
      if (frog.didCollideWith(enemy)) {   // Option: you hit an enemy
        gameOver = -1;  // Set game to loser
        gameOver();   // Handle the game over logic
      }

      // prepare any enemies who have hit either edge of the screen to be removed
      if (enemy.hitEdge()) {
        enemiesToRemove.add(enemy);
      }
    }

    // Remove the enemies that hit the edge
    enemies.removeAll(enemiesToRemove);

    // Create a new enemy for each that was removed
    while (enemies.size() < NUM_ENEMIES) {
      createNewEnemy();
    }

    // Check if you made it to the end
    if (frog.getRow().getIndex() == 0) {  // Option: you made it to the end
      gameOver = 1;   // Set to winner
      gameOver(); // Handle the game over logic
    }

    // Update progress
    if (frogMoved) {
      updateProgress(frog.getRow().getIndex());   // move frog when moved
      frogMoved = false;
    }

    ui.update();  // updates everything on canvas
  }

  // Creates new random enemy at a random place with a random speed and direction
  private void createNewEnemy() {
    // log enemies created
    numEnemiesCreated++;

    /*
     * Create a random direction and speed. 
     * 
     * Note a direction's positivity is the direction it is moving
     * based off of this value we determine the starting x value, weather right or left
     * 
     * Speed is the number of pixels-per-clock-cycle the enemy will be moving along the x axis
     */
    int direction = random.nextInt(11) - 5;
    while (direction == 0) {
      direction = random.nextInt(11) - 5;
    }

    // Logging for ending game screen
    if (direction > 0) {
      numEnemiesRight++;
    } else {
      numEnemiesLeft++;
    }

    // Logging for ending game screen
    avgEnemySpeed = ((avgEnemySpeed * (numEnemiesCreated - 1)) + Math.abs(direction)) / numEnemiesCreated;

    // Don't allow enemies to spawn in safe rows.
    int minRow = 2;
    int maxRow = rows.size() - 2;
    int row;
    do {
      row = random.nextInt(maxRow - minRow + 1) + minRow;
    } while (isSafeRow(row));

    /*
     * Create new threads for each new enemy 
     * 
     * we need to set the variables we chose above to finals before creating a thread. they cannot be 
     * final any eariler since we re-draw as many times as needed for a valid choice to be made.
     */
    final int dir = direction;
    final int r = row;
    Thread enemyThread = new Thread(() -> {   // Create a new thread for the enemy
      Enemy enemy = new Enemy(this.canvas, rows.get(r), dir);
      enemies.add(enemy);

      /*
       * Right moving enemies are set to the left side of the screen, and left moving are set to 
       * the right side of the screen.  This is determined by the positivity of the enemy's direction.
       */
      if (dir > 0) {
        enemy.setX(enemy.DIM_WIDTH + 1);
      } else {
        enemy.setX(GAME_WIDTH - (enemy.DIM_WIDTH + 1));
      }
    });

    enemyThread.start(); // Start the thread for the new enemy
  }

  // Just hard coding the safe rows
  private boolean isSafeRow(int row) {
    return (row == 0 || row == NUM_ROWS - 1 || row == 9 || row == 10 || row == 13 || row == 4 || row == 20);
  }

  /*
   * Movement takes place using the WASD keys. these are the event handlers to make that happen
   */
  private class KeyPressHandler implements EventHandler<KeyEvent> {
    @Override
    public void handle(KeyEvent event) {
      if (gameOver == 0) {
        if (event.getCode() == KeyCode.W) {
          // Handle the W key press event by moving the frog up one row
          moveFrogF();
        } else if (event.getCode() == KeyCode.A) {
          // Handle the A key press event by moving the frog left
          moveFrogL();
        } else if (event.getCode() == KeyCode.D) {
          // Handle the D key press event by moving the frog right
          moveFrogR();
        } else if (event.getCode() == KeyCode.S) {
          // Handle the S key press event by moving the frog down one row
          moveFrogB();
        }
      }
    }
  }

  // Updates the progress label at the top of the screen.
  private void updateProgress(int currentRow) {
    double progress = 100.0 - ((double) currentRow / NUM_ROWS * 100);
    progress = Math.round(progress * 100.0) / 100.0; // Rounds to 2 decimal places
    progressLabel.setText("Progress: " + progress + "%");

    main.updateProgressLabel(progress); // Call the Main class's method to update the progress label in UI
  }

  // Logic for what happens when a game is over
  public int gameOver() {

    endTime = System.nanoTime(); // log ending time for stats screen

    if (gameOver == -1) {
      // Game has a loser, call the losing scene in the Main class
      main.getPrimaryStage().setScene(main.create_losing_scene(main.getPrimaryStage(), this));
    } else if (gameOver == 1) {
      // Game has a winner, call the winning scene in the Main class
      main.getPrimaryStage().setScene(main.create_winning_scene(main.getPrimaryStage(), this));
    }
    return gameOver;
  }

  /*
   * Movement methods:
   * Frog moves in increments of 25px
   */
  double FROG_MOVE = 25;
  private void moveFrogF() {
    if (gameOver == 0) {  // Stop frog from moving if game is over

      // log move count
      numMoves++;

      frog.move(rows.get(frog.getRow().getIndex() - 1));  // move frog
      frog.render(canvas);    // re-render frog
      updateProgress(frog.getRow().getIndex());   // update progress each time frog is moved 
      frogMoved = true;
    }
  }
  private void moveFrogB() {
    // Check the frog is able to move backward
    if (frog.getRow().getIndex() < rows.size() - 1) {
      
      // log move count
      numMoves++;

      frog.move(rows.get(frog.getRow().getIndex() + 1));
      frog.render(canvas);
      updateProgress(frog.getRow().getIndex());
      frogMoved = true;
    }
  }
  private void moveFrogL() {
    if ((frog.getX() - FROG_MOVE) > 0) { // meaning frog CAN move

      // log move count
      numMoves++;

      frog.setX(frog.getX() - FROG_MOVE);
      frog.render(canvas);
      frogMoved = true;
    }
  }
  private void moveFrogR() {
    if ((frog.getX() + FROG_MOVE) < GAME_WIDTH) { // meaning frog CAN move

      // log move count
      numMoves++;

      frog.setX(frog.getX() + FROG_MOVE);
      frog.render(canvas);
      frogMoved = true;
    }
  }

  /*
   * * * Getters and Setters * * *
   */
  public ArrayList<Row> getRows() { return rows; }
  public ArrayList<Enemy> getEnemies() { return enemies; }
  public int getRowSize() { return ROW_SIZE; }
  public UI getUI() { return ui; }
  public Canvas getCanvas() { return canvas; }
  public Frog getFrog() { return frog; }

  // Specific to the stats screen
  public int getNumMoves() { return numMoves; }
  public int getNumEnemiesCreated() { return numEnemiesCreated; }
  public long getStartTime() { return startTime; }
  public long getEndTime() { return endTime; }
  public double getAvgEnemySpeed() { return avgEnemySpeed; }
  public int getNumEnemiesRight() { return numEnemiesRight; }
  public int getNumEnemiesLeft() { return numEnemiesLeft; }
  public long getTimeElapsed() { return endTime - startTime; }
}