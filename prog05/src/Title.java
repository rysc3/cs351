import java.util.List;
import javafx.scene.Scene;
import java.util.ArrayList;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.image.ImageView;

/**
 * @author Luka Bazar
 * @author Ryan Scherbarth
 * @author Roget Davis 
 * @author Samuel Dauk
 * <p>
 * Title Screen and handling
 */
public class Title {

    /**
     * Title screen backgrounds
     */
    private enum Titles {
        TITLE1("backgrounds/title-01.png"), TITLE2("backgrounds/title-multi.png");

        private final ImageView img;

        Titles(String imgStr) {
            ImageView tempImg = null;
            try {
                tempImg = new ImageView(imgStr);
            } catch (Exception e) {
                System.out.println("File not found: " + imgStr);
            }
            img = tempImg;
        }

        /**
         * Get image of Sprite
         *
         * @return Image
         */
        public Image getImage() {
            return this.img.getImage();
        }
    }

    private final Scene scene;
    private final BorderPane pane;
    private final PlayerData playerData;
    private Level currentLevel;
    private final String hostname;
    private final int port;
    private Titles currentTitle = Titles.TITLE2;

    /**
     * Title screen
     *
     * @param width  width of window
     * @param height height of window
     */
    public Title(int width, int height, String hostname, int port) {
        this.pane = new BorderPane();
        this.scene = new Scene(this.pane, width, height);
        this.playerData = new PlayerData();
        this.hostname = hostname;
        this.port = port;
        changeBackground(currentTitle.getImage());
    }

    /**
     * Change background
     *
     * @param backgroundImg image to change to
     */
    private void changeBackground(Image backgroundImg) {
        BackgroundSize backgroundSize = new BackgroundSize(scene.getWidth(), scene.getHeight(), false,
                false, false, false);
        pane.setBackground(new Background(new BackgroundImage(backgroundImg, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, null, backgroundSize)));
        handlePress();
    }

    /**
     * Handles key presses for Title screen
     */
    private void handlePress() {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                List<Label> labels = makeLabels();
                int multi = (int) scene.getHeight() / 240;
                for (int idx = 0; idx < 4; idx++) {
                    playerData.addPlayer(24 * multi, 200 * multi, 0, true);
                }

                Client client = new Client(hostname, port, playerData);
                Thread clientThread = new Thread(client);
                clientThread.start();

                try {
                    Thread.sleep(100); // Give client time to get player number
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(client.getPlayerNum());

                currentLevel = new Level(scene, pane, labels, multi, client, playerData, client.getPlayerNum(), Level.Mode.LEVEL2);
                changeBackground(new Image("backgrounds/background-02.png"));

                HBox hBox = new HBox();
                hBox.getChildren().addAll(labels);
                hBox.setPadding(new Insets(10));
                hBox.setSpacing(10);
                pane.setTop(hBox);
            }
        });
    }

    /**
     * Create labels based on Game Mode
     *
     * @return List of labels
     */
    private List<Label> makeLabels() {
        List<Label> labels = new ArrayList<>();
        if (currentTitle == Titles.TITLE1) {
            labels.add(new Label("Score: 5000"));
            labels.add(new Label("Lives: 3"));
        }
        else {
            labels.add(new Label("Score: 0"));
            labels.add(new Label("Lives: 1"));
        }

        for (Label label : labels) {
            label.setTextFill(Color.WHITE);
            label.setFont(new Font("Stencil", 5 * (scene.getHeight() / 240)));
        }
        return labels;
    }

    /**
     * Get the current level
     *
     * @return current level
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Get the scene
     *
     * @return scene
     */
    public Scene getScene() {
        return scene;
    }

}
