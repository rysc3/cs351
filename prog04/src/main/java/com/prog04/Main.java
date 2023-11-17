/*
 * @author Ryan Scherbarth
 * @author Manny Metcalfe
 * @author Ambrose Hwang
 * 
 * Last Edited: 11/16/2023
 */
package com.prog04;

import javafx.animation.Interpolator;
import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.shape.Line;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.prog04.Controller;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.event.EventHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** Ambrose Hwang Danny Metcalfe Ryan Scherbarth
 * The Main class extends the JavaFX Application and is responsible for creating
 * and managing the user interface of the game. It sets up the primary stage and scenes,
 * initializes UI components, and handles interactions and updates between the game's
 * controller and the UI.
 */
public class Main extends Application {
    /*
     * - Start Height
     * - Start Width
     * - Game Height
     * - Game Width
     */

    private List<Label> columnSpacerValueLabels = new ArrayList<>();
    private List<Label> smallTextAreaValueLabels = new ArrayList<>();

    private List<Controller.SiloStream> streamvalues = new ArrayList<>();
    private List<Label> accValueLabels = new ArrayList<>();
    private List<Label> bakValueLabels = new ArrayList<>();
    private List<Label> currValueLabels = new ArrayList<>();
    private static final int[] size = {400, 400, 600, 600};

    private int[] accValues = new int[12];
    private int[] bakValues = new int[12];

    private Slider artificialDelaySlider;
    private Button stepButton;

    private boolean isGamePaused = false;

    private Label titleLabel;

    private TextArea textArea1;
    private TextArea textArea2;
    private TextArea textArea3;
    private TextArea textArea4;
    private TextArea textArea5;
    private TextArea textArea6;
    private TextArea textArea7;
    private TextArea textArea8;
    private TextArea textArea9;
    private TextArea textArea10;
    private TextArea textArea11;
    private TextArea textArea12;

    private TextArea inTextArea1;
    private TextArea inTextArea2;

    private TextArea outELeftTextArea;
    private TextArea outERightTextArea;

    private TextArea outLLeftTextArea;
    private TextArea outLRightTextArea;
    TextArea humanTextArea;

    private Stage primaryStage;

    private Controller TIS_Controller;

