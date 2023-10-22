package com.example.prog02;

import java.util.Random;

public class AutoMode extends Game {
  private int numGames;
  private int her1Wins;
  private int her2Wins;

  public AutoMode(int boardSize, int numGames) {
    super(boardSize);
    this.numGames = numGames;
    this.her1Wins = 0;
    this.her2Wins = 0;
  }

  public int getHer1Wins() {
    return her1Wins;
  }

  public int getHer2Wins() {
    return her2Wins;
  } 

  public void runSimulation(){
    for (int i = 0; i < numGames; i++) {
      runGame();
    }
  }

  private void runGame() {
    // Reset the game for a new match
    restartGame();

    while (hasWinner() == -1) {
      moveHER();
      next();
    }

    int winner = hasWinner();
    if (winner == 1) {
      her1Wins++;
    } else if (winner == 2) {
      her2Wins++;
    }
  }

  @Override
  public void handleHerMove() {
    log("SlowMode handleHerMove");

    // Create a random number generator
    Random random = new Random();

    // Loop through the board to find HER's pieces
    for (int i = 0; i < boardSize; i++) {
      for (int j = 0; j < boardSize; j++) {
        if (gameArr[i][j] == current.getId()) {
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
        if (gameArr[i][j] == current.getId()) {
          handleHerMove();
        }
      }
    }
  }
}
