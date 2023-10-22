package com.example.prog02;

import java.util.Random;

import javafx.scene.paint.Color;

/*
 * Mode should move one turn at a time. HER will generate arrows w/ color probability 
 * based on each possible move. There should be a button to allow HER to choose 
 * the best move, or the ability for the user to override and choose HER's move for 
 * them. 
 * 
 * To do this we can just override a few of the main methods in a game that handle
 * the turn logic, as well as adding logic for the arrows which doesn't exist in the 
 * game class. 
 */
public class SlowMode extends Game {

  public SlowMode(int boardSize) {
    super(boardSize);
  }

  /*
   * This method overrides because we need to add the visualization for all the
   * possible moves,
   * but wait for user input at each turn. The player can either choose to make
   * HER's move for
   * them, or hit the button for HER to choose the best move.
   */
  @Override
  public void handleHerMove() {
    log("SlowMode handleHerMove");
    if (current.getId() != 2) {
      log("not player 2 using HER button");
      return;
    }

    // Create a random number generator
    Random random = new Random();

    // Loop through the board to find HER's pieces
    for (int i = 0; i < boardSize; i++) {
      for (int j = 0; j < boardSize; j++) {
        if (gameArr[i][j] == 2) {
          Boolean[][] moves = getMoves(2, i, j);
          int validMovesCount = 0;

          // Count the number of valid moves
          for (int k = 0; k < boardSize; k++) {
            for (int l = 0; l < boardSize; l++) {
              if (moves[k][l]) {
                validMovesCount++;
              }
            }
          }

          // If HER has valid moves, select a random move
          if (validMovesCount > 0) {
            int randomMoveIndex = random.nextInt(validMovesCount);
            int moveCounter = 0;

            // Loop again to find the randomly selected move
            for (int k = 0; k < boardSize; k++) {
              for (int l = 0; l < boardSize; l++) {
                if (moves[k][l]) {
                  if (moveCounter == randomMoveIndex) {
                    // Make the selected move
                    move(current, board.getSquare(i, j), board.getSquare(k, l));

                    return; // HER's move is done
                  }
                  moveCounter++;
                }
              }
            }
          }
        }
      }
    }
  }

  @Override
  public void moveHER() {
    log("moveHER");
    // loop through the board, for each piece, plug into the getMoves method, then
    // draw
    // arrows for each valid move.
    for (int i = 0; i < boardSize; i++) {
      for (int j = 0; j < boardSize; j++) {
        if (gameArr[i][j] == 2) {
          Boolean[][] moves = getMoves(2, i, j);

          // Now draw an arrow for each move
          for (int k = 0; k < boardSize; k++) {
            for (int l = 0; l < boardSize; l++) {
              if(moves[k][l] == null){
                log("null move");
              }else if (moves[k][l]) {
                board.getSquare(k, l).highlight(Color.GREEN);

                log("highlighting valid moves");
              }
            }
          }
        }
      }
    }
  }
}