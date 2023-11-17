/*
 * @author Ryan Scherbarth
 * @author Manny Metcalfe
 * @author Ambrose Hwang
 * 
 * Last Edited: 11/16/2023
 */
package com.prog04;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;

/**
 * The UI class represents the user interface for a JavaFX application.
 * It primarily deals with graphical elements, rendered on a canvas. This class is responsible
 * for initializing the canvas with specified dimensions and updating the UI elements as needed.
 */
public class UI {

  private Canvas canvas;

    /**
     * Constructs a UI instance with a canvas of specified dimensions.
     *
     * @param size An array where size[2] is the width and size[3] is the height of the canvas.
     */
  public UI(int[] size){
    this.canvas = new Canvas(size[2], size[3]);
  }

  // Loop through each item on the canvas and update it
  public void update(){

  }

  /*
   * * Getters & Setters
   */

   public Scene getScene(){
      // return new Scene(canvas, canvas.getWidth(), canvas.getHeight());
      return null;
   }
  //Getters for canvas
   public Canvas getCanvas(){ return canvas; }
}
