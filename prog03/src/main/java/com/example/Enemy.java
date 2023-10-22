package com.example;

import java.util.Random;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Rectangle;

public class Enemy extends Rectangle {
  public static final double DIM_WIDTH = 15;

  private Row row;
  private double vX; // Speed of the enemy
  private Canvas canvas;
  private String image;

  public Enemy(Canvas canvas, Row row, int direction) {
    super(DIM_WIDTH, DIM_WIDTH, Color.BLACK); // Initialize the Rectangle

    // initialize variables
    this.row = row;
    this.canvas = canvas;
    this.setX(row.getX());
    this.setY(row.getY());
    this.vX = direction;

    // initialize a random enemy image (there are a total of 10 indexed at 0)
    Random random = new Random();
    int index = random.nextInt(10);
    this.image = "/com/Sprites01/" + index + ".png";
  }

  public void render(Canvas canvas) {
    canvas.getGraphicsContext2D().setFill(this.getFill());
    canvas.getGraphicsContext2D().fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
  }

  // move the enemy in the direction and speed specified by vX
  public void move() {
    this.setX(this.getX() + vX);
  }

  /*
   * * * Getters & Setters * * *
   */

  // Check if the enemy hit edge of screen (to be removed)
  public boolean hitEdge() { return this.getX() > canvas.getWidth() - DIM_WIDTH || this.getX() < 0; }
  public double getVX() { return vX; }
  public Row getRow() { return row; }
  public double getCenterX() { return getX() + DIM_WIDTH / 2; }
  public double getCenterY() { return getY() + DIM_WIDTH / 2; }
  public String getImage(){ return image; }   // used to store filepath for the image that ui will render
}