    /**
     * Starts the game
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {

        TIS_Controller = new Controller(size, this);
        streamvalues = TIS_Controller.getStreams();

        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        root.setPadding(new Insets(20));

        titleLabel = new Label(TIS_Controller.getTitle());
        titleLabel.setFont(new Font("Arial", 16));
        titleLabel.setAlignment(Pos.TOP_LEFT);
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        VBox topVBox = new VBox(10);
        topVBox.setAlignment(Pos.TOP_LEFT);


        humanTextArea = new TextArea();
        humanTextArea.setPrefHeight(80);
        humanTextArea.setMaxWidth(200);
        humanTextArea.setEditable(false);

        topVBox.getChildren().addAll(titleLabel, humanTextArea);
        root.setTop(topVBox);

        GridPane centerGrid = new GridPane();
        centerGrid.setHgap(5);
        centerGrid.setVgap(5);


        centerGrid.add(createColumnSpacer(), 3, 1);
        centerGrid.add(createColumnSpacer(), 5, 1);
        centerGrid.add(createColumnSpacer(), 7, 1);
        centerGrid.add(createColumnSpacer(), 3, 3);
        centerGrid.add(createColumnSpacer(), 5, 3);
        centerGrid.add(createColumnSpacer(), 7, 3);
        centerGrid.add(createColumnSpacer(), 3, 5);
        centerGrid.add(createColumnSpacer(), 5, 5);
        centerGrid.add(createColumnSpacer(), 7, 5);

        centerGrid.add(createSmallTextArea(), 2, 2);
        centerGrid.add(createSmallTextArea(), 4, 2);
        centerGrid.add(createSmallTextArea(), 6, 2);
        centerGrid.add(createSmallTextArea(), 8, 2);
        centerGrid.add(createSmallTextArea(), 2, 4);
        centerGrid.add(createSmallTextArea(), 4, 4);
        centerGrid.add(createSmallTextArea(), 6, 4);
        centerGrid.add(createSmallTextArea(), 8, 4);

        centerGrid.add(createBoxWithTextField(1), 2, 1);
        centerGrid.add(createBoxWithTextField(2), 4, 1);
        centerGrid.add(createBoxWithTextField(3), 6, 1);
        centerGrid.add(createBoxWithTextField(4), 8, 1);
        centerGrid.add(createBoxWithTextField(5), 2,  3);
        centerGrid.add(createBoxWithTextField(6), 4, 3);
        centerGrid.add(createBoxWithTextField(7), 6, 3);
        centerGrid.add(createBoxWithTextField(8), 8, 3);
        centerGrid.add(createBoxWithTextField(9), 2, 5);
        centerGrid.add(createBoxWithTextField(10), 4, 5);
        centerGrid.add(createBoxWithTextField(11), 6, 5);
        centerGrid.add(createBoxWithTextField(12), 8, 5);

        root.setCenter(centerGrid);

        addTitlesToSilosWithTextArea(centerGrid, streamvalues);

        VBox leftVBox = new VBox(20);
        TIS_Controller.fillText();

        HBox horizontalBox = new HBox(10);

        int inCount = 0;
        String[] labels = {"", "", "", ""};

        for ( int i = 0; i < streamvalues.size(); i ++) {
            Controller.SiloStream siloValue = streamvalues.get(i);
            labels[i] = siloValue.getTitle();

        }
        for (String label : labels) {
            VBox miniVBox = new VBox();
            Label lbl = new Label(label);
            lbl.setTextFill(Color.WHITE);

            StackPane stackPane = new StackPane();
            Rectangle rect = new Rectangle(50, 390);
            rect.setFill(Color.GREY);
            stackPane.getChildren().add(rect);

            if (label.contains("INPUT")) {
                TextArea inTextArea = createTextArea();
                stackPane.getChildren().add(inTextArea);

                if (inCount == 0) {
                    inTextArea1 = inTextArea;
                    inCount++;
                } else if (inCount == 1) {
                    inTextArea2 = inTextArea;
                }
            } else{
                VBox outBox = createOutBox(label);
                horizontalBox.getChildren().add(outBox);
                continue;
            }

            miniVBox.getChildren().addAll(lbl, stackPane);
            horizontalBox.getChildren().add(miniVBox);
        }


        leftVBox.getChildren().add(horizontalBox);
        root.setLeft(leftVBox);
        HBox bottomHBox = new HBox(10);

        Button stopButton = new Button("Stop");
        stopButton.setPrefWidth(50);
        stopButton.setPrefHeight(50);
        TextField stopTextField = new TextField();
        stopTextField.setPrefWidth(110);
        bottomHBox.getChildren().addAll(stopButton);

        stepButton = new Button("Step");
        stepButton.setPrefWidth(50);
        stepButton.setPrefHeight(50);
        TextField stepTextField = new TextField();
        stepTextField.setPrefWidth(110);
        bottomHBox.getChildren().addAll(stepButton);

        Button runButton = new Button("Run");
        runButton.setPrefWidth(50);
        runButton.setPrefHeight(50);
        TextField runTextField = new TextField();
        runTextField.setPrefWidth(110);
        bottomHBox.getChildren().addAll(runButton);

        Button startButton = new Button("Play");
        startButton.setPrefWidth(50);
        startButton.setPrefHeight(50);

        Button resumeButton = new Button("Resume");
        resumeButton.setPrefWidth(50);
        resumeButton.setPrefHeight(50);

        bottomHBox.getChildren().add(resumeButton);
        bottomHBox.getChildren().add(startButton);

        artificialDelaySlider = new Slider();
        artificialDelaySlider.setMin(50); // Minimum value
        artificialDelaySlider.setMax(6050); // Maximum value
        artificialDelaySlider.setValue(1000); // Initial value
        artificialDelaySlider.setShowTickLabels(true);
        artificialDelaySlider.setShowTickMarks(true);
        artificialDelaySlider.setMajorTickUnit(1000);
        artificialDelaySlider.setSnapToTicks(true);

        Label sliderLabel = new Label("Artificial Delay (MS)");
        sliderLabel.setTextFill(Color.WHITE);

        bottomHBox.getChildren().addAll(sliderLabel, artificialDelaySlider);

        startButton.setOnAction(event -> {
            System.out.println("Game Starting...");
            TIS_Controller.start();
            isGamePaused = false;
            stepButton.setText("Pause");
            stepButton.setOnAction(e -> handlePauseGame());
        });


        resumeButton.setOnAction(event -> handleResumeGame());
        stopButton.setOnAction(event -> {
            TIS_Controller.stopAndResetGame();
        });

        // Example of button event listener in your UI class
//        stepButton.setOnAction(event -> {
//            TIS_Controller.executeOneStep();
//        });



        root.setBottom(bottomHBox);

        leftVBox.getChildren().add(bottomHBox);

        BorderPane.setAlignment(bottomHBox, Pos.CENTER_LEFT);
        bottomHBox.setSpacing(5);
        BorderPane.setMargin(bottomHBox, new Insets(10, 0, 0, 0));

        this.primaryStage = primaryStage;
        Scene startScene = createStartScene();
        primaryStage.setScene(startScene);
        primaryStage.setTitle("TIS");
        primaryStage.setScene(new Scene(root, 1500, 800));
        primaryStage.show();


        updateInputBoxesFromStreams();
        updateOutputBoxesFromStreams();
        displayHumanInstructions();
//        fillEmptyTextAreas();
        fillTextIfNotSet("COMMS FAILURE");


    }
    /**
     * Resets the UI elements to their initial state. This includes setting
     * accumulator, backup, and current value labels to zero and clearing all text areas.
     */

