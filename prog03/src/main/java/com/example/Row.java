package com.example;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

// Horizontal rows on which enemies will be moving
public class Row {
  public static final double WIDTH = 750;
  public static final double HEIGHT = 40; // Set ROW_SIZE to 40px for now
  private int index;
  private Color color;
  private double centerY; // absolute center on the screen
  private ArrayList<Enemy> enemies;

  public Row(int index, double centerY, Color color) {
    this.index = index;
    this.centerY = centerY;
    this.color = color;
    enemies = new ArrayList<>();
  }

  public void render(GraphicsContext graphicsContext) {
    graphicsContext.setFill(color);
    graphicsContext.fillRect(0, 0, WIDTH, HEIGHT);
  }

  public void addEnemy(Enemy enemy) { enemies.add(enemy); }

  public ArrayList<Enemy> getEnemies() { return enemies; }

  public double getX(){ return WIDTH / 2; }

  public double getY(){ return centerY; }

  public Color getColor() { return color; }

  public int getIndex() { return index; }
}