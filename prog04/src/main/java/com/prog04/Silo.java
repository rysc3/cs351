/*
 * @author Ryan Scherbarth
 * @author Manny Metcalfe
 * @author Ambrose Hwang
 * 
 * Last Edited: 11/16/2023
 */
package com.prog04;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import java.time.Duration;
import java.util.ArrayList;
import java.lang.Math;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The Silo class represents an individual processing unit in a simulation,
 * capable of executing a set of instructions. It communicates with a central
 * controller, manages internal state (like ACC and BAK), and executes commands
 * based on its instruction set. It also handles label-based jumps and interactions
 * with neighboring silos in different directions.
 */
public class Silo implements Runnable {
    // Save the instruction set in the body
    private ArrayList<String> body;
    private int current; // Current instruction
    // Value that can be written to directly.
    private int ACC;
    // Value that can only be used by the save() method.
    private int BAK;
    // Labels
    private ArrayList<Label> labels;

    private final Controller control;

    private boolean stayWait = false;


    /**
     * Signals the silo to end its current operation.
     */
    public void end(){
        stayWait = true;
    }



    /**
     * Constructs a Silo with a given set of instructions and associates it with a controller.
     *
     * @param body The list of instructions this silo will execute.
     * @param controller The controller managing this silo.
     */
    public Silo(ArrayList<String> body, Controller controller) {
        this.body = body;
        this.control = controller;
        // initialize things
        this.current = 0;
        createLabels();
    }

    private void createLabels() {
        labels = new ArrayList<>();
        // loop through input and create labels
        for (int i = 0; i < body.size(); i++) {
            // format: : <LABEL> :
            if (body.get(i).charAt(0) == ':' && body.get(i).charAt(body.get(i).length()-1) == ':') {
                labels.add(new Label(i, body.get(i).substring(1, body.get(i).length()-1))); // If we see the label regex, create label
            }
        }
    }

    /**
     * Starts the execution of the silo's instructions. This method manages the flow of
     * instruction processing and interacts with the controller for synchronization and communication.
     */
    public void play() {
        synchronized (control.getWaiting()){
            control.addWait(this);
        }
        if (current >= body.size()) {
            updateIndex();
            synchronized (control.getWaiting()){
                control.removeWait(this);
            }
            return;
        }
        String cur = body.get(current);

        // Check if cur word count
        String[] command = cur.split(" ");

        if (command.length == 1) { // One word commands
            switch (command[0]) {
                case "NOOP" -> noop();
                case "SWAP" -> swap();
                case "SAVE" -> save();
                case "NEGATE" -> negate();
                default -> {
                }
            }
        } else {
            switch (command[0]) { // Multi-word commands
                case "MOVE" -> move(command[1], command[2]);
                case "ADD" -> add(command[1]);
                case "SUB" -> sub(command[1]);
                case "JUMP" -> Jump(getLabel(command[1]));
                case "JEZ" -> JEZ(getLabel(command[1]));
                case "JNZ" -> JNZ(getLabel(command[1]));
                case "JGZ" -> JGZ(getLabel(command[1]));
                case "JLZ" -> JLZ(getLabel(command[1]));
                case "JRO" -> JRO((command[1]));
                case "SLEEP" -> sleep(command[1]);
            }
        }
        updateIndex();
        synchronized (control.getWaiting()){
            if(!stayWait) {
                control.removeWait(this);
            }
        }
    }

    // Loops current back to begging, each silo should loop infinitely
    private void updateIndex() {
        if (this.current > body.size() - 1) {
            this.current = 0;
        } else {
            current++;
        }
    }

