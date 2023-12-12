import java.io.*;
import java.util.*;
import javafx.util.Pair;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.animation.AnimationTimer;
import java.nio.charset.StandardCharsets;


 /**
 * @author Luka Bazar
 * @author Ryan Scherbarth
 * @author Roget Davis 
 * @author Samuel Dauk
 * <p>
 * Main level with GameObjects
 */
public class Level {

    /**
     * Level as Mode
     */
    public enum Mode {
        LEVEL1, LEVEL2
    }

    private enum PlatformType {
        STANDARD, ROPE
    }

    private final Scene scene;
    private final Pane pane;
    private final List<Label> labels;
    private final int multi;
    private final Mode level;
    private final PlayerData playerData;
    private final int playerNum;
    private final Client client;
    private List<GameObject> platforms;
    private List<Rope> ropes;
    private List<Collectable> fruits;
    private List<Enemy> enemies;
    private Player player;
    private boolean isWin = false;
    private boolean isPressed = false;
    private boolean isOver = false;
    private int numRopes = 0;
    private final Random random = new Random(System.currentTimeMillis());
    /*
     * Standings format: List<Integer> ->
     * [playerId, score, playerId, score, playerId, score, playerId, score]
     */
    private List<Integer> standings;

    private final List<Player> otherPlayers = new ArrayList<>();

    /**
     * Places all game objects onto the level
     *
     * @param scene JavaFX scene
     * @param pane  Pane to place assets
     * @param multi Multiple used to scale window
     */
    public Level(Scene scene, Pane pane, List<Label> labels, int multi, Client client,
            PlayerData playerData, int playerNum, Mode level) {

        this.scene = scene;
        this.pane = pane;
        this.multi = multi;
        this.level = level;
        this.labels = labels;
        this.playerData = playerData;
        this.playerNum = playerNum;
        this.client = client;
        this.standings = new ArrayList<>(8);
        makeGameObjects();
        makeCollectables();
        makeEnemies();
        makePlayer();
        addAll();

        for (int i = 0; i < 3; i++) {
            Player player = new Player(24 * multi, 200 * multi, 27 * multi, 16 * multi, 1);
            otherPlayers.add(player);
            pane.getChildren().add(player.getGameObject());
        }
        otherPlayers.add(playerNum, player);

        play();
    }

    /**
     * Are players ready to start game
     * @return true if yes, false otherwise
     */
    private boolean allReady() {
        for (int i = 0; i < 4; i++) {
            if (!playerData.getPlayerData(i).isReady() && i != playerNum) {
                return false;
            }
        }
        return true;
    }

