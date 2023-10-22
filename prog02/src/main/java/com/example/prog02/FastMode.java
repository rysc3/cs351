package com.example.prog02;

import java.util.Random;

public class FastMode extends Game {
  private Random random;

  public FastMode(int boardSize) {
    super(boardSize);
    this.random = new Random();
  }

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
          handleHerMove();
        }
      }
    }
  }
}
