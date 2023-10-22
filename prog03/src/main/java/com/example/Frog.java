package com.example;

import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Rectangle;

public class Frog extends Rectangle {
	private static final double DIM_WIDTH = 15;

	private Row row;
	// Frog Sprite
	private String image = "/com/Sprites02/crab01.png";

	public Frog(Canvas canvas, Row row) {
		super(DIM_WIDTH, DIM_WIDTH, Color.YELLOW); // Initialize the Rectangle

		// initialize variables
		this.row = row;
		this.setX(row.getX());
		this.setY(row.getY());
	}

	/*
	 * takes in 
	 * - frog x
	 * - frog y
	 * - frog size 
	 * - enemy x 
	 * - enemy y 
	 * - enemy size 
	 * 
	 * and calculates if they are colliding with eachother
	 * Doing this using methods within javafx.scene.shape.Rectangle which is why I 
	 * don't want to change that elsewhere.
	 */
	public boolean didCollideWith(Enemy enemy) {
		double enemyX = enemy.getCenterX();
		double enemyY = enemy.getCenterY();
		double enemyWidth = enemy.getWidth();
		double enemyHeight = enemy.getHeight();

		double x = this.getCenterX();
		double y = this.getCenterY();
		double width = this.getWidth();
		double height = this.getHeight();

		boolean xCond1 = x + width >= enemyX;
		boolean xCond2 = x <= enemyX + enemyWidth;
		boolean collidedX = xCond1 && xCond2;

		boolean yCond1 = y + height >= enemyY;
		boolean yCond2 = y <= enemyY + enemyHeight;
		boolean collidedY = yCond1 && yCond2;

		// Let ya know you suck
		if (collidedX && collidedY) {
			System.out.println("Collision detected:");
			System.out.println("Frog: x=" + x + ", y=" + y + ", width=" + width + ", height=" + height);
			System.out.println("Enemy: x=" + enemyX + ", y=" + enemyY + ", width=" + enemyWidth + ", height=" + enemyHeight);
		}

		return collidedX && collidedY;
	}

	public void render(Canvas canvas) {
		canvas.getGraphicsContext2D().setFill(this.getFill());
		canvas.getGraphicsContext2D().fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}

	// Frog movement 
	public void move(Row row) {
		this.row = row;
		this.setY(row.getY());
	}
	public void setRow(Row row) {
		this.row = row;
		this.setX(row.getX());
	}

	/*
	 * * * Getters & Setters * * *
	 */
	public double getSize() { return (DIM_WIDTH * DIM_WIDTH); }
	public Row getRow() { return row; }
	public double getCenterX() { return this.getX() + DIM_WIDTH / 2; }
	public double getCenterY() { return this.getY() + DIM_WIDTH / 2; }
	public String getImage() { return image; }
}