    /**
     * Holds animation timer in order to update the game
     */
    public void play() {
        AnimationTimer timer = new AnimationTimer() {
            int count = 0;
            int altCount = 300;
            long last = 0;

            @Override
            public void handle(long now) {
                if (allReady()) {
                    if (now - last >= 8_333_333) {
                        if (altCount == 0) {
                            altCount = 1;
                        }
                        if (isWin) {
                            this.stop();
                            popUp();
                        }
                        if (player.getLives() == 0) {
                            killPlayer();
                            this.stop();
                            player.getGameObject().relocate(0, 0);
                            pane.getChildren().remove(player.getGameObject());
                            popUp();
                        }
                        if (count % 120 == 0) {
                            for (int i = fruits.size() - 1; i >= 0; i--) {
                                if (!pane.getChildren().contains(fruits.get(i).getGameObject())
                                        && level == Mode.LEVEL2) {
                                    fruits.get(i).setFalling(false);
                                    fruits.get(i).respawn();
                                    pane.getChildren().add(fruits.get(i).getGameObject());
                                    pane.getChildren().add(fruits.get(i).getHitBox());
                                    break;
                                }
                            }
                            if (getScore() == 0 && level == Mode.LEVEL1) {
                                labels.get(0).setText("Score: 0");
                            } else {
                                if (level == Mode.LEVEL1) {
                                    labels.get(0).setText("Score: " + (getScore() - 100));
                                } else {
                                    // Add timing score to each player.
                                    // Debugging
                                    //System.out.println("***");
                                    for (int i = 0; i < client.getPlayerData().getNumPlayers(); i++) {
                                        if (client.getPlayerData().getPlayerData(i).getIsAlive()) {
                                            client.getPlayerData().getPlayerData(i).addScore(100);

                                            // Mark current player:
                                            if (i == playerNum) {
                                                //System.out.print("[ ");
                                                //System.out.print("Player " + i + " Score: "
                                                //        + client.getPlayerData().getPlayerData(i).getScore());
                                                //System.out.print(" ]\n");
                                            } else {
                                                //System.out.println("Player " + i + " Score: "
                                                //       + client.getPlayerData().getPlayerData(i).getScore());
                                            }
                                        } else {
                                            if (i == playerNum) {
                                                //System.out.print("[ ");
                                                //System.out.print("Player " + i + " is dead.");
                                                //System.out.print(" ]\n");
                                            } else {
                                                //System.out.println("Player " + i + " is dead.");
                                            }
                                        }
                                    }
                                    //System.out.println("***");

                                    labels.get(0).setText("Score: " + (getScore() + 100));
                                }
                            }
                        }
                        if (count % altCount == 0 && level == Mode.LEVEL2) {
                            Enemy enemyToAdd = new Enemy(48 * multi, 56 * multi, 16 * multi, 8 * multi);
                            enemyToAdd.setXVelocity(1.5 / 3.0 * multi);
                            enemyToAdd.setYVelocity(1.5 / 3.0 * multi);
                            enemies.add(enemyToAdd);
                            altCount -= 5;
                        }
                        if (count % 300 == 0 && level == Mode.LEVEL1) {
                            Enemy enemyToAdd = new Enemy(80 * multi, 56 * multi, 16 * multi, 8 * multi);
                            enemyToAdd.setXVelocity(1.5 / 3.0 * multi);
                            enemyToAdd.setYVelocity(1.5 / 3.0 * multi);
                            enemies.add(enemyToAdd);
                        }
                        if (count % 8 == 0) {
                            if (player.getCycle() == 0) {
                                for (Player otherPlayer : otherPlayers) {
                                    otherPlayer.setCycle(1);
                                }
                                for (Enemy enemy : enemies) {
                                    enemy.setCycle(1);
                                }
                            } else if (player.getCycle() == 1) {
                                for (Player otherPlayer : otherPlayers) {
                                    otherPlayer.setCycle(2);
                                }
                                for (Enemy enemy : enemies) {
                                    enemy.setCycle(0);
                                }
                            } else if (player.getCycle() == 2) {
                                for (Player otherPlayer : otherPlayers) {
                                    otherPlayer.setCycle(3);
                                }
                            } else {
                                for (Player otherPlayer : otherPlayers) {
                                    otherPlayer.setCycle(0);
                                }
                            }
                        }
                        if (count % 25 == 0) {
                            if (player.getHasOverA()) {
                                player.setOverToB();
                                player.setHasOverA(false);
                            } else {
                                player.setOverToA();
                                player.setHasOverA(true);
                            }
                        }
                        update();
                        last = now;
                        count = (count + 1) % 3600;
                    }
                }

                // player has died
                playerData.setPlayerData(playerNum, player.getX(), player.getY(), getScore(), player.xVelocity(),
                        player.yVelocity(), player.getLives() > 0, player.isJumping(),
                        player.isWalking(), player.isGrounded(), player.isClimbing(), player.isClimbingSpecial(),
                        (int) player.getGameObject().getScaleX(), player.isCycling(), true);
            }
        };
        timer.start();

    }

    public boolean allPlayersDead() {
        //System.out.println("-----");
        for (int i = 0; i < 3; i++) {
           //System.out.println("player " + i + ": " + client.getPlayerData().getPlayerData(i).getIsAlive());
            if (client.getPlayerData().getPlayerData(i).getIsAlive()) {
                return false;
            }
        }
       // System.out.println("-----");
        return true;
    }

    /**
     * Is the current game over
     *
     * @return true if it is, false if not
     */
    public boolean isOver() {
        return isOver;
    }

