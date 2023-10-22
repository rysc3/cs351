package com.example.prog02;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

/*
 * Base class for which each of the game modes:
 * - FastMode
 * - SlowMode
 * - AutoMode 
 * 
 * extend from. Should handle all the general logic that controls the game, and 
 * the individual mode classes will override the shared methods, such as .next() 
 * and .herMove() in order to make them function as expected for that mode. 
 */
class Game {
  protected int boardSize; // Save size of board to make it easier
  protected int[][] gameArr; // Game is the logical representation of the game
  protected Board board; // Board handles the visualization of the game logic
  protected List<Player> players;
  protected Player current;

  // Squares to handle movement
  protected Square select1;
  protected Square select2;

  // Handle tracking HER moves & punishment
  protected ArrayList<ArrayList<Square>> moveLog = new ArrayList<>();
  protected ArrayList<ArrayList<Square>> punishment = new ArrayList<>();

  public Game(int boardSize) {
    this.boardSize = boardSize;
    this.players = new ArrayList<>();
    this.gameArr = new int[boardSize][boardSize];
    initializeGame();
    log("Constructor");
  }

  public void printMoveLogAndPunishment() {
    System.out.println("Move Log:");
    for (ArrayList<Square> movePair : moveLog) {
      System.out.print("[");
      System.out.print(movePair.get(0));
      System.out.print(", ");
      System.out.print(movePair.get(1));
      System.out.println("]");
    }

    System.out.println("Punishment:");
    for (ArrayList<Square> movePair : punishment) {
      System.out.print("[");
      System.out.print(movePair.get(0));
      System.out.print(", ");
      System.out.print(movePair.get(1));
      System.out.println("]");
    }
  }

  private void initializeGame() {

    // initialize players
    if (players.size() == 0) {
      players.add(new Player(this, 1, Color.WHEAT, 1));
      players.add(new HER(this, 2, Color.BLACK, -1));
    }

    // set initial player
    current = players.get(0);

    // reset all pieces
    for (int row = 0; row < boardSize; row++) {
      for (int col = 0; col < boardSize; col++) {
        if (row == 0) {
          gameArr[row][col] = 2; // Top row set to 2
        } else if (row == boardSize - 1) {
          gameArr[row][col] = 1; // Bottom row set to 1
        } else {
          gameArr[row][col] = 0; // All other rows set to 0
        }
      }
    }

    // initialize board
    if (board == null) {
      this.board = new Board(this, gameArr, boardSize);
    } else {
      board.update();
    }
  }

  /*
   * Getters & Setters
   */
  public Board getBoard() {
    return board;
  }

  public int getBoardSize() {
    return boardSize;
  }

  public int[][] getGameArr() {
    return gameArr;
  }

  /*
   * Method to switch players, first checks if there is a winner, if not then
   * sets current to the next player.
   */
  public void next() {
    unhighlightAll();
    // check if there is a winner
    if (hasWinner() != -1) {
      System.out.println("Player " + current.getId() + " wins!");
      return;
    }

    // get next player
    int index = players.indexOf(current);
    current = players.get((index + 1) % players.size());

    log("SlowMode next");

    // handle HER's move
    if (current.getId() == 2) {
      moveHER();
    }
  }

  /*
   * this method is to be used when a user wants to play a second and further
   * subseqent games within the same mode. This is the way to restart the game
   * while still saving HER's memory of the previous games, if you go back to
   * select gameMode it will clear this information.
   */
  public void restartGame() {
    moveLog.clear();
    initializeGame();
    board.update();
  }

  /*
   * Determine outcome of the game:
   * 1: player wins
   * 2: HER wins
   * 3: stalemate
   * 4: HER starts game with no valid moves
   */
  public int hasWinner() {
    int winner = -1;
    // Check if either player made it to the other side
    for (int i = 0; i < boardSize; i++) {
      if (gameArr[0][i] == 1) {
        winner = 1;
      } // player made it to HER side
      if (gameArr[boardSize - 1][i] == 2) {
        return 2;
      } // HER made it to player side
    }

    // Check if either ran out of pieces
    if (players.get(0).getMoves() == null) {
      return 2;
    } // player ran out of pieces
    if (players.get(1).getMoves() == null) {
      return 1;
    } // HER ran out of pieces

    // HER has been punished too much and can't move
    if (players.get(1).getMoves().length == 0) {
      return 4;
    }

    // Check if player 2 (HER) wins
    if (winner == 2) {
      // If the moveLog is not empty, add the most recent unique move pair to the
      // punishment list
      if (!moveLog.isEmpty()) {
        ArrayList<Square> lastMovePair = moveLog.get(moveLog.size() - 1);
        if (!punishment.contains(lastMovePair)) {
          punishment.add(lastMovePair);
        }
      }

    }

    return winner;
  }