    public void resetUI() {

        for (Label accLabel : accValueLabels) {
            accLabel.setText("0");
        }
        for (Label bakLabel : bakValueLabels) {
            bakLabel.setText("0");
        }
        for (Label currLabel : currValueLabels) {
            currLabel.setText("0");
        }

        textArea1.clear();
        textArea2.clear();
        textArea3.clear();
        textArea4.clear();
        textArea5.clear();
        textArea6.clear();
        textArea7.clear();
        textArea8.clear();
        textArea9.clear();
        textArea10.clear();
        textArea11.clear();
        textArea12.clear();

        artificialDelaySlider.setValue(1000);
        stepButton.setText("Step");

    }

    /**
     * Handles the pause game functionality by stepping the game and resuming it.
     * It sets the game state to paused and changes the step button text appropriately.
     */
    private void handlePauseGame() {
        TIS_Controller.step();
        TIS_Controller.resumeGame();
        isGamePaused = true;
        stepButton.setText("Step");
        if (isGamePaused) {
//            put ur method here
            System.out.println("handle step");
        }
    }

    /**
     * Resumes the game from a paused state. It calls the resumeGame method in the controller
     * and updates the game state and step button text.
     */
    private void handleResumeGame() {
        TIS_Controller.resumeGame();
        isGamePaused = false;
        stepButton.setText("Pause");

    }

    /**
     * Fills text areas with a default text if they are not already set. This method
     * iterates through each text area and sets the specified default text if the text area
     * is empty.
     *
     * @param defaultText The default text to set in each empty text area.
     */
    public void fillTextIfNotSet(String defaultText) {
        for (int i = 0; i < 12; i++) { // Assuming you have 12 text areas
            if (!isTextAreaSet(i)) {
                setTextToArea(i, defaultText);
            }
        }
    }

    /**
     * Checks if a specific text area is set with some text.
     *
     * @param areaIndex The index of the text area to check.
     * @return true if the text area at the specified index has text, false otherwise.
     */
    public boolean isTextAreaSet(int areaIndex) {
        String text = "";
        switch (areaIndex) {
            case 0:
                text = getTextFromArea1();
                break;
            case 1:
                text = getTextFromArea2();
                break;
            case 2:
                text = getTextFromArea3();
                break;
            case 3:
                text = getTextFromArea4();
                break;
            case 4:
                text = getTextFromArea5();
                break;
            case 5:
                text = getTextFromArea6();
                break;
            case 6:
                text = getTextFromArea7();
                break;
            case 7:
                text = getTextFromArea8();
                break;
            case 8:
                text = getTextFromArea9();
                break;
            case 9:
                text = getTextFromArea10();
                break;
            case 10:
                text = getTextFromArea11();
                break;
            case 11:
                text = getTextFromArea12();
                break;
            default:
                // Optionally handle an invalid index
                System.out.println("Invalid area index: " + areaIndex);
                return false; // or handle it differently based on your requirements
        }
        return text != null && !text.isEmpty();
    }

    /**
     * Sets text to a specific text area based on the area index.
     * This method also applies a specific style to the text area when text is set.
     *
     * @param areaIndex The index of the text area where the text is to be set.
     * @param text The text to be set in the specified text area.
     */
    private void setTextToArea(int areaIndex, String text) {
        String backgroundColorStyle = "-fx-control-inner-background: red;";
        switch (areaIndex) {
            case 0:
                setTextToArea1(text);
                textArea1.setStyle(backgroundColorStyle);
                break;
            case 1:
                setTextToArea2(text);
                textArea2.setStyle(backgroundColorStyle);
                break;
            case 2:
                setTextToArea3(text);
                textArea3.setStyle(backgroundColorStyle);
                break;
            case 3:
                setTextToArea4(text);
                textArea4.setStyle(backgroundColorStyle);
                break;
            case 4:
                setTextToArea5(text);
                textArea5.setStyle(backgroundColorStyle);
                break;
            case 5:
                setTextToArea6(text);
                textArea6.setStyle(backgroundColorStyle);
                break;
            case 6:
                setTextToArea7(text);
                textArea7.setStyle(backgroundColorStyle);
                break;
            case 7:
                setTextToArea8(text);
                textArea8.setStyle(backgroundColorStyle);
                break;
            case 8:
                setTextToArea9(text);
                textArea9.setStyle(backgroundColorStyle);
                break;
            case 9:
                setTextToArea10(text);
                textArea10.setStyle(backgroundColorStyle);
                break;
            case 10:
                setTextToArea11(text);
                textArea11.setStyle(backgroundColorStyle);
                break;
            case 11:
                setTextToArea12(text);
                textArea12.setStyle(backgroundColorStyle);
                break;
            default:
                break;
        }
    }

