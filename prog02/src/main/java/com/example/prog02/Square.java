package com.example.prog02;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Square extends StackPane {
  private int row, col;
  private Game game;
  private Circle circle;

  private static final int SQUARE_SIZE = 200;
  private static final double ORIGINAL_STROKE_WIDTH = 2.0;
  private static final Color ORIGINAL_STROKE_COLOR = Color.BLACK;

  public Square(Game game, int row, int col, Color color) {
    this.game = game;
    this.row = row;
    this.col = col;

    // Create a square shape as the background
    Rectangle squareBackground = new Rectangle(SQUARE_SIZE, SQUARE_SIZE); // Adjust the size as needed
    squareBackground.setFill(color);

    // Handle pieces
    this.circle = new Circle(80); // Adjust the size of the circle as needed
    this.circle.setVisible(false);

    // Add the square background and circle to the StackPane
    this.getChildren().addAll(squareBackground, circle);

    // Add a black border to the square
    squareBackground.setStroke(ORIGINAL_STROKE_COLOR);
    squareBackground.setStrokeWidth(ORIGINAL_STROKE_WIDTH);

    game.log("creating a Square");

    update();

    // event listener
    setOnMouseClicked(event -> handleSquareClick());
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public void setCircle(Color color) {
    circle.setVisible(true);
  }

  public Color getCircleColor() {
    return (Color) circle.getFill();
  }

  public void removeCircle() {
    circle.setVisible(false);
  }

  public void update() {
    int[][] gameArr = game.getGameArr();
    int val = gameArr[row][col];
    if (val == 1) {
      circle.setFill(Color.WHEAT);
      circle.setVisible(true);
    } else if (val == 2) {
      circle.setFill(Color.BLACK);
      circle.setVisible(true);
    } else {
      circle.setVisible(false);
    }
  }

  // event listeners to handle clicks
  private void handleSquareClick() {
    System.out.println("(Square Class) Clicked on square at " + row + ", " + col);
    game.handleSquareClick(this);
  }

  public boolean hasPiece() {
    if (circle.isVisible())
      return true;
    return false;
  }

  public void highlight(Color color) {
    Rectangle squareBackground = (Rectangle) this.getChildren().get(0); // Get the square background
    squareBackground.setStrokeWidth(ORIGINAL_STROKE_WIDTH * 2.5); // Increase stroke width by 25%
    squareBackground.setStroke(color); // Change stroke color to green
  }

  public void unhighlight() {
    Rectangle squareBackground = (Rectangle) this.getChildren().get(0); // Get the square background
    squareBackground.setStrokeWidth(ORIGINAL_STROKE_WIDTH); // Restore original stroke width
    squareBackground.setStroke(ORIGINAL_STROKE_COLOR); // Restore original stroke color
  }
}