    /**
     * Get score from label
     *
     * @return int score
     */
    public int getScore() {
        return Integer.parseInt(labels.get(0).getText().substring(7));
    }

    /**
     * End of game popups
     */
    // @ryan here
    private void popUp() {
        Stage stage = new Stage();
        stage.setWidth(scene.getWidth() / 4);
        stage.setHeight(scene.getHeight() / 4);
        Label label = new Label();
        label.setTextAlignment(TextAlignment.CENTER);
        Button button = new Button("Back to Title Screen");
        VBox vBox = new VBox(label, button);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vBox);
        stage.setScene(new Scene(borderPane));
        button.setOnAction(event -> {
            isOver = true;
            client.stopClient();
            stage.close();
        });

        stage.setOnCloseRequest(event -> {
            isOver = true;
            client.stopClient();
        });
        if (level == Mode.LEVEL2) {
            List<Integer> sortedStandings = sortStandings(standings);

            int numPlayers = sortedStandings.size() / 2;

            // Set the label text with standings information
            StringBuilder standingsInfo = new StringBuilder("Final Standings:\n");
            for (int i = 0; i < numPlayers; i++) {
                int playerId = sortedStandings.get(i * 2); // Get playerId
                int score = sortedStandings.get(i * 2 + 1); // Get score

                standingsInfo.append("Player ").append(playerId).append(": ").append(score).append("\n");
            }

            label.setText("Game Over!\n" + standingsInfo.toString());
        } else {
            // Code to handle other levels if needed
            // For example, set a default message or handle other game modes
            label.setText("Game Over!\nLevel completed. No standings available for this level.");
        }

