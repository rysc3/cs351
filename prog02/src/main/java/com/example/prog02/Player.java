package com.example.prog02;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class Player {
  private Color color;
  private int id;
  private Game game;
  private int[][] moves;
  private int direction;

  public Player(Game game, int id, Color color, int direction) {
    this.direction = direction;
    this.game = game;
    this.color = color;
    this.id = id;
    initializeMoves();
  }

  private void initializeMoves() {
    /*
     * This is the easiest way to do this just going to check if I named the player
     * "HER" and if not then assume it is the player. Might come back to this later
     * if I have time but this makes sense for what is required in the project.
     */
    int numRows = game.getBoardSize();
    int numCols = game.getBoardSize();

    moves = new int[numRows * numCols][2]; // Each move is represented by an [x, y] pair
    int index = 0;

    if (id == 2) {
      // Initialize moves for "HER" (player 2)
      for (int col = 0; col < numCols; col++) {
        // "HER" starts at the top row
        moves[index][0] = 0; // Row 0
        moves[index][1] = col;
        index++;
      }
    } else {
      // Initialize moves for the other player (player 1)
      for (int row = 0; row < numRows; row++) {
        for (int col = 0; col < numCols; col++) {
          if ((row + col) % 2 == 0) {
            // Example: Add moves for even (black) squares
            moves[index][0] = row;
            moves[index][1] = col;
            index++;
          }
        }
      }
    }
    toString();
  }

  public int[][] getMoves() { return moves; }
  public int getDirection() { return direction; }
  public Color getColor() { return color; }
  public int getId() { return id; }

  /*
   * This will handle removing pieces from a player when they are punished 
   * I'm just going to do this instead of a HER class.
   */
  public void setPunishment(int[][] punishment) {
  }

  @Override 
  public String toString(){
    StringBuilder result = new StringBuilder();

    for (int[] row : moves) {
      for (int value : row) {
        result.append(value).append(" ");
      }
      result.append("\n");
    }
    return result.toString();
  }
}