  /*
   * Handles first and second click for player movement
   */
  public void setSelect1(Square select1) {
    this.select1 = select1;
  }

  public void setSelect2(Square select2) {
    this.select2 = select2;
  }

  public void reset() {
    select1 = null;
    select2 = null;
  }

  public void handleSquareClick(Square square) {
    setSelect1(square);
    System.out.println("Current Player: " + current.getId() + "\n");
    System.out.println("(Game Class) Select1: " + select1.getRow() + ", " + select1.getCol());

    // Check if the selected square belongs to the current player
    if (gameArr[select1.getRow()][select1.getCol()] == current.getId()) {
      Boolean[][] validMoves = getMoves(current.getId(), select1.getRow(), select1.getCol());
      System.out.println(movesToString(validMoves));
    } else {
      System.out.println("Invalid selection: The selected square does not belong to the current player.");
    }
    toString();
  }

  /*
   * This fucntion handles assigning select2 based off of select1 & the player's
   * direction
   */
  public void handleButtonClick(Button button) {
    // Cannot assign select2 before select1
    if (select1 == null) {
      System.out.println("(Game) No select1");
      return;
    }

    int playerDirection = current.getDirection();
    int row = select1.getRow();
    int col = select1.getCol();

    switch (button.getId()) {
      case "upButton":
        row -= playerDirection;
        break;
      case "downButton":
        row += playerDirection;
        log("Down Button");
        break;
      case "leftButton":
        if (playerDirection == 1) {
          row -= 1;
          col -= 1; // Move diagonally left for player 1
          log("Left Button Dir: 1");
        } else if (playerDirection == -1) {
          row += 1;
          col -= 1; // Move diagonally left for player 2
          log("Left Button Dir: -1");
        }
        break;
      case "rightButton":
        if (playerDirection == 1) {
          row -= 1;
          col += 1; // Move diagonally right for player 1
          log("Right Button Dir: 1");
        } else if (playerDirection == -1) {
          row += 1;
          col += 1; // Move diagonally right for player 2
          log("Left Button Dir: -1");
        }
        break;
      default:
        System.out.println("Invalid button ID");
        return;
    }

    // Check if the new position is within the gameArr bounds
    if (row >= 0 && row < boardSize && col >= 0 && col < boardSize) {
      select2 = board.getSquare(row, col);
      System.out.println("(Game Class) Select2: " + select2.getRow() + ", " + select2.getCol());
      move(current, select1, select2);
    } else {
      System.out.println("Invalid move: Out of bounds.");
    }
  }

  /*
   * Handle valid movement. We do all the heavy lifting in the player class when
   * we generate the valid moves, now we just need to check if this is part of the
   * valid moves, if so, move & update valid moves, if not then do nothing.
   */
  public void move(Player player, Square start, Square end) {
    // Check if either start or end square is null
    if (start == null || end == null) {
      System.out.println("No moves selected.");
      return;
    }

    int startRow = start.getRow();
    int startCol = start.getCol();
    int endRow = end.getRow();
    int endCol = end.getCol();

    // Check if the move is out of bounds
    if (endRow < 0 || endRow >= boardSize || endCol < 0 || endCol >= boardSize) {
      System.out.println("Trying to move off the board.");
      return;
    }

    // For other players, continue with the existing logic
    Boolean[][] moves = getMoves(player.getId(), startRow, startCol);
    if (endRow >= boardSize || endCol >= boardSize || endRow < 0 || endCol < 0) {
      System.out.println("(Game) (Move) invalid index for new move.");
      return;
    }
    if (moves[endRow][endCol]) {
      gameArr[startRow][startCol] = 0; // remove old piece
      gameArr[endRow][endCol] = player.getId(); // place new piece

      // Create move pairs and add them to the moveLog
      // ArrayList<Square> movePair = new ArrayList<>();
      // movePair.add(start);
      // movePair.add(end);
      addToMoveLog(start, end);

      /*
       * - reset - clears board selections for movement
       * - udpate - updates board visualization, places new pieces and removes moved
       * ones
       * - next - checks for winner, if none, moves to next player
       */
      reset();
      board.update();
      next();
    } else {
      System.out.println("Invalid move: Not a valid move.");
    }
  }