        stage.show();
    }

    /**
     * Updates the screen based on user input and gravity
     */
    private void update() {

        for (int i = 0; i < otherPlayers.size(); i++) {
            if (i != playerNum && otherPlayers.get(i).getLives() <= 0) {
                pane.getChildren().remove(otherPlayers.get(i).getGameObject());
            }
            else if(i != playerNum && !playerData.getPlayerData(i).getIsAlive()) {
                otherPlayers.get(i).respawn(new Label());
            }
            else if(i != playerNum && otherPlayers.get(i).getLives() > 0) {
                Player dk = otherPlayers.get(i);
                dk.setXVelocity(playerData.getPlayerData(i).xVelocity());
                dk.setYVelocity(playerData.getPlayerData(i).yVelocity());
                dk.setX(playerData.getPlayerData(i).getX() + playerData.getPlayerData(i).xVelocity());
                dk.setY(playerData.getPlayerData(i).getY() + playerData.getPlayerData(i).yVelocity());
                dk.getGameObject().setScaleX(playerData.getPlayerData(i).getDirection());
                dk.setJumping(playerData.getPlayerData(i).isJumping());
                dk.setWalking(playerData.getPlayerData(i).isWalking());
                dk.setGrounded(playerData.getPlayerData(i).isGrounded());
                dk.setClimbing(playerData.getPlayerData(i).isClimbing());
                dk.setClimbingSpecial(playerData.getPlayerData(i).isClimbingSpecial());
                dk.isCycle(playerData.getPlayerData(i).isCycle());
                dk.changeSprite();
            }
        }

        if (player.getFallCount() >= 8 * multi) {
            if (player.isGrounded()) {
                if (level == Mode.LEVEL1) {
                    labels.get(0).setText("Score: 5000");
                }
                player.respawn(labels.get(1));
            }
        }

        player.changeSprite();

        for (Enemy enemy : enemies) {
            if (snapToBounds(enemy)) {
                enemy.switchXDir();
                enemy.setXVelocity(enemy.xVelocity());
            }
            enemy.setYVelocity(enemy.yVelocity() + 0.5 / 3.0 * multi);
            enemy.setX(enemy.getX() + enemy.xVelocity());
            enemy.setY(enemy.getY() + enemy.yVelocity());
            pane.getChildren().remove(enemy.getGameObject());
            pane.getChildren().add(enemy.getGameObject());
            enemy.changeSprite();
        }

        player.setX(player.getX() + player.xVelocity());
        player.setY(player.getY() + player.yVelocity());

        // Gravity
        if (!player.isClimbing()) {
            player.setYVelocity(player.yVelocity() + 0.25 / 3.0 * multi);
        }

        snapToBounds(player);
        handleCollisions();

        scene.setOnKeyPressed(this::keyPressed);
        scene.setOnKeyReleased(this::keyReleased);
    }

    /**
     * Handles key release events
     *
     * @param event KeyEvent
     */
    private void keyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
            player.setXVelocity(0);
            player.setWalking(false);
            isPressed = false;
        } else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
            player.setYVelocity(0);
            player.isCycle(false);
            isPressed = false;
        }
    }

    /**
     * Handles key press events
     *
     * @param event KeyEvent
     */
    private void keyPressed(KeyEvent event) {
        if (player.isFalling()) {
        } else if (event.getCode() == KeyCode.LEFT && !isPressed) {
            if (player.isClimbingSpecial()) {
                player.getGameObject().setScaleX(-1);
                player.getOverhead().setScaleX(-1);
                player.setX(player.getX() - 2.5 * multi);
                player.setClimbing(false);
                player.setClimbingSpecial(false);
                isPressed = true;
            } else if (player.isClimbing()) {
                int left;
                if (level == Mode.LEVEL1) {
                    left = 0;
                } else {
                    left = 24 * multi;
                }
                if (player.getGameObject().getScaleX() == 1 && player.getX() - player.getWidth() / 2 > left) {
                    player.setClimbingSpecial(true);
                    player.setX(player.getX() - player.getWidth() / 2 + ropes.get(0).getWidth());
                    player.getGameObject().setScaleX(-1);
                    player.getOverhead().setScaleX(-1);
                }
                player.getGameObject().setScaleX(1);
                player.getOverhead().setScaleX(1);
            } else {
                player.setXVelocity(-2 / 3.0 * multi);
                player.setWalking(true);
                player.getGameObject().setScaleX(-1);
                player.getOverhead().setScaleX(-1);
            }
        } else if (event.getCode() == KeyCode.RIGHT && !isPressed) {
            if (player.isClimbingSpecial()) {
                player.getGameObject().setScaleX(1);
                player.getOverhead().setScaleX(1);
                player.setX(player.getX() + 5.5 * multi);
                player.setClimbing(false);
                player.setClimbingSpecial(false);
                isPressed = true;
            } else if (player.isClimbing()) {
                int right;
                if (level == Mode.LEVEL1) {
                    right = 256 * multi;
                } else {
                    right = 232 * multi;
                }
                if (player.getGameObject().getScaleX() == -1 && player.getX() + 3 * player.getWidth() / 2 < right) {
                    player.setClimbingSpecial(true);
                    player.setX(player.getX() + player.getWidth() / 2);
                    player.getGameObject().setScaleX(1);
                    player.getOverhead().setScaleX(1);
                }
                player.getGameObject().setScaleX(-1);
                player.getOverhead().setScaleX(-1);
            } else {
                player.setXVelocity(2 / 3.0 * multi);
                player.setWalking(true);
                player.getGameObject().setScaleX(1);
                player.getOverhead().setScaleX(1);
            }
        } else if (event.getCode() == KeyCode.UP && player.isClimbing()) {
            if (player.isClimbingSpecial()) {
                if (numRopes == 1) {
                    player.setYVelocity(0);
                    player.setClimbing(false);
                    player.setClimbingSpecial(false);
                } else {
                    player.setYVelocity(-2 / 3.0 * multi);
                }
            } else {
                player.setYVelocity(-0.75 / 3.0 * multi);
            }
            player.isCycle(true);
        } else if (event.getCode() == KeyCode.DOWN && player.isClimbing()) {
            if (player.isClimbingSpecial()) {
                if (numRopes == 1) {
                    player.setYVelocity(0);
                    player.setClimbing(false);
                    player.setClimbingSpecial(false);
                } else {
                    player.setYVelocity(0.75 / 3.0 * multi);
                }
                player.isCycle(true);
            } else {
                if (numRopes == 0) {
                    player.setClimbing(false);
                }
                player.setYVelocity(2 / 3.0 * multi);
            }
        } else if (event.getCode() == KeyCode.SPACE && !player.isJumping() && !player.isClimbing()) {
            player.setYVelocity(-5 / 3.0 * multi);
            player.setJumping(true);
        }
    }

    /**
     * Handles all collision factors
     */
    private void handleCollisions() {

        player.setGrounded(false);

        platformCollision();

        numRopes = 0;
        for (Rope rope : ropes) {
            if (isCollision(player, rope) && player.getY() + player.getHeight() / 2 < rope.getY() + rope.getHeight() &&
                    player.getY() > rope.getY()) {
                if (rope.isWinner()) {
                    isWin = true;
                }
                if (player.isJumping()) {
                    player.setYVelocity(0);
                    player.setJumping(false);
                }
                if (!player.isClimbing()) {
                    player.setYVelocity(0);
                    player.setX(rope.getX() - player.getWidth() / 2 - rope.getWidth());
                }
                player.setXVelocity(0);
                player.setClimbing(true);
                player.setGrounded(false);
                numRopes++;
            }
        }

        for (Collectable fruit : fruits) {
            if (player.getGameObject().getBoundsInParent().intersects(fruit.getHitBox().getBoundsInParent())) {
                if (!fruit.isFalling()) {

                    //System.out.println("Fruit collected, +400 Score");
                    playerData.getPlayerData(playerNum).addScore(400);

                    labels.get(0).setText("Score: " + (getScore() + 400));
                }
                fruit.setFalling(true);
                fruit.getHitBox().relocate(0, 0);
            }
            for (Enemy enemy : enemies) {
                if (isCollision(fruit, enemy) && fruit.isFalling()) {
                    labels.get(0).setText("Score: " + (getScore() + 400));
                    enemy.getGameObject().relocate(256 * multi, 240 * multi);
                    pane.getChildren().remove(enemy.getGameObject());
                }
            }
            if (fruit.isFalling()) {
                fruit.setY(fruit.getY() + 2 * multi);
                if (fruit.getY() > 240 * multi) {
                    fruit.getGameObject().relocate(0, 0);
                    pane.getChildren().remove(fruit.getGameObject());
                    pane.getChildren().remove(fruit.getHitBox());
                    fruits.set(fruits.indexOf(fruit), fruits.get(0));
                    fruits.set(0, fruit);
                }
            }
        }
        for (Enemy enemy : enemies) {
            if (isCollision(player, enemy)) {
                if (level == Mode.LEVEL1) {
                    labels.get(0).setText("Score: 5000");
                }
                player.respawn(labels.get(1));
                enemy.setX(pane.getWidth() + enemy.getWidth());
                enemy.setY(pane.getHeight() + enemy.getHeight());
                pane.getChildren().remove(enemy.getGameObject());
            }
            for (GameObject platform : platforms) {
                if (isCollision(platform, enemy) && enemy.getY() < 72 * multi
                        && enemy.getGameObject().getRotate() == 0) {
                    enemy.setYVelocity(0);
                    enemy.setY(platform.getY() - enemy.getHeight());
                }
            }
            for (Rope rope : ropes) {
                if (enemy.getX() == rope.getX() + rope.getWidth() / 2 &&
                        enemy.getY() >= rope.getY() - 2 * enemy.getHeight() && enemy.getGameObject().getRotate() == 0) {
                    int randomInt = random.nextInt(0, 4);
                    if (randomInt == 0) {
                        enemy.setX(rope.getX() + rope.getWidth() / 2 - enemy.getWidth() / 2);
                        enemy.setYVelocity(2.5 / 3 * multi);
                        enemy.changeRotate();
                    }
                }
            }
        }

        opponentCollision();

    }

    /**
     * Handles opponent collision
     */
    private void opponentCollision() {
        for (int i = 0; i < otherPlayers.size(); i++) {
            if (i != playerNum) {
                Player opponent = otherPlayers.get(i);
                if (isCollision(player, opponent)) {
                    if (!opponent.isGrounded() && player.isGrounded())
                        player.respawn(labels.get(1));
                    else if (!player.isGrounded() && opponent.isGrounded())
                        opponent.respawn(new Label());
                }
            }
            for (int j = 0; j < otherPlayers.size(); j++) {
                if (i != j && i != playerNum && j != playerNum) {
                    Player opponent1 = otherPlayers.get(i);
                    Player opponent2 = otherPlayers.get(j);
                    if (isCollision(opponent1, opponent2)) {
                        if (!opponent2.isGrounded() && opponent1.isGrounded())
                            opponent1.respawn(new Label());
                        else if (!opponent1.isGrounded() && opponent2.isGrounded())
                            opponent2.respawn(new Label());
                    }
                }
            }
        }
    }

    /**
     * Handles platform collisions
     */
    private void platformCollision() {
        for (GameObject platform : platforms) {
            if (isCollision(player, platform) && onPlatform(player, platform) && player.getY() < platform.getY()) {
                player.setYVelocity(0);
                player.setJumping(false);
                player.setClimbing(false);
                player.setClimbingSpecial(false);
                player.setGrounded(true);
                player.setY(platform.getY() - player.getHeight());
            } else if (isCollision(player, platform) && player.isClimbing() && player.getY() > platform.getY()) {
                if (player.isClimbingSpecial()) {
                    player.setYVelocity(0);
                    player.setY(platform.getY() + platform.getHeight());
                } else if (player.getGameObject().getScaleX() == 1 &&
                        player.getX() + 2 * 9 * multi > platform.getX() + platform.getWidth()) {
                    player.setYVelocity(0);
                    player.setY(platform.getY() + platform.getHeight());
                } else if (player.getGameObject().getScaleX() == -1 &&
                        player.getX() + player.getWidth() - 2 * 9 * multi < platform.getX()) {
                    player.setYVelocity(0);
                    player.setY(platform.getY() + platform.getHeight());
                } else if (player.getX() > platform.getX() &&
                        player.getX() + player.getWidth() < platform.getX() + platform.getWidth()) {
                    player.setYVelocity(0);
                    player.setY(platform.getY() + platform.getHeight());
                }
                player.setGrounded(false);
            } else if (isCollision(player, platform) && player.isClimbing() && player.getY() < platform.getY()) {
                if (player.getGameObject().getScaleX() == -1 &&
                        player.getX() + player.getWidth() - 2 * 9 * multi < platform.getX()) {
                    player.setYVelocity(0);
                    player.setY(platform.getY() - player.getHeight());
                } else if (player.getGameObject().getScaleX() == 1 &&
                        player.getX() + 2 * 9 * multi > platform.getX() + platform.getWidth()) {
                    player.setYVelocity(0);
                    player.setY(platform.getY() - player.getHeight());
                }
                player.setGrounded(false);
            }
        }
    }

    /**
     * Snap player in bounds
     */
    private boolean snapToBounds(GameObject gameObject) {
        boolean isSnapping = false;

        if (gameObject.getX() < 0) {
            gameObject.setX(0);
            isSnapping = true;
        }

        if (level == Mode.LEVEL1) {

            if (gameObject.getX() > pane.getWidth() - gameObject.getWidth()) {
                gameObject.setX(pane.getWidth() - gameObject.getWidth());
                isSnapping = true;
            }

            if (gameObject instanceof Enemy && gameObject.getX() + gameObject.getWidth() > 208 * multi) {
                gameObject.setX(208 * multi - gameObject.getWidth());
                isSnapping = true;
            }

            else if (gameObject.getY() > pane.getHeight() + gameObject.getHeight()) {
                if (gameObject instanceof Player) {
                    labels.get(0).setText("Score: 5000");
                    player.respawn(labels.get(1));
                }
                if (gameObject instanceof Enemy) {
                    gameObject.setX(pane.getWidth() + gameObject.getWidth());
                    gameObject.setY(pane.getHeight() + gameObject.getHeight());
                    pane.getChildren().remove(gameObject.getGameObject());
                }
                isSnapping = true;
            }
        }

        else if (level == Mode.LEVEL2) {
            if (gameObject.getX() < 24 * multi) {
                gameObject.setX(24 * multi);
                isSnapping = true;
            } else if (gameObject.getX() + gameObject.getWidth() > 232 * multi) {
                gameObject.setX(232 * multi - gameObject.getWidth());
                isSnapping = true;
            }
        }
        return isSnapping;
    }

    /**
     * Checks for collision between two GameObjects
     *
     * @param object1 First object
     * @param object2 Second object
     * @return true if they collide, false otherwise
     */
    private boolean isCollision(GameObject object1, GameObject object2) {
        return object1.getGameObject().getBoundsInParent().intersects(object2.getGameObject().getBoundsInParent());
    }

    /**
     * Checks if one GameObject is on top of another GameObject
     *
     * @param object1 First object
     * @param object2 Second object
     * @return true if one is on top of the second, false otherwise
     */
    private boolean onPlatform(GameObject object1, GameObject object2) {
        double upperBound = object2.getX() + object2.getWidth() - object1.getWidth() / 2;
        double lowerBound = object2.getX() - object1.getWidth() / 2;
        return object1.getX() > lowerBound && object1.getX() < upperBound;
    }

    /**
     * Adds all GameObjects to screen
     */
    private void addAll() {
        for (GameObject rope : ropes) {
            pane.getChildren().add(rope.getGameObject());
        }
        for (GameObject platform : platforms) {
            pane.getChildren().add(platform.getGameObject());
        }
        for (Collectable fruit : fruits) {
            pane.getChildren().add(fruit.getGameObject());
            pane.getChildren().add(fruit.getHitBox());
        }
        for (GameObject enemy : enemies) {
            pane.getChildren().add(enemy.getGameObject());
        }
        if (level == Mode.LEVEL1) {
            ropes.get(ropes.size() - 1).setWinner();
        }
        pane.getChildren().add(player.getGameObject());
        pane.getChildren().add(player.getOverhead());
    }

    /**
     * Creates GameObjects from stream
     *
     * @param in   input stream
     * @param type current GameObject type to create
     */
    private void platformsFromStream(InputStream in, PlatformType type) {
        InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
        Scanner scanner = new Scanner(isr);
        scanner.useDelimiter(",|\\n");
        scanner.nextLine();
        while (scanner.hasNext()) {
            int width = Integer.parseInt(scanner.next()) * multi;
            int height = Integer.parseInt(scanner.next()) * multi;
            int x = Integer.parseInt(scanner.next()) * multi;
            int y = Integer.parseInt(scanner.next()) * multi;
            scanner.nextLine();
            if (type == PlatformType.STANDARD) {
                platforms.add(new GameObject(x, y, width, height));
            } else {
                ropes.add(new Rope(x, y, width, height));
            }
        }
    }

    /**
     * Creates GameObjects, and reads text files
     */
    private void makeGameObjects() {
        platforms = new ArrayList<>();
        ropes = new ArrayList<>();

        if (level == Mode.LEVEL1) {
            try (InputStream in = Level.class.getResourceAsStream("ropes-01.txt")) {
                platformsFromStream(in, PlatformType.ROPE);
            } catch (IOException e) {
                System.out.println("No File.");
            }

            try (InputStream in = Level.class.getResourceAsStream("platforms-01.txt")) {
                platformsFromStream(in, PlatformType.STANDARD);
            } catch (IOException e) {
                System.out.println("No File.");
            }
        }

        if (level == Mode.LEVEL2) {
            try (InputStream in = Level.class.getResourceAsStream("ropes-02.txt")) {
                platformsFromStream(in, PlatformType.ROPE);
            } catch (IOException e) {
                System.out.println("No File.");
            }

            try (InputStream in = Level.class.getResourceAsStream("platforms-02.txt")) {
                platformsFromStream(in, PlatformType.STANDARD);
            } catch (IOException e) {
                System.out.println("No File.");
            }
        }
    }

    /**
     * Creates initial collectables
     */
    private void makeCollectables() {
        fruits = new ArrayList<>();
        int size = 16 * multi;
        if (level == Mode.LEVEL1) {
            fruits.add(new Collectable(Collectable.Fruit.CHERRY, 96 * multi, 81 * multi, size, size));
            fruits.add(new Collectable(Collectable.Fruit.GUAVA, 32 * multi, 81 * multi, size, size));
            fruits.add(new Collectable(Collectable.Fruit.GUAVA, 153 * multi, 98 * multi, size, size));
            fruits.add(new Collectable(Collectable.Fruit.BANANA, 136 * multi, 145 * multi, size, size));
        } else if (level == Mode.LEVEL2) {
            for (int i = 0; i < 8; i++) {
                int nextX = (35 + (24 * i)) * multi;
                fruits.add(new Collectable(Collectable.Fruit.CHERRY, nextX, 120 * multi, size, size));
            }
        }
    }

    /**
     * Creates initial enemies
     */
    private void makeEnemies() {
        enemies = new ArrayList<>();
    }

    /**
     * Creates player
     */
    private void makePlayer() {
        if (level == Mode.LEVEL1) {
            player = new Player(0, 200 * multi, 27 * multi, 16 * multi, 3);
        } else if (level == Mode.LEVEL2) {
            player = new Player(24 * multi, 200 * multi, 27 * multi, 16 * multi, 1);
        }
    }

    /**
     * Kill current player
     */
    private void killPlayer() {
        player.setX(pane.getWidth() + player.getWidth());
        player.setY(pane.getHeight() + player.getHeight());
        playerData.setPlayerData(playerNum, player.getX(), player.getY(), getScore(), player.xVelocity(),
                player.yVelocity(), player.getLives() > 0, player.isJumping(), player.isWalking(),
                player.isGrounded(), player.isClimbing(), player.isClimbingSpecial(),
                (int) player.getGameObject().getScaleX(), player.isCycling(), false);
        waitForGameEnd();   // make sure players don't disconnect too soon.
    }

    /*
     * Keeps each client connected to the server until all opponents have died, then delays 
     * a few seconds so that scores can be updated.
     * 
     */
    private void waitForGameEnd() {
        //System.out.println("Inside of waitForGameEnd");

        // wait for all players to be dead before disconnecting
        while (!allPlayersDead()) {
            try {
                Date currentTime = new Date();
                Thread.sleep(1000);
                //System.out.println("[" + currentTime + "] " + "players still alive.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // update standings now that every player has died
        for (int i = 0; i < 3; i++) {
            standings.add(i);
            standings.add(client.getPlayerData().getPlayerData(i).getScore());
        }

        // Print standings to console
        //System.out.println("----- Standings ----- ");
        for (int i = 0; i < standings.size(); i++) {
            if (i % 2 == 0) {
                //System.out.print("Player " + standings.get(i) + ": ");
            } else {
                //System.out.println(standings.get(i));
            }
        }
        //System.out.println("--------------------- ");

        // Stay connected for 5 seconds after everyone dies to make sure standings have
        // plenty of time to update
        try {
            Thread.sleep(5000);
            //System.out.println("Waiting 5 seconds.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sorts the standings array in descending order, keeps scores aligned w/ player id's. 
     * Format: [playerId, score, ...]
     * 
     * @param input     unsorted standings array w/ player id's and corresponding scores. 
     * @return         sorted standings array w/ player id's and corresponding scores.
     */
    private List<Integer> sortStandings(List<Integer> input) {

        List<Pair<Integer, Integer>> players = new ArrayList<>();
        for (int i = 0; i < input.size(); i += 2) {
            int playerId = input.get(i);
            int score = input.get(i + 1);
            players.add(new Pair<>(playerId, score));
        }

        // sort
        players.sort((p1, p2) -> p2.getValue().compareTo(p1.getValue()));

        List<Integer> sortedStandings = new ArrayList<>();
        for (Pair<Integer, Integer> player : players) {
            sortedStandings.add(player.getKey()); // id
            sortedStandings.add(player.getValue()); // score
        }

        return sortedStandings;
    }

}