    /**
     * Creates and returns a StackPane containing a small TextArea with specific styling
     * and additional UI elements like labels for up and down arrows, and a value label.
     *
     * @return A StackPane containing a styled TextArea and other UI components.
     */
    private StackPane createSmallTextArea() {
        TextArea smallTextArea = new TextArea();
        smallTextArea.setPrefSize(100, 50);
        smallTextArea.setEditable(false);
        smallTextArea.setStyle("-fx-control-inner-background: black; -fx-text-fill: white; " +
                "-fx-text-box-border: black; " +
                "-fx-background-radius:0; -fx-border-width: 0; -fx-padding: 0;");

        Label upArrow = new Label("↑");
        Label downArrow = new Label("↓");
        upArrow.setFont(new Font(20));
        downArrow.setFont(new Font(20));
        upArrow.setTextFill(Color.WHITE);
        downArrow.setTextFill(Color.WHITE);

        Label valueLabel = new Label("");
        valueLabel.setFont(new Font(20));
        valueLabel.setTextFill(Color.WHITE);
        smallTextAreaValueLabels.add(valueLabel);

        HBox contentBox = new HBox(upArrow, downArrow, valueLabel);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setSpacing(10);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(smallTextArea, contentBox);

        return stackPane;
    }

    /**
     * Creates and returns a VBox that acts as a spacer in the UI, containing a styled TextArea
     * with arrow symbols and a label for displaying values.
     *
     * @return A VBox acting as a spacer with specific UI components.
     */
    private VBox createColumnSpacer() {

        TextArea arrowsTextArea = new TextArea("\n" + "←   →");
        arrowsTextArea.setPrefSize(90, 110);
        arrowsTextArea.setEditable(false);
        arrowsTextArea.setStyle("-fx-control-inner-background: black; -fx-text-fill: white; -fx-font-size: 20; " +
                "-fx-text-alignment: center; -fx-text-box-border: transparent;");
        Label valueLabel = new Label("");
        valueLabel.setPrefSize(90, 30);
        valueLabel.setAlignment(Pos.CENTER);
        valueLabel.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 20; " +
                "-fx-text-box-border: transparent;");

        columnSpacerValueLabels.add(valueLabel);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(arrowsTextArea, valueLabel);

