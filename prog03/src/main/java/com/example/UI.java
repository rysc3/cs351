package com.example;

import java.util.ArrayList;
import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.GraphicsContext;

public class UI extends StackPane {
  private Canvas canvas;
  private ArrayList<Row> rows;
  private Frogger frogger;
  private ArrayList<Image> enemySprites; // Store enemy sprite images
  private static final int NUM_ENEMY_SPRITES = 10; // Number of enemy sprite images

  public UI(Frogger frogger) {
    // initialize variables 
    this.frogger = frogger;
    this.canvas = frogger.getCanvas();
    this.rows = frogger.getRows();
    this.enemySprites = new ArrayList<>();

    loadEnemySprites(); // Load enemy sprite images
    initializePane();   // StackPane Styling
  }

  public void initializePane() {
    this.setStyle("-fx-background-color: lightgray;");
  }

  /*
   * Load the image for each enemy
   * 
   * Enemy getImage() returns a string which is the path to the image
   */
  private void loadEnemySprites() {
    for (int i = 1; i <= NUM_ENEMY_SPRITES; i++) {
      Image enemySprite = new Image("file:../sprites/enemy" + i + ".png");
      enemySprites.add(enemySprite);
    }
  }

  /*
   * 1. Clear canvas 
   * 2. re-draw everything
   * 
   * this is called once every clock cycle in the animation timer within Frogger.java
   */
  public void update() {
    GraphicsContext gc = canvas.getGraphicsContext2D();

    // Clear the canvas
    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    // Draw the rows directly on the GraphicsContext
    for (Row row : rows) {
      double y = row.getY() - Row.HEIGHT / 2;
      double rowWidth = Row.WIDTH;

      gc.setFill(row.getColor());
      gc.fillRect(0, y, rowWidth, Row.HEIGHT);

      gc.setStroke(Color.BLUE);
      gc.strokeRect(0, y, rowWidth, Row.HEIGHT);

      // Add labels with row indices at the bottom right of each row
      String rowIndexLabel = Integer.toString(row.getIndex());
      double labelX = rowWidth - 20;
      double labelY = y + Row.HEIGHT - 15;
      gc.setFill(Color.BLACK); // Set label color
      gc.fillText(rowIndexLabel, labelX, labelY);
    }

    // Get Frog Image
    InputStream frogStream = getClass().getResourceAsStream(frogger.getFrog().getImage());
    if (frogStream != null) {
      Image frogImage = new Image(frogStream);
      // Show tha crab
      renderFrogWithImage(frogger.getFrog(), frogImage);
    }

    // get the enemy images for each enemy on the screen 
    for (Enemy enemy : frogger.getEnemies()) {
      // show the enemy image
      InputStream enemyStream = getClass().getResourceAsStream(enemy.getImage());
      if (enemyStream != null) {
        Image enemyImage = new Image(enemyStream);
        renderEnemyWithImage(enemy, enemyImage);
      }
      // move enemy
      enemy.move();
    }
  }

  /*
   * Helper methods to set the images ontop of the squares for frog and enemies
   */
  private void renderEnemyWithImage(Enemy enemy, Image image) {
    double enemyX = enemy.getX();
    double enemyY = enemy.getY();
    double enemyWidth = enemy.getWidth();
    double enemyHeight = enemy.getHeight();

    double imageX = enemyX + (enemyWidth - image.getWidth()) / 2;
    double imageY = enemyY + (enemyHeight - image.getHeight()) / 2;

    // Draw the enemy image on top of the square
    canvas.getGraphicsContext2D().drawImage(image, imageX, imageY);
  }

  private void renderFrogWithImage(Frog frog, Image image) {
    double x = frog.getX();
    double y = frog.getY();
    double width = frog.getWidth();
    double height = frog.getHeight();

    double imageX = x + (width - image.getWidth()) / 2;
    double imageY = y + (height - image.getHeight()) / 2;

    // Draw the enemy image on top of the square
    canvas.getGraphicsContext2D().drawImage(image, imageX, imageY);
  }

  public void removeEnemyImage(Enemy enemy) {
    GraphicsContext gc = canvas.getGraphicsContext2D();

    // Clear the area occupied by the enemy on the canvas
    gc.clearRect(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
  }
}