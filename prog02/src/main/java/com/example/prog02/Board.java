package com.example.prog02;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Board extends GridPane {
  private Square[][] squares;
  private final int boardSize;
  private Game game;

  public Board(Game game, int[][] gameArr, int size) {
    this.game = game;
    this.boardSize = size;
    initializeBoard();
    update();
  }

  // initialize all the squares on the board
  public void initializeBoard() {
    squares = new Square[boardSize][boardSize];

    for (int row = 0; row < boardSize; row++) {
      for (int col = 0; col < boardSize; col++) {
        Color color = (row + col) % 2 == 0 ? Color.LIGHTGRAY : Color.GRAY;
        squares[row][col] = new Square(game, row, col, color);
        add(squares[row][col], col, row);
      }
    }
  }

  public Square getSquare(int row, int col) {
    return squares[row][col];
  }

  /*
   * Loops through and updates each square individually
   */
  public void update() {
    for (int row = 0; row < boardSize; row++) {
      for (int col = 0; col < boardSize; col++) {
        squares[row][col].update();
      }
    }
  }
}