  /*
   * Take a row & col as input, and return a boolean arrray of valid moves.
   * Intended use:
   * pass in the coordinate of the selected square. This will loop through and
   * return an
   * array of all the possible moves from this location, taking into account
   * - Player direction
   * - Capture-able pieces
   * - Edges of board
   * 
   * After running this function, we should be able to just take input from user
   * on move()
   * and if the second location passed into move() is a true, then we allow it.
   */
  public Boolean[][] getMoves(int id, int row, int col) {
    log("getting moves");
    Boolean[][] moves = new Boolean[boardSize][boardSize];
    for (int r = 0; r < boardSize; r++) {
      for (int c = 0; c < boardSize; c++) {
        moves[r][c] = false;
      }
    }

    // Check if the player has no valid moves
    boolean hasValidMoves = false;

    if (current.getDirection() > 0) { // Moving from top of screen down
      if (row > 0) {
        if (gameArr[row - 1][col] == 0) {
          moves[row - 1][col] = true; // Move straight forward is empty
          hasValidMoves = true;
        }
        if (col > 0 && gameArr[row - 1][col - 1] != current.getId() && gameArr[row - 1][col - 1] != 0) {
          moves[row - 1][col - 1] = true; // Move diagonally left if opponent
          hasValidMoves = true;
        }
        if (col < boardSize - 1 && gameArr[row - 1][col + 1] != current.getId() && gameArr[row - 1][col + 1] != 0) {
          moves[row - 1][col + 1] = true; // Move diagonally right if opponent
          hasValidMoves = true;
        }
      }
    } else { // Moving from bottom of screen up
      if (row < boardSize - 1) {
        if (gameArr[row + 1][col] == 0) {
          moves[row + 1][col] = true; // Move straight forward is empty
          hasValidMoves = true;
        }
        if (col > 0 && gameArr[row + 1][col - 1] != current.getId() && gameArr[row + 1][col - 1] != 0) {
          moves[row + 1][col - 1] = true; // Move diagonally left if opponent
          hasValidMoves = true;
        }
        if (col < boardSize - 1 && gameArr[row + 1][col + 1] != current.getId() && gameArr[row + 1][col + 1] != 0) {
          moves[row + 1][col + 1] = true; // Move diagonally right if opponent
          hasValidMoves = true;
        }
      }
    }

    // If there are no valid moves, it's a stalemate
    if (!hasValidMoves) {
      return null;
    }

    return moves;
  }

  @Override
  public String toString() {
    String str = "";
    str += "Game Board: \n";
    for (int row = 0; row < boardSize; row++) {
      for (int col = 0; col < boardSize; col++) {
        str += "  ";
        str += gameArr[row][col] + " ";
      }
      str += "\n";
    }
    return str;
  }

  public String movesToString(Boolean[][] array) {
    String str = "";
    str += "Moves: \n";
    for (int row = 0; row < boardSize; row++) {
      for (int col = 0; col < boardSize; col++) {
        str += "  ";
        str += array[row][col] ? "T " : "F ";
      }
      str += "\n";
    }
    return str;
  }

  public void log(String message) {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    if (stackTrace.length >= 3) {
      StackTraceElement caller = stackTrace[3]; // Adjusted index to get the calling function
      String className = caller.getClassName();
      String methodName = caller.getMethodName();
      System.out.println("I am in " + methodName + " function in " + className + " class: " + message);
    } else {
      System.out.println("Could not determine caller information: " + message);
    }
  }

  public void unhighlightAll() {
    for (int row = 0; row < boardSize; row++) {
      for (int col = 0; col < boardSize; col++) {
        board.getSquare(row, col).unhighlight();
      }
    }
  }

  public void addToMoveLog(Square square1, Square square2) {
    ArrayList<Square> movePair = new ArrayList<>();
    movePair.add(square1);
    movePair.add(square2);
    moveLog.add(movePair);
  }

  /*
   * general HER logic
   */

  public void handleHerMove() {
  }

  public void moveHER() {
  }

}