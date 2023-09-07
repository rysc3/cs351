package prog01;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.util.Random;
import javafx.scene.shape.*;

public class App extends Application {

  private static double STROKE_WIDTH = 1.5;
  private static double CIRCLE_RADIUS = 300;

  // Define the variables the controls will be editing:
  private static int TIMES_TABLE = 2;
  private static int CIRCLE_POINTS = 360;
  private static boolean RUNNING = false;
  private static int STEP = 30;
  private static int FPS = 30;
  private static Color COLOR = Color.RED;
  // todo figure out color somehow

  private Thread animationThread;

  // CSS Styling to make the toggle button change colors
  private static final String RED_BUTTON_STYLE = "-fx-background-color: #FF0000;";
  private static final String GREEN_BUTTON_STYLE = "-fx-background-color: #00FF00;";


  @Override
  public void start(Stage stage) {
    // Create layout
    BorderPane root = new BorderPane();

    // Create the circle outline
    Circle circle = new Circle(CIRCLE_RADIUS);
    circle.setFill(Color.TRANSPARENT);
    circle.setStroke(Color.BLACK);
    circle.setStrokeWidth(STROKE_WIDTH);

    // Controls Vbox
    VBox controlBox = new VBox();
    controlBox.setSpacing(10); // add some padding

    // simple toggle control
    Button RUNNING_tog = new Button("toggle animation");
    RUNNING_tog.setStyle(RED_BUTTON_STYLE); // Set the initial style to red


    // button to reset lines
    Button clearButton = new Button("Clear Lines");
    clearButton.setOnAction(event -> clearLines(circle)); // Call the clearLines method

    HBox start_box = new HBox(RUNNING_tog, clearButton);

    /*
      CIRCLE_POINTS control:
      - Create a text field with buttons on either side to inc. and dec. value of CIRCLE_POINTS
      - text field is non, editable, and only allow you to inc / dec between [30, 360]
      - add event lisetners to update the text box and value when buttons are hit

      - points_hbox:
        ----------------
       |    label      |
       |    buttons    |
       -----------------

       - points_vbox:
       -------------------------------------------------------------------------------------
       |  button dec.                text_field               button inc.                  |
       -------------------------------------------------------------------------------------
    */
    Label CIRCLE_POINTS_label = new Label("Circle points: ");
    TextField CIRCLE_POINTS_text = new TextField();
    CIRCLE_POINTS_text.setEditable(false);  // I want you to only edit with buttons so I can keep increments of 30
    CIRCLE_POINTS_text.setPrefWidth(45);
    CIRCLE_POINTS_text.setText(String.valueOf(CIRCLE_POINTS));

    Button CIRCLE_POINTS_decrease = new Button("-30");
    CIRCLE_POINTS_decrease.setOnAction(event -> {
      if(CIRCLE_POINTS > 30){   // set min value of 30
        CIRCLE_POINTS -= 30;
        CIRCLE_POINTS_text.setText(String.valueOf(CIRCLE_POINTS));
      }else{
        CIRCLE_POINTS = 10;
        CIRCLE_POINTS_text.setText(String.valueOf(CIRCLE_POINTS));
      }
    });

    Button CIRCLE_POINTS_increase = new Button("+30");
    CIRCLE_POINTS_increase.setOnAction(event -> {
      if(CIRCLE_POINTS < 360  && CIRCLE_POINTS != 10){  // set max value of 360
        CIRCLE_POINTS += 30;
        CIRCLE_POINTS_text.setText(String.valueOf(CIRCLE_POINTS));
      }else if(CIRCLE_POINTS == 10){
        CIRCLE_POINTS += 20;
        CIRCLE_POINTS_text.setText(String.valueOf(CIRCLE_POINTS));
      }
    });

    VBox points_vbox = new VBox();
    HBox points_hbox = new HBox();
    points_hbox.setPadding(new Insets(10));

    points_hbox.getChildren().addAll(
      CIRCLE_POINTS_decrease,
      CIRCLE_POINTS_text,
      CIRCLE_POINTS_increase
    );
    points_vbox.getChildren().addAll(
      CIRCLE_POINTS_label,
      points_hbox
    );

    /*
      STEP control:


    */
    Label STEP_label = new Label("STEP: " + STEP);

    Slider STEP_scrollbar = new Slider();
    STEP_scrollbar.setMin(1);
    STEP_scrollbar.setMax(360);
    STEP_scrollbar.setValue(STEP);

    STEP_scrollbar.valueProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue){
        STEP = newValue.intValue();
        STEP_label.setText("Step: " + STEP);
      }
    });

    VBox STEP_vbox = new VBox();
    STEP_vbox.setPadding(new Insets(10));
    STEP_vbox.getChildren().addAll(
      STEP_label,
      STEP_scrollbar
    );

    /*
      FPS Control:

    */
    Label FPS_label = new Label("FPS: " + FPS);

    Slider FPS_scrollbar = new Slider();
    FPS_scrollbar.setMin(1);
    FPS_scrollbar.setMax(400);
    FPS_scrollbar.setValue(FPS);

    FPS_scrollbar.valueProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        FPS = newValue.intValue();
        FPS_label.setText("FPS: " + FPS);
      }
    });

    VBox FPS_vbox = new VBox();
    FPS_vbox.setPadding(new Insets(10));

    FPS_vbox.getChildren().addAll(
      FPS_label,
      FPS_scrollbar
    );

    /*
      Jump feature:

      - step vbox
      -----------------------
      |   STEP:    POINT:   |
      -----------------------

      - step hbox
      -----------------------
      |      step label:    |
      |    step text input  |
      -----------------------

      - point hbox
      -----------------------
      |     point label:    |
      |   point text input  |
      -----------------------

    */
    int jump_step = 0;
    int jump_point = 0;

    Label jump_point_biglabel = new Label("Jump to a point:");
    Button jump_toggle = new Button("Go");
    jump_toggle.setPrefWidth(75);
    jump_toggle.setAlignment(Pos.CENTER);

    Label jump_step_label = new Label("Step: ");
    TextField jump_step_text = new TextField();
    jump_step_text.setPrefWidth(45);
    jump_step_text.setText(String.valueOf(jump_step));

    Label jump_point_label = new Label("Point: ");
    TextField jump_point_text = new TextField();
    jump_point_text.setPrefWidth(45);
    jump_point_text.setText(String.valueOf(jump_point));

    jump_toggle.setOnAction(event -> {
      // get values from text fields
      int point = Integer.parseInt(jump_point_text.getText());
      int step = Integer.parseInt(jump_step_text.getText());

      // Draw the lines
      drawLines(point, step, circle);
    });

    VBox jump_point_bigbox = new VBox();
    HBox jump_hbox = new HBox();
    HBox jump_point_hbox = new HBox();
    jump_point_hbox.setPadding(new Insets(10));
    jump_point_hbox.getChildren().addAll(
      jump_point_label,
      jump_point_text
    );

    HBox jump_step_hbox = new HBox();
    jump_step_hbox.setPadding(new Insets(10));
    jump_step_hbox.getChildren().addAll(
      jump_step_label,
      jump_step_text
    );

    jump_hbox.getChildren().addAll(
      jump_point_hbox,
      jump_step_hbox,
      jump_toggle
    );

    jump_point_bigbox.getChildren().addAll(
      jump_point_biglabel,
      jump_hbox,
      jump_toggle
    );


    /*
      new line color:

    */


    Rectangle colorPreview = new Rectangle(65, 25); // Adjust the size as needed
    Button color_button = new Button("Change Color");
    color_button.setOnAction(event -> {
      COLOR = changeColor();
      // Update the color preview rectangle's fill color
      colorPreview.setFill(COLOR);
    });

    // Create the color preview rectangle
    colorPreview.setStroke(Color.BLACK); // Set the outline color to black
    colorPreview.setFill(COLOR); // Set the initial fill color to the current color

    HBox colorButtonBox = new HBox(10); // Horizontal layout for button and preview
    colorButtonBox.getChildren().addAll(color_button, colorPreview);



    // Adding everything to control box
    controlBox.getChildren().addAll(
      start_box,
      points_vbox,
      STEP_vbox,
      FPS_vbox,
      jump_point_bigbox,
      colorButtonBox
    );


    // Organize items on screen
    root.setLeft(circle);
    root.setRight(controlBox);
    controlBox.setPadding(new Insets(10));

    // Create Scene
    Scene scene = new Scene(root, 800, 650);

    // Defining the action of the toggle button
    RUNNING_tog.setOnAction(event -> {
      if(RUNNING) {
        stopAnimation();
        RUNNING_tog.setStyle(RED_BUTTON_STYLE);
      } else {
        startAnimation(circle);
        RUNNING_tog.setStyle(GREEN_BUTTON_STYLE);
      }
    });


    // Create the scene

    stage.setScene(scene);
    stage.show();
  }

  // Function to create a random color
  public Color changeColor(){

    Random rand = new Random();
    double r = rand.nextDouble();
    double g = rand.nextDouble();
    double b = rand.nextDouble();

    return new Color(r, g, b, 1.0);
  }

  public void startAnimation(Circle circle){
    RUNNING = true;

    animationThread = new Thread(() -> {
      while(RUNNING){
        Platform.runLater(() -> drawLine(circle));
        try {
          Thread.sleep(1000 / FPS);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
    animationThread.setDaemon(true);
    animationThread.start();
  }

  public void stopAnimation() {
    RUNNING = false;
    if (animationThread != null){
      animationThread.interrupt();
    }
  }

  // Function to create a line on a circle from (sX, sY) to (eX, eY).
  public static void drawLine(Circle circle) {
    // Create and start a new thread for each line
    Thread drawLineThread = new Thread(() -> {
      double radius = CIRCLE_RADIUS; // Radius of the circle
      int numPoints = CIRCLE_POINTS; // Number of evenly spaced points
      int step = STEP; // Step value

      // Calculate the angle increment between each point
      double angleIncrement = 360.0 / numPoints;

      // Choose a random starting point
      double angleStart = Math.random() * 360;

      // Calculate the current point
      int currentPoint = (int) Math.round(angleStart / angleIncrement);

      // Calculate the next point based on the current point and step
      int nextPoint = (currentPoint * step) % numPoints;

      // Calculate the coordinates of the starting and ending points
      double startX = radius * Math.cos(Math.toRadians(currentPoint * angleIncrement)) + radius;
      double startY = radius * Math.sin(Math.toRadians(currentPoint * angleIncrement)) + radius;

      double endX = radius * Math.cos(Math.toRadians(nextPoint * angleIncrement)) + radius;
      double endY = radius * Math.sin(Math.toRadians(nextPoint * angleIncrement)) + radius;

      Line line = new Line(startX, startY, endX, endY);
      line.setStroke(COLOR);
      line.setStrokeWidth(STROKE_WIDTH);

      // Update the UI on the JavaFX Application Thread
      Platform.runLater(() -> {
        ((Pane) circle.getParent()).getChildren().add(line);
      });
    });

    // Start the thread to draw the line
    drawLineThread.start();
  }

  // Method used to auto draw the lines for the jump
  public static void drawLines(int point, int step, Circle circle) {
    STEP = step;
    for(int i = 0; i < point; i++) {
      Thread drawLineThread = new Thread(() -> drawLine(circle));
      drawLineThread.start();
    }
  }

  // Function to clear all lines
  public static void clearLines(Circle circle) {
    if (circle.getParent() instanceof Pane) {
      Pane parentPane = (Pane) circle.getParent();
      parentPane.getChildren().removeIf(node -> node instanceof Line);
    }
  }


  public static void main(String[] args) { launch(); }
}