        return vBox;
    }

    /**
     * Adds titles to silos represented by TextArea elements in the GridPane.
     * This method iterates through a list of streams, creating a TextArea for each stream's title
     * and placing it in the grid at specific positions.
     *
     * @param gridPane The GridPane in which the title areas are to be added.
     * @param streams The list of SiloStream objects, each representing a stream in the UI.
     */
    private void addTitlesToSilosWithTextArea(GridPane gridPane, List<Controller.SiloStream> streams) {
        for (Controller.SiloStream stream : streams) {
            String title = stream.getTitle();
            int row = stream.getRow();
            int col = stream.getColumn();

            TextArea titleArea = new TextArea(title);
            titleArea.setEditable(false);
            titleArea.setWrapText(true);
            titleArea.setMaxHeight(30);
            titleArea.setPrefWidth(100);
            titleArea.setStyle("-fx-control-inner-background: black; -fx-text-fill: white;");
            GridPane.setValignment(titleArea, VPos.BOTTOM);

            if (row == -1 && col == 0) {
                gridPane.add(titleArea, col + 2, row + 1);
            }
            else if(row == -1 && col > 2) {
                gridPane.add(titleArea, col + 5, row + 1);
            }
            else if(row >= 3 && col > 2) {
                gridPane.add(titleArea, col + 5, row + 3);
            }
            else if (col == 1) {
                gridPane.add(titleArea, col + 3, row + 3);
            }
            else if (row == 1) {
                gridPane.add(titleArea, col + 2, row - 1);
            }
            else {
                gridPane.add(titleArea, col + 2, row + 3);
            }
        }
    }

    /**
     * Displays the human-readable instructions in the human text area. It gathers
     * instructions from the controller and updates the UI component.
     */
    public void displayHumanInstructions() {
        ArrayList<String> instructionList = TIS_Controller.getHumanInstructions();
        StringBuilder instructions = new StringBuilder();

        for (String instruction : instructionList) {
            instructions.append(instruction).append("\n");
        }

        humanTextArea.setText(instructions.toString());
    }

    /**
     * Updates the input boxes with values from the streams. This method reads
     * input stream values from the controller and updates the corresponding text areas in the UI.
     */
    public void updateInputBoxesFromStreams() {
        List<Controller.SiloStream> streams = TIS_Controller.getStreams();
        for (int i = 0; i < streams.size(); i++) {
            Controller.SiloStream stream = streams.get(i);
            if (stream.getType() == Controller.StreamType.IN) {
                String values = stream.getCurrentValues().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n"));
                switch (i) {
                    case 0: setTextToInTextArea1(values); break;
                    case 1: setTextToInTextArea2(values); break;
                }
            }
        }
    }

    /**
     * Updates the output boxes with values from the streams. This method reads
     * output stream values from the controller and updates the corresponding text areas in the UI.
     */
    public void updateOutputBoxesFromStreams() {
        List<Controller.SiloStream> streams = TIS_Controller.getStreams();
        int outputIndex = 0;
        for (Controller.SiloStream stream : streams) {
            if (stream.getType() == Controller.StreamType.OUT) {
                String values = stream.getValues().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining("\n"));

                if (outputIndex == 0) {
                    setTextToOutELeft(values);
                } else if (outputIndex == 1) {
                    setTextToOutLLeft(values);
                }
                outputIndex++;
            }
        }
    }

    private TextArea createTextArea() {
        TextArea textArea = new TextArea();
        textArea.setPrefSize(50, 390);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        return textArea;
    }

    int outputBoxCount = 0;
    private VBox createOutBox(String label) {
        VBox outVBox = new VBox();
        Label outLabel = new Label(label);
        outLabel.setTextFill(Color.WHITE);

        TextArea leftTextArea = new TextArea();
        leftTextArea.setPrefSize(50, 390);
        leftTextArea.setWrapText(true);
        leftTextArea.setEditable(false);

        TextArea rightTextArea = new TextArea();
        rightTextArea.setPrefSize(50, 390);
        rightTextArea.setWrapText(true);
        rightTextArea.setEditable(false);

        if (label.contains("OUTPUT")) {
            if (outputBoxCount == 0) {
                outELeftTextArea = leftTextArea;
                outERightTextArea = rightTextArea;
            } else if (outputBoxCount == 1) {
                outLLeftTextArea = leftTextArea;
                outLRightTextArea = rightTextArea;
            }
            outputBoxCount++;
        }

        Line verticalLine = new Line();
        verticalLine.setStartX(0);
        verticalLine.setEndX(0);
        verticalLine.setStartY(0);
        verticalLine.setEndY(390);
        verticalLine.setStroke(Color.BLACK);

        HBox hbox = new HBox();
        hbox.getChildren().addAll(leftTextArea, verticalLine, rightTextArea);
        hbox.setAlignment(Pos.CENTER);

        outVBox.getChildren().addAll(outLabel, hbox);
        return outVBox;
    }

    /**
     * Sets the text color of the first output box's right text area to red.
     * This method is typically used to indicate a specific state or condition, such as an error or warning.
     */
    public void setFirstOutputBoxRight() {
        String redTextColorStyle = "-fx-text-fill: red;";

        outERightTextArea.setStyle(redTextColorStyle);
    }
    /**
     * Sets the text color of the second output box's right text area to red.
     * Similar to setFirstOutputBoxRight, this method signifies a specific state (like an error) in the second output box.
     */
    public void setSecondOutputBoxRight() {
        String redTextColorStyle = "-fx-text-fill: red;";

        outLRightTextArea.setStyle(redTextColorStyle);
    }
    /**
     * Resets the text color of the first output box's right text area to green.
     * This method is typically used to indicate a return to normal or successful state after a previous warning or error.
     */
    public void resetFirstOutputBoxRight() {
        String redTextColorStyle = "-fx-text-fill: green;";

        outERightTextArea.setStyle(redTextColorStyle);
    }

    /**
     * Resets the text color of the second output box's right text area to green.
     * Similar to resetFirstOutputBoxRight, this method is used to indicate that the second output box is in a normal or successful state.
     */
    public void resetSecondOutputBoxRight() {
        String redTextColorStyle = "-fx-text-fill: green;";

        outLRightTextArea.setStyle(redTextColorStyle);
    }

    private StackPane createBoxWithTextField(int id) {
        StackPane stack = new StackPane();
        Rectangle rect = new Rectangle(150, 150);
        rect.setFill(Color.GREY);

        TextArea textArea = new TextArea();
        textArea.setPrefWidth(100);
        textArea.setPrefHeight(140);
        textArea.setWrapText(true);
        textArea.setPadding(new Insets(5, 5, 5, 5));
        textArea.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));

        switch (id) {
            case 1:
                textArea1 = textArea;
                break;
            case 2:
                textArea2 = textArea;
                break;
            case 3:
                textArea3 = textArea;
                break;
            case 4:
                textArea4 = textArea;
                break;
            case 5:
                textArea5 = textArea;
                break;
            case 6:
                textArea6 = textArea;
                break;
            case 7:
                textArea7 = textArea;
                break;
            case 8:
                textArea8 = textArea;
                break;
            case 9:
                textArea9 = textArea;
                break;
            case 10:
                textArea10 = textArea;
                break;
            case 11:
                textArea11 = textArea;
                break;
            case 12:
                textArea12 = textArea;
                break;
        }

        VBox smallRectsVBox = new VBox(5);
        boolean isFirstRectangle = true;
        boolean isSecondRectangle = false;
        boolean isThirdRectangle = false;
        for (int i = 0; i < 3; i++) {
            Rectangle smallRect = new Rectangle(30, 30);
            smallRect.setFill(Color.WHITE);

            StackPane smallRectStack = new StackPane();
            smallRectStack.getChildren().add(smallRect);

            if (isFirstRectangle) {
                VBox textVBox = new VBox();

                Label accLabel = new Label("ACC");
                accLabel.setFont(new Font(10));
                accLabel.setTextFill(Color.BLACK);

                Label valueLabel = new Label("0");
                valueLabel.setFont(new Font(10));
                valueLabel.setTextFill(Color.BLACK);
                accValueLabels.add(valueLabel);

                textVBox.getChildren().addAll(accLabel, valueLabel);
                textVBox.setAlignment(Pos.CENTER);

                smallRectStack.getChildren().add(textVBox);
                isFirstRectangle = false;
                isSecondRectangle = true;
            } else if (isSecondRectangle) {
                VBox textVBox = new VBox();

                Label bakLabel = new Label("BAK");
                bakLabel.setFont(new Font(10));
                bakLabel.setTextFill(Color.BLACK);

                Label valueLabel = new Label("0");
                valueLabel.setFont(new Font(10));
                valueLabel.setTextFill(Color.BLACK);
                bakValueLabels.add(valueLabel);

                textVBox.getChildren().addAll(bakLabel, valueLabel);
                textVBox.setAlignment(Pos.CENTER);

                smallRectStack.getChildren().add(textVBox);
                isSecondRectangle = false;
                isThirdRectangle = true;
            } else if (isThirdRectangle) {
                VBox textVBox = new VBox();

                Label lastLabel = new Label("CURR");
                lastLabel.setFont(new Font(10));
                lastLabel.setTextFill(Color.BLACK);

                Label valueLabel = new Label("0");
                valueLabel.setFont(new Font(10));
                valueLabel.setTextFill(Color.BLACK);
                currValueLabels.add(valueLabel);

                textVBox.getChildren().addAll(lastLabel, valueLabel);
                textVBox.setAlignment(Pos.CENTER);

                smallRectStack.getChildren().add(textVBox);
                isThirdRectangle = false;
            }

            smallRectsVBox.getChildren().add(smallRectStack);
        }
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(textArea, smallRectsVBox);

        stack.getChildren().addAll(rect, hbox);
        return stack;
    }

    /**
     * Returns the current text in the human text area.
     *
     * @return A string representing the text in the human text area.
     */
    public String getHumanText() {
        return humanTextArea.getText();
    }

    /**
     * Sets the text in the human text area.
     *
     * @param text The text to be set in the human text area.
     */
    public void setHumanText(String text) {
        humanTextArea.setText(text);
    }

    /**
     * Gets the text in the given silo text area.
     *
     *
     */
    public String getTextFromArea1() {
        return textArea1.getText();


    }
    /**
     * Sets the text in the given silo text area.
     *
     *
     */
    public void setTextToArea1(String text) {
        textArea1.setText(text);
    }

    /**
     * Gets the text in the given silo text area.
     *
     *
     */
    public String getTextFromArea2() {
        return textArea2.getText();
    }

    /**
     * Sets the text in the given silo text area.
     *
     *
     */
    public void setTextToArea2(String text) {
        textArea2.setText(text);
    }

    /**
     * Gets the text in the given silo text area.
     *
     *
     */

    public String getTextFromArea3() {
        return textArea3.getText();
    }
    /**
     * sets the text in the given silo text area.
     *
     *
     */
    public void setTextToArea3(String text) {
        textArea3.setText(text);
    }
    /**
     * Gets the text in the given silo text area.
     *
     *
     */
    public String getTextFromArea4() {
        return textArea4.getText();
    }
    /**
     * Sets the text in the given silo text area.
     *
     *
     */
    public void setTextToArea4(String text) {
        textArea4.setText(text);
    }

    /**
     * Gets the text in the given silo text area.
     *
     *
     */
    public String getTextFromArea5() {
        return textArea5.getText();
    }
    /**
     * Sets the text in the given silo text area.
     *
     *
     */
    public void setTextToArea5(String text) {
        textArea5.setText(text);
    }

    /**
     * Gets the text in the given silo text area.
     *
     *
     */
    public String getTextFromArea6() {
        return textArea6.getText();
    }
    /**
     * Sets the text in the given silo text area.
     *
     *
     */
    public void setTextToArea6(String text) {
        textArea6.setText(text);
    }

    /**
     * Gets the text in the given silo text area.
     *
     *
     */
    public String getTextFromArea7() {
        return textArea7.getText();
    }
    /**
     * Sets the text in the given silo text area.
     *
     *
     */
    public void setTextToArea7(String text) {
        textArea7.setText(text);
    }
    /**
     * Gets the text in the given silo text area.
     *
     *
     */
    public String getTextFromArea8() {
        return textArea8.getText();
    }
    /**
     * Sets the text in the given silo text area.
     *
     *
     */
    public void setTextToArea8(String text) {
        textArea8.setText(text);
    }
    /**
     * Gets the text in the given silo text area.
     *
     *
     */
    public String getTextFromArea9() {
        return textArea9.getText();
    }
    /**
     * Sets the text in the given silo text area.
     *
     *
     */
    public void setTextToArea9(String text) {
        textArea9.setText(text);
    }

    /**
     * Gets the text in the given silo text area.
     *
     *
     */
    public String getTextFromArea10() {
        return textArea10.getText();
    }
    /**
     * Sets the text in the given silo text area.
     *
     *
     */
    public void setTextToArea10(String text) {
        textArea10.setText(text);
    }

    /**
     * Gets the text in the given silo text area.
     *
     *
     */
    public String getTextFromArea11() {
        return textArea11.getText();
    }
    /**
     * Sets the text in the given silo text area.
     *
     *
     */
    public void setTextToArea11(String text) {
        textArea11.setText(text);
    }

    /**
     * Gets the text in the given silo text area.
     *
     *
     */
    public String getTextFromArea12() {
        return textArea12.getText();
    }
    /**
     * Sets the text in the given silo text area.
     *
     * @param text The text to be set in the human text area.
     */
    public void setTextToArea12(String text) {
        textArea12.setText(text);
    }

    /**
     * Retrieves the text from the first input text area.
     *
     * @return The current text in the first input text area.
     */
    public String getTextFromInTextArea1() {
        return inTextArea1.getText();
    }

    /**
     * Sets the given text to the first input text area.
     *
     * @param text The text to set in the first input text area.
     */

    public void setTextToInTextArea1(String text) {
        inTextArea1.setText(text);
    }
    /**
     * Retrieves the text from the second input text area.
     *
     * @return The current text in the second input text area.
     */

    public String getTextFromInTextArea2() {
        return inTextArea2.getText();
    }

    /**
     * Sets the given text to the second input text area.
     *
     * @param text The text to set in the second input text area.
     */
    public void setTextToInTextArea2(String text) {
        inTextArea2.setText(text);
    }

    public String getTextFromOutELeft() {
        return outELeftTextArea.getText();
    }

    public void setTextToOutELeft(String text) {
        outELeftTextArea.setText(text);
    }

    public String getTextFromOutERight() {
        return outERightTextArea.getText();
    }

    public void setTextToOutERight(String text) {
        String greenTextColorStyle = "-fx-text-fill: green;";
        outERightTextArea.setText(text);
        outERightTextArea.setStyle(greenTextColorStyle);
    }

    public String getTextFromOutLLeft() {
        return outLLeftTextArea.getText();
    }

    public void setTextToOutLLeft(String text) {
        outLLeftTextArea.setText(text);
    }

    public String getTextFromOutLRight() {
        return outLRightTextArea.getText();
    }

    public void setTextToOutLRight(String text) {
        String greenTextColorStyle = "-fx-text-fill: green;";
        outLRightTextArea.setText(text);
        outLRightTextArea.setStyle(greenTextColorStyle);
    }

    /**
     * Retrieves the accumulator value from a specified index.
     *
     * @param index The index of the accumulator value to retrieve.
     * @return The accumulator value at the specified index.
     * @throws IllegalArgumentException if the index is invalid.
     */
    public int getAccValue(int index) {
        if (index < 0 || index >= accValueLabels.size()) {
            throw new IllegalArgumentException("Invalid index");
        }
        return Integer.parseInt(accValueLabels.get(index).getText());
    }

    /**
     * Retrieves the backup value from a specified index.
     *
     * @param index The index of the backup value to retrieve.
     * @return The backup value at the specified index.
     * @throws IllegalArgumentException if the index is invalid.
     */
    public int getBakValue(int index) {
        if (index < 0 || index >= bakValueLabels.size()) {
            throw new IllegalArgumentException("Invalid index");
        }
        return Integer.parseInt(bakValueLabels.get(index).getText());
    }

    /**
     * Retrieves the last value from a specified index.
     *
     * @param index The index of the last value to retrieve.
     * @return The last value at the specified index.
     * @throws IllegalArgumentException if the index is invalid.
     */
    public int getLastValue(int index) {
        if (index < 0 || index >= currValueLabels.size()) {
            throw new IllegalArgumentException("Invalid index");
        }
        return Integer.parseInt(currValueLabels.get(index).getText());
    }


    /**
     * Sets the accumulator value from a specified index.
     *
     * @param index The index of the accumulator value to retrieve.
     * @return The accumulator value at the specified index.
     * @throws IllegalArgumentException if the index is invalid.
     */
    public void setAccValue(int index, int value) {
        int realBox;
        int row = index/ TIS_Controller.getColumns();
        int column = index % TIS_Controller.getColumns();
        realBox = (row*4) + column;
        if (realBox < 0 || realBox >= accValueLabels.size()) {
            throw new IllegalArgumentException("Invalid index");
        }
        accValueLabels.get(realBox).setText(Integer.toString(value));
    }

    /**
     * Sets the backup value from a specified index.
     *
     * @param index The index of the backup value to retrieve.
     * @return The backup value at the specified index.
     * @throws IllegalArgumentException if the index is invalid.
     */
    public void setBakValue(int index, int value) {
        int realBox;
        int row = index/ TIS_Controller.getColumns();
        int column = index % TIS_Controller.getColumns();
        realBox = (row*4) + column;
        if (realBox < 0 || realBox >= bakValueLabels.size()) {
            throw new IllegalArgumentException("Invalid index");
        }
        bakValueLabels.get(realBox).setText(Integer.toString(value));
    }

    /**
     * sets the last value from a specified index.
     *
     * @param index The index of the last value to retrieve.
     * @return The last value at the specified index.
     * @throws IllegalArgumentException if the index is invalid.
     */
    public void setLastValue(int index, int value) {
        int realBox;
        int row = index/ TIS_Controller.getColumns();
        int column = index % TIS_Controller.getColumns();
        realBox = (row*4) + column;
        if (realBox < 0 || realBox >= currValueLabels.size()) {
            throw new IllegalArgumentException("Invalid index");
        }
        currValueLabels.get(realBox).setText(Integer.toString(value));
    }
    /**
     * Retrieves the current value of the artificial delay slider.
     *
     * @return The current value of the artificial delay slider.
     */
    public double getArtificialDelayValue() {
        return artificialDelaySlider.getValue();
    }
    // Getters and Setters for Column Spacer Value Labels
    public String getHorizontalValue(int index) {
        return columnSpacerValueLabels.get(index).getText();
    }
    //Setters for Column Spacer Value Labels
    public void setHorizontalValue(int index, String value) {
        columnSpacerValueLabels.get(index).setText(value);
    }

    // Getters for Small Text Area Value Labels
    public String getVerticalValue(int index) {
        return smallTextAreaValueLabels.get(index).getText();
    }
    //Setters for Small Text Area Value Labels
    public void setVerticalValue(int index, String value) {
        smallTextAreaValueLabels.get(index).setText(value);
    }


    /**
     * Launches program
     * @param args passed in
     */
    public static void main(String[] args) {
        launch(args);
    }

    private Scene createStartScene(){
        StackPane startLayout = new StackPane();
        Scene startScene = new Scene(startLayout, size[0], size[1]);

        Label titleLabel = new Label("TIS Game Thing");
        titleLabel.setStyle("-fx-font-family: 'Comic Sans MS'; -fx-font-size: 36px; -fx-font-weight: bold;");
        StackPane.setAlignment(titleLabel, javafx.geometry.Pos.TOP_CENTER);
        startLayout.getChildren().add(titleLabel);

        Button startButton = new Button("Play");
        startLayout.getChildren().add(startButton);

        // Button to start the game
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(createGameScene()); // set scene
                TIS_Controller.start(); // start game
            }
        });

        return startScene;
    }

    // When you hit the play button, this starts the game
    private Scene createGameScene(){
        this.TIS_Controller = new Controller(size, this);  // initialize controller
        return TIS_Controller.getUI().getScene();

    }

    // When a game has thrown an exit code
    private Scene gameOverScene(){
        return null;
    }
}


