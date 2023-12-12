import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.application.Application;
import javafx.scene.text.TextAlignment;
import javafx.animation.AnimationTimer;

 /**
 * @author Luka Bazar
 * @author Ryan Scherbarth
 * @author Roget Davis 
 * @author Samuel Dauk
 * <p>
 * Main method for Donkey Kong Jr.
 */
public class DonkeyKongJr extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start for JavaFX stage
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage primaryStage) {
        String hostname = getParameters().getUnnamed().get(0);
        int port = Integer.parseInt(getParameters().getUnnamed().get(1));

        int multi = 3;
        int pixelWidth = 256;
        int pixelHeight = 240;

        int width = pixelWidth * multi;
        int height = pixelHeight * multi;

        final Title[] titleScreen = {new Title(width, height, hostname, port)};
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (titleScreen[0].getCurrentLevel() != null) {
                    if (titleScreen[0].getCurrentLevel().isOver()) {
                        titleScreen[0] = new Title(width, height, hostname, port);
                        primaryStage.setScene(titleScreen[0].getScene());
                    }
                }
            }
        };
        animationTimer.start();

        primaryStage.setTitle("Donkey Kong Jr.");
        primaryStage.getIcons().add(new Image("icon.png"));
        primaryStage.setScene(titleScreen[0].getScene());
        primaryStage.show();

        Stage instructions = new Stage();
        instructions.setHeight(height / 2.0);
        instructions.setWidth(width);
        Label instructionText = getLabel();
        instructionText.setMaxWidth(instructions.getWidth());
        instructionText.setPadding(new Insets(10, 20, 10, 20));
        Pane instructionPane = new Pane();
        instructionPane.getChildren().add(instructionText);
        instructions.setScene(new Scene(instructionPane));
        instructions.show();
    }

    /**
     * Create instruction label text
     *
     * @return Label with instructions
     */
    private static Label getLabel() {
        Label instructionText = new Label("""
                Instructions
                Jump to grab onto vines/chains, Junior climbs up very slow on one vine/chain but by reaching for
                another he climbs much faster, however holding onto only one vine/chain makes sliding down faster.
                The player with the flashing graphic overhead is your player!
                                
                Game Mode
                Game B: A custom level for this project, Junior only has one life, drop on other players to take out\s
                fellow players, fruit respawns, and enemies spawn faster as the game progresses.
                Survive as long as possible, out last the other players, collect fruit, and drop
                that fruit on enemies to get a high score before the enemies overwhelm Junior!\s
                                
                Controls
                Left/Right: Move right and left on the ground, or reach/grab a vine/chain
                Up/Down: Climb up and down the vines/chains
                Space: Jump
                Enter: Start the game with the highlighted selection on title screen\s""");
        instructionText.setTextAlignment(TextAlignment.JUSTIFY);
        instructionText.setWrapText(true);
        return instructionText;
    }
}
