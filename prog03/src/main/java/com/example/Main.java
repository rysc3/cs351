package com.example;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.animation.Interpolator;
import javafx.scene.layout.StackPane;
import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.animation.TranslateTransition;

public class Main extends Application {
  // Start screen sizes
  public static final int START_WIDTH = 750;
  public static final int START_HEIGHT = 500;
  // Game screen sizes
  public static final int GAME_WIDTH = 750;
  public static final int GAME_HEIGHT = 950;

  // Game Components
  public Frogger frogger;
  private BorderPane uiPane;
  private Stage primaryStage;

  // Conclusion Scenes
  private Scene losingScene;
  private Scene winningScene;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage; // Initialize primaryStage here
    Scene startScene = create_start_scene(primaryStage);
    primaryStage.setScene(startScene);
    primaryStage.setTitle("Frogger");
    primaryStage.show();
  }

  // Creates the starting screen logically and visually.
  private Scene create_start_scene(Stage primaryStage) {
    StackPane startLayout = new StackPane();
    Scene startScene = new Scene(startLayout, START_WIDTH, START_HEIGHT);

    Label titleLabel = new Label("Frogger");
    titleLabel.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 36px; -fx-font-weight: bold;");
    StackPane.setAlignment(titleLabel, javafx.geometry.Pos.TOP_CENTER);
    startLayout.getChildren().add(titleLabel);

    Button startButton = new Button("Play");
    startLayout.getChildren().add(startButton);

    // Button to startthe game
    startButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        primaryStage.setScene(create_game_scene(primaryStage)); // set scene
        frogger.playGame();
      }
    });

    // Make the title bounce lol
    TranslateTransition tt = new TranslateTransition(Duration.seconds(2), titleLabel);
    tt.setByY(20);
    tt.setCycleCount(TranslateTransition.INDEFINITE);
    tt.setAutoReverse(true);
    tt.setInterpolator(Interpolator.SPLINE(0.1, 0.2, 0.3, 0.4));
    tt.play();

    return startScene;
  }

  // Creates gameplay scene logically and visually
  private Scene create_game_scene(Stage primaryStage) {
    // Create the Canvas
    Canvas canvas = new Canvas(GAME_WIDTH, GAME_HEIGHT);
    this.frogger = new Frogger(canvas, this);

    // Create the main UI layout (which is a StackPane)
    UI ui = frogger.getUI();
    ui.setPrefSize(GAME_WIDTH, GAME_HEIGHT);

    // Create a layout for the progress section
    uiPane = new BorderPane();

    // Create the progress label
    Label progressLabel = createProgressLabel();
    uiPane.setTop(progressLabel);

    // Add the canvas to the center
    uiPane.setCenter(canvas);

    // Add the BorderPane (with progress section and canvas) to the main UI layout
    ui.getChildren().add(uiPane);

    // Create the game scene
    Scene gameScene = new Scene(ui, GAME_WIDTH, GAME_HEIGHT + (START_HEIGHT / 15)); // Adjust the scene height

    return gameScene;
  }

  /*
   * Creates winning and losing screen based off of the frogger gameOver return value
   */
  public Scene create_winning_scene(Stage primaryStage, Frogger frogger) {
    StackPane winningLayout = new StackPane();
    Scene winningScene = new Scene(winningLayout, START_WIDTH, START_HEIGHT);

    Label winningLabel = new Label("You Won!");
    winningLabel.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 36px; -fx-font-weight: bold;");
    StackPane.setAlignment(winningLabel, Pos.TOP_CENTER);
    winningLayout.getChildren().add(winningLabel);

    // Display the statistics
    int numMoves = frogger.getNumMoves();
    int numEnemiesCreated = frogger.getNumEnemiesCreated();
    long startTime = frogger.getStartTime();
    long endTime = System.nanoTime();
    long timeElapsed = (endTime - startTime) / 1_000_000_000; // Convert to seconds
    double avgEnemySpeed = frogger.getAvgEnemySpeed();
    int numEnemiesRight = frogger.getNumEnemiesRight();
    int numEnemiesLeft = frogger.getNumEnemiesLeft();

    // Format the average enemy speed
    String formattedAvgSpeed = String.format("%.1f", avgEnemySpeed);

    Label statisticsLabel = new Label("Statistics:\n" +
        "Number of Moves: " + numMoves + "\n" +
        "Number of Enemies Created: " + numEnemiesCreated + "\n" +
        "Time Elapsed: " + timeElapsed + " Seconds" + "\n" +
        "Average Enemy Speed: " + formattedAvgSpeed + "\n" +
        "Number of Enemies Moving Right: " + numEnemiesRight + "\n" +
        "Number of Enemies Moving Left: " + numEnemiesLeft);

    statisticsLabel.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 16px; -fx-font-weight: bold;");
    StackPane.setAlignment(statisticsLabel, Pos.CENTER);
    winningLayout.getChildren().add(statisticsLabel);

    Button backButton = new Button("Back");
    StackPane.setAlignment(backButton, Pos.BOTTOM_CENTER);
    StackPane.setMargin(backButton, new Insets(0, 0, 20, 0)); // Add spacing from the bottom
    winningLayout.getChildren().add(backButton);

    backButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        primaryStage.setScene(create_start_scene(primaryStage));
      }
    });

    return winningScene;
  }
  public Scene create_losing_scene(Stage primaryStage, Frogger frogger) {
    StackPane losingLayout = new StackPane();
    Scene losingScene = new Scene(losingLayout, START_WIDTH, START_HEIGHT);

    Label losingLabel = new Label("You Lost!");
    losingLabel.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 36px; -fx-font-weight: bold;");
    StackPane.setAlignment(losingLabel, Pos.TOP_CENTER);
    losingLayout.getChildren().add(losingLabel);

    // Display the statistics on the losing screen as well
    int numMoves = frogger.getNumMoves();
    int numEnemiesCreated = frogger.getNumEnemiesCreated();
    long startTime = frogger.getStartTime();
    long endTime = System.nanoTime();
    long timeElapsed = (endTime - startTime) / 1_000_000_000; // Convert to seconds
    double avgEnemySpeed = frogger.getAvgEnemySpeed();
    int numEnemiesRight = frogger.getNumEnemiesRight();
    int numEnemiesLeft = frogger.getNumEnemiesLeft();

    // Format the average enemy speed
    String formattedAvgSpeed = String.format("%.1f", avgEnemySpeed);

    Label statisticsLabel = new Label("Statistics:\n" +
            "Number of Moves: " + numMoves + "\n" +
            "Number of Enemies Created: " + numEnemiesCreated + "\n" +
            "Time Elapsed: " + timeElapsed + " Seconds" + "\n" +
            "Average Enemy Speed: " + formattedAvgSpeed + "\n" +
            "Number of Enemies Moving Right: " + numEnemiesRight + "\n" +
            "Number of Enemies Moving Left: " + numEnemiesLeft);

    statisticsLabel.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 16px; -fx-font-weight: bold;");
    StackPane.setAlignment(statisticsLabel, Pos.CENTER);
    StackPane.setMargin(statisticsLabel, new Insets(10, 0, 0, 0)); // Add spacing from the top
    losingLayout.getChildren().add(statisticsLabel);

    Button backButton = new Button("Back");
    StackPane.setAlignment(backButton, Pos.BOTTOM_CENTER);
    StackPane.setMargin(backButton, new Insets(0, 0, 20, 0)); // Add spacing from the bottom
    losingLayout.getChildren().add(backButton);

    backButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            primaryStage.setScene(create_start_scene(primaryStage));
        }
    });

    return losingScene;
}

  private Label createProgressLabel() {
    Label progressLabel = new Label("Progress: 0%");
    progressLabel.setId("progressLabel"); // You can add CSS styling here
    // Set the height of the progress label
    progressLabel.setPrefHeight(START_HEIGHT / 15); // 5 times taller

    // Center the label
    BorderPane.setAlignment(progressLabel, Pos.CENTER);

    // Set the font style to match the title
    progressLabel.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 24px; -fx-font-weight: bold;");

    // Add more styling and customization as needed
    return progressLabel;
  }

  public void updateProgressLabel(double progress) {
    String progressText = String.format("Progress: %.2f%%", progress);
    Label progressLabel = (Label) uiPane.getTop(); // Get the progress label from the UI
    progressLabel.setText(progressText);
  }

  /*
   * * * Getters & Setters * * *
   */
  public Stage getPrimaryStage() { return this.primaryStage; }
  public Scene getLosingScene() { return losingScene; }
  public Scene getWinningScene() { return winningScene; }
}