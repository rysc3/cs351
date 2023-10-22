package com.example.prog02;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextField;


public class HexaPawn extends Application {
  private static final int BOARD_SIZE = 3; // Size of each game board

  private enum GameMode {
    SLOW_MODE, FAST_MODE, AUTO_MODE
  }

  private Stage primaryStage;
  private Scene gameModeScene;
  private Scene gamePlayScene;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    primaryStage = stage;
    primaryStage.setTitle("Hexa-something game");

    // Create scenes
    gameModeScene = createGameModeScene();
    // gamePlayScene = createGamePlayScene(GameMode.SLOW_MODE, new SlowMode(BOARD_SIZE));

    // Set the game mode scene as the initial scene
    primaryStage.setScene(gameModeScene);
    primaryStage.show();
  }

  private Scene createGameModeScene() {
    VBox vbox = new VBox(10); // 10 pixels spacing between components
    vbox.setPadding(new Insets(10));

    Text title = new Text("HexaPawn");
    title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

    ComboBox<String> gameModeComboBox = new ComboBox<>();
    gameModeComboBox.getItems().addAll("Slow Mode", "Fast Mode", "Auto Mode");
    gameModeComboBox.setValue("Select Game Mode"); // Default selection

    Button startButton = new Button("Start Game");
    startButton.setOnAction(e -> {
      String selectedMode = gameModeComboBox.getValue();
      switch (selectedMode) {
        case "Slow Mode":
          SlowMode game = new SlowMode(BOARD_SIZE);
          gamePlayScene = createGamePlayScene(GameMode.SLOW_MODE, game);
          break;
        case "Fast Mode":
          FastMode game2 = new FastMode(BOARD_SIZE);
          gamePlayScene = createGamePlayScene(GameMode.FAST_MODE, game2);
          break;
        case "Auto Mode":
          int numGames = 1;

          // Create a TextField for entering the number of games
          TextField numGamesTextField = new TextField("1"); // Default value is 1
          numGamesTextField.setPromptText("Enter number of games");
          numGamesTextField.setMaxWidth(150); // Set the maximum width
          vbox.getChildren().addAll(title, gameModeComboBox, numGamesTextField, startButton);
          // add event listener from the numgames field to update numGames int

          AutoMode game3 = new AutoMode(BOARD_SIZE, numGames);
          gamePlayScene = createGamePlayScene(GameMode.AUTO_MODE, game3);
          break;
        default:
          return; // No valid game mode selected
      }
      primaryStage.setScene(gamePlayScene);
    });

    vbox.getChildren().addAll(title, gameModeComboBox, startButton);

    return new Scene(vbox, 775, 650);
  }

  private Scene createGamePlayScene(GameMode gameMode, Game game) {
    BorderPane borderPane = new BorderPane();

    StackPane gameContainer = createGameContainer(gameMode);
    VBox controlBox = createControlBox(gameMode, game);
    gameContainer.getChildren().add(game.getBoard());

    // Set the left 2/3 of the screen to the game container and the right 1/3 to the
    // control box
    borderPane.setCenter(gameContainer);
    borderPane.setRight(controlBox);

    return new Scene(borderPane, 800, 650);
  }

  private StackPane createGameContainer(GameMode gameMode) {
    StackPane stackPane = new StackPane();
    GridPane gameGrid = createGameGrid(40);
    stackPane.getChildren().add(gameGrid);
    stackPane.setPadding(new Insets(20)); // Add padding to center the game

    // Create a back button for returning to the game mode selection scene
    Button backButton = new Button("Back");
    backButton.setOnAction(e -> primaryStage.setScene(gameModeScene));
    stackPane.getChildren().add(backButton);

    return stackPane;
  }

  private GridPane createGameGrid(int size) {
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20));
    // You can add game-specific logic here to populate the grid.
    return grid;
  }

  private VBox createControlBox(GameMode gameMode, Game game) {
    VBox vbox = new VBox(10); // 10 pixels spacing between components
    vbox.setPadding(new Insets(10));

    // Add game-specific controls based on the selected game mode
    if (gameMode == GameMode.SLOW_MODE) {
        vbox.getChildren().add(getUniversalControls(game));
        Button her = new Button("HER Play."); // tells HER to play what they have calculated to be the best move
        her.setId("herButton");
        her.setOnAction(e -> game.handleHerMove());

        vbox.getChildren().addAll(her);
    } else if (gameMode == GameMode.FAST_MODE) {
        vbox.getChildren().add(getUniversalControls(game));
        // Add controls for Fast Mode
    } else if (gameMode == GameMode.AUTO_MODE) {
        vbox.getChildren().add(getUniversalControls(game));
        // Add controls for Auto Mode
    }

    // Create a "Play Again" button
    Button playAgainButton = new Button("Play Again");
    playAgainButton.setOnAction(e -> {
        // Restart the game within the current game mode
        game.restartGame();
    });

    // Create back button
    Button backButton = new Button("Select Mode");
    backButton.setOnAction(e -> primaryStage.setScene(gameModeScene));

    vbox.getChildren().addAll(playAgainButton, backButton);

    return vbox;
}

  private VBox getUniversalControls(Game game) {
    VBox vbox = new VBox(10); // 10 pixels spacing between components
    vbox.setPadding(new Insets(10));

    // Text field to display the current player
    Text playerText = new Text("Move Piece: ");
    playerText.setFont(Font.font("Arial", FontWeight.BOLD, 14));

    // Create an HBox for centering the moveButtonGrid
    HBox hbox = new HBox();
    hbox.setAlignment(Pos.CENTER); // Center the contents horizontally

    // Create a grid for move buttons
    GridPane moveButtonGrid = new GridPane();
    moveButtonGrid.setHgap(10);
    moveButtonGrid.setVgap(10);
    moveButtonGrid.setPadding(new Insets(10));

    // Create up, down, left, and right arrow buttons
    Button upButton = new Button("↑");
    Button downButton = new Button("↓");
    Button leftButton = new Button("←");
    Button rightButton = new Button("→");

    // Set unique IDs for each button for easy identification
    upButton.setId("upButton");
    upButton.setOnAction(e -> game.handleButtonClick(upButton));

    downButton.setId("downButton");
    downButton.setOnAction(e -> game.handleButtonClick(downButton));

    leftButton.setId("leftButton");
    leftButton.setOnAction(e -> game.handleButtonClick(leftButton));

    rightButton.setId("rightButton");
    rightButton.setOnAction(e -> game.handleButtonClick(rightButton));

    // Add the buttons to the grid
    moveButtonGrid.add(new StackPane(), 0, 0); // Empty space
    moveButtonGrid.add(upButton, 1, 0); // Up button
    moveButtonGrid.add(new StackPane(), 2, 0); // Empty space
    moveButtonGrid.add(leftButton, 0, 1); // Left button
    moveButtonGrid.add(new StackPane(), 1, 1); // Empty space
    moveButtonGrid.add(rightButton, 2, 1); // Right button
    moveButtonGrid.add(new StackPane(), 0, 2); // Empty space
    moveButtonGrid.add(downButton, 1, 2); // Down button
    moveButtonGrid.add(new StackPane(), 2, 2); // Empty space

    // Add the moveButtonGrid to the HBox
    hbox.getChildren().add(moveButtonGrid);

    // Add the HBox to the VBox
    vbox.getChildren().addAll(playerText, hbox);

    return vbox;
  }

}