    private void sleep(String sleeping){
        int sleep = 0;
        try{
            sleep = Integer.parseInt(sleeping);
        }catch(Exception ignored){

        }
        try {
            control.pauseGame();
            Thread.sleep(TimeUnit.SECONDS.toMillis(sleep));
            control.resumeGame();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * * * Required Methods * * *
     */

    // Noop does nothing.
    private void noop() {
    }


    /*
     * Accepts int, silo, ACC
     *
     * @param src can be any of the above.
     * @param dst
     */
    private void move(String src, String dst) {
        //Determine source if nil source is already 0;
        int sourceVal = switch (src) {
            case "UP" -> control.pullValue(this, Controller.Direction.UP);
            case "DOWN" -> control.pullValue(this, Controller.Direction.DOWN);
            case "LEFT" -> control.pullValue(this, Controller.Direction.LEFT);
            case "RIGHT" -> control.pullValue(this, Controller.Direction.RIGHT);
            case "ACC" -> ACC;
            default -> 0;
        };
        try {
            sourceVal = Integer.parseInt(src);
        } catch (Exception ignored) {

        }

        switch (dst) {
            case "UP" -> control.pushValue(sourceVal, this, Controller.Direction.UP);
            case "DOWN" -> control.pushValue(sourceVal, this, Controller.Direction.DOWN);
            case "LEFT" -> control.pushValue(sourceVal, this, Controller.Direction.LEFT);
            case "RIGHT" -> control.pushValue(sourceVal, this, Controller.Direction.RIGHT);
            case "ACC" -> ACC = sourceVal;
        }


    }

    // checks type of input, returns 1 for silo, 2 for int, -999999 for invalid
    private <T> int getType(T in) {
        if (in instanceof Silo) {
            return 1;
        } else if (in instanceof Integer) {
            return 2;
        } else {
            return -999999;
        }
    }

    // Swaps the values of the ACC and BAK.
    private void swap() {
        int temp = ACC;
        ACC = BAK;
        BAK = temp;
    }

    // Saves from ACC to BAK
    private void save() {
        BAK = ACC;
    }

    /*
     * Generic functions that allow for either ints or Silos to be passed through
     * and handle the addition accordingly.
     */
    private void add(String input) {

        try {
            int add = Integer.parseInt(input);
            ACC += add;
            return;
        } catch (Exception ignored) {

        }


        switch (input) {
            case "UP" -> ACC += control.pullValue(this, Controller.Direction.UP);
            case "DOWN" -> ACC += control.pullValue(this, Controller.Direction.DOWN);
            case "LEFT" -> ACC += control.pullValue(this, Controller.Direction.LEFT);
            case "RIGHT" -> ACC += control.pullValue(this, Controller.Direction.RIGHT);
            case "ACC" -> ACC += ACC;
        }


    }


    private void sub(String input) {
        try {
            int add = Integer.parseInt(input);
            ACC -= add;
            return;
        } catch (Exception ignored) {

        }
        switch (input) {
            case "UP" -> ACC -= control.pullValue(this, Controller.Direction.UP);
            case "DOWN" -> ACC -= control.pullValue(this, Controller.Direction.DOWN);
            case "LEFT" -> ACC -= control.pullValue(this, Controller.Direction.LEFT);
            case "RIGHT" -> ACC -= control.pullValue(this, Controller.Direction.RIGHT);
            case "ACC" -> ACC -= ACC;
        }
    }

    // Negate ACC, do nothing if it is currently 0.
    private void negate() {
        if (ACC != 0) {
            ACC = -ACC;
        }
    }

    // TODO @ryan
    // THis is wrong, figure out how to get the correct label as a parameter

    // Jumps to given label
    private void Jump(Label label) {
        this.current = getIndex(label);
    }

    // Jumps to next instruction if ACC == 0
    private void JEZ(Label label) {
        if (this.ACC == 0) {
            this.current = getIndex(label);
        }
    }

    // Jump to label if ACC != 0
    private void JNZ(Label label) {
        if (this.ACC != 0) {
            this.current = getIndex(label);
        }
    }

    // Jump to label if ACC > 0
    private void JGZ(Label label) {
        if (this.ACC > 0) {
            this.current = getIndex(label);
        }
    }

    // Jump to label if ACC < 0
    private void JLZ(Label label) {
        if (this.ACC < 0) {
            this.current = getIndex(label);
        }
    }

    // Jumps whatever index is placed as a param. Can wrap around begin and end
    // Could probably change this to just call update() as many times as needed. Only
    // Issue is handling negative values.
    private void JRO(String index) {
        int jumpVal = 0;
        switch (index) {
            case "UP" -> jumpVal = control.pullValue(this, Controller.Direction.UP);
            case "DOWN" -> jumpVal = control.pullValue(this, Controller.Direction.DOWN);
            case "LEFT" -> jumpVal = control.pullValue(this, Controller.Direction.LEFT);
            case "RIGHT" -> jumpVal = control.pullValue(this, Controller.Direction.RIGHT);
            case "ACC" -> jumpVal = ACC;
        }
        try {
            jumpVal = Integer.parseInt(index);
        } catch (Exception ignored) {

        }

        int jumpActual = current;

        int increment = 0;
        if (jumpVal < 0) {
            increment = 1;
        } else if (jumpVal > 0) {
            increment = -1;
        } else {
            current = -1;
            return;
        }
        for (int i = jumpVal; i != 0; i = i + increment) {
            jumpActual -= increment;
            if (jumpActual > body.size() - 1) jumpActual = 0;
            else if (jumpActual < 0) {
                jumpActual = body.size() - 1;
            }

        }

        current = jumpActual - 1;


    }

    /*
     * * * Helper Methods * * *
     */

    private void setACC(int ACC) {
        this.ACC = ACC;
    }

    //Gets ACC value
    public int getACC() {
        return ACC;
    }
    //Gets BAK value
    public int getBAK() {
        return BAK;
    }

    /*
     * Takes a label as a param, and returns the index of that label in the body.
     * the index corresponds to which line the label is on.
     */
    private int getIndex(Label label) {
        for (Label lab : this.labels) {
            if (lab.getString().equals(label.getString())) {
                return lab.getIndex();
            }
        }
        return 0;
    }

    // Loops through all of the labels to find the correct one. not efficient but whateva
    private Label getLabel(String string) {
        // We assume there will be no two identitical labels. If there is this will always just go to the first one.
        for (int i = 0; i < labels.size(); i++) {
            if (labels.get(i).getString().equals(string)) {
                return labels.get(i);
            }
        }
        return null;
    }
    //Gets current instructin index
    public int getCurrent() {
        return this.current;
    }


    //RUns silos
    @Override
    public void run() {
        play();
        control.countDown();

    }

    // Class to handle lables
    private static class Label {

        private int index;
        private String string;

        public Label(int index, String string) {
            this.index = index; // this index corresponds to the index of instruction in the body
            this.string = string;
        }

        public int getIndex() {
            return index;
        }

        public String getString() {
            return string;
        }
    }
}
