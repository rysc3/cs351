import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * @author Luka Bazar
 * @author Ryan Scherbarth
 * @author Roget Davis 
 * @author Samuel Dauk
 * <p>
 * Player character (Donkey Kong Jr.)
 */
public class Player extends GameObject {

    /**
     * Enum containing all Donkey Kong Jr. sprites
     */
    private enum Sprites {
        WALK1("sprites/walk-01.png"), WALK2("sprites/walk-02.png"),
        WALK3("sprites/walk-03.png"), CLIMB1("sprites/climb-01.png"),
        CLIMB2("sprites/climb-02.png"), CLIMB3("sprites/climb-03.png"),
        JUMP("sprites/jump-01.png"), FALL("sprites/fall-01.png"),
        OVERHEADA("sprites/overhead-01.png"), OVERHEADB("sprites/overhead-02.png");

        private final ImageView img;

        Sprites(String imgStr) {
            ImageView tempImg = null;
            try {
                tempImg = new ImageView(imgStr);
            } catch (Exception e) {
                System.out.println("File not found.");
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

    private boolean isCycle = false;
    private int cycle = 0;
    private int fallCount = 0;
    private int lives;
    private double xVelocity = 0;
    private double yVelocity = 0;
    private int score = 0;
    private final double spawnX;
    private final double spawnY;
    private boolean isJumping = false;
    private boolean isWalking = false;
    private boolean isGrounded = true;
    private boolean isClimbing = false;
    private boolean isFalling = false;
    private boolean isClimbingSpecial = false;
    private final ImageView dk = new ImageView();
    private final ImageView overhead = new ImageView();
    private boolean hasOverA = true;

    private boolean isReady = false;

    /**
     * Player GameObject (Donkey Kong Jr.)
     *
     * @param x      initial x coordinate
     * @param y      initial y coordinate
     * @param width  width of object
     * @param height height of object
     */
    public Player(int x, int y, int width, int height, int lives) {
        super(x, y, width, height);
        this.lives = lives;
        spawnX = x;
        spawnY = y;
        dk.setTranslateX(x);
        dk.setTranslateY(y);
        dk.setPreserveRatio(true);
        dk.setFitHeight(height);
        overhead.setTranslateX(x + 10);
        overhead.setTranslateY(y - height);
        overhead.setPreserveRatio(true);
        overhead.setFitHeight(height * 0.75);
        overhead.setFitWidth(height * 0.75);
        setOverToA();
    }

    /**
     * Returns Node representing GameObject
     *
     * @return Node
     */
    @Override
    public Node getGameObject() {
        return dk;
    }

    /**
     * Returns overhead sprite object
     *
     * @return Node
     */
    public Node getOverhead() { return overhead; }

    /**
     * Get current x coordinate
     *
     * @return double x
     */
    @Override
    public double getX() {
        return dk.getTranslateX();
    }

    /**
     * Get current y coordinate
     *
     * @return double y
     */
    @Override
    public double getY() {
        return dk.getTranslateY();
    }

    /**
     * Set new x coordinate
     *
     * @param x new x coordinate
     */
    @Override
    public void setX(double x) {
        dk.setTranslateX(x);
        if(isClimbing && !isClimbingSpecial && dk.getScaleX() == -1) {
            overhead.setTranslateX(x + 0.375 * getHeight() + getWidth()/2);
        }
        else {
            overhead.setTranslateX(x + 0.375 * getHeight());
        }
    }

    public void setHasOverA(boolean b) { hasOverA = b; }

    public boolean getHasOverA() { return hasOverA; }

    /**
     * Set new y coordinate
     *
     * @param y new y coordinate
     */
    @Override
    public void setY(double y) {
        dk.setTranslateY(y);
        overhead.setTranslateY(y - getHeight());
    }

    /**
     * Set to overhead image A
     */
    public void setOverToA(){ overhead.setImage(Sprites.OVERHEADA.getImage()); }

    /**
     * Set to overhead image B
     */
    public void setOverToB(){ overhead.setImage(Sprites.OVERHEADB.getImage()); }
    
    /**
     * Set velocity in x direction
     *
     * @param xVelocity new xVelocity
     */
    public void setXVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    /**
     * Set velocity in y direction
     *
     * @param yVelocity new yVelocity
     */
    public void setYVelocity(double yVelocity) { this.yVelocity = yVelocity; }

    /**
     * Get current xVelocity
     *
     * @return double xVelocity
     */
    public double xVelocity() {
        return this.xVelocity;
    }

    /**
     * Get current yVelocity
     *
     * @return double yVelocity
     */
    public double yVelocity() {
        return this.yVelocity;
    }

    /**
     * Is player jumping
     *
     * @return true if player is jumping, false otherwise
     */
    public boolean isJumping() {
        return isJumping;
    }

    /**
     * Set if character is jumping
     *
     * @param jumping boolean to set
     */
    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }

    /**
     * Is player walking
     *
     * @return true if player is walking, false otherwise
     */
    public boolean isWalking() {
        return isWalking;
    }

    /**
     * Set if player is walking
     *
     * @param walking boolean to set
     */
    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    /**
     * Is player climbing
     *
     * @return true if player is climbing, false otherwise
     */
    public boolean isClimbing() {
        return isClimbing;
    }

    /**
     * Set if player is climbing
     *
     * @param climbing boolean to set
     */
    public void setClimbing(boolean climbing) {
        isClimbing = climbing;
    }

    /**
     * Is player in special two-handed climb state
     *
     * @return true if character is in the two-handed climb, false otherwise
     */
    public boolean isClimbingSpecial() {
        return isClimbingSpecial;
    }

    /**
     * Set if player is in special two-handed climb state
     *
     * @param climbingSpecial boolean to set
     */
    public void setClimbingSpecial(boolean climbingSpecial) {
        isClimbingSpecial = climbingSpecial;
    }

    /**
     * Is player grounded
     *
     * @return true if player is grounded, false otherwise
     */
    public boolean isGrounded() {
        return isGrounded;
    }

    /**
     * Set if player is grounded
     *
     * @param grounded boolean to set
     */
    public void setGrounded(boolean grounded) {
        isGrounded = grounded;
    }

    /**
     * Is player falling
     *
     * @return true if player is falling, false otherwise
     */
    public boolean isFalling() {
        return isFalling;
    }

    /**
     * Total frames fall count
     *
     * @return number of falling
     */
    public int getFallCount() {
        return fallCount;
    }

    private void resetFall() {
        isFalling = false;
        fallCount = 0;
    }

    /**
     * Changes the current Image for the
     * ImageView representing the player character
     */
    public void changeSprite() {
        if (isClimbing()) {
            resetFall();
            setClimb();
        }
        else if (isJumping()) {
            resetFall();
            setJump();
        }
        else if (isWalking()) {
            resetFall();
            setWalk();
        }
        else if (isGrounded()) {
            resetFall();
            setDefault();
        }
        else {
            isFalling = true;
            fallCount++;
            setFall();
        }
    }

    /**
     * Respawn the player on death
     *
     * @param label Label to update with lives counter
     */
    public void respawn(Label label) {
        lives--;
        if (lives == 0) {
            label.setText("Lives: 0");
        }
        else if (lives >= 1) {
            dk.setTranslateX(spawnX);
            dk.setTranslateY(spawnY);
            label.setText("Lives: " + lives);
        }
    }

    /**
     * Get current number of lives the Player has
     *
     * @return number of lives left
     */
    public int getLives() {
        return lives;
    }

    /**
     * Set current image to a Walk sprite
     */
    private void setWalk() {
        if (cycle == 0) {
            dk.setImage(Sprites.WALK2.getImage());
        }
        else if (cycle == 1) {
            dk.setImage(Sprites.WALK3.getImage());
        }
        else if (cycle == 2) {
            dk.setImage(Sprites.WALK2.getImage());
        }
        else {
            dk.setImage(Sprites.WALK1.getImage());
        }
    }

    /**
     * Set current image to Jump sprite
     */
    private void setJump() {
        dk.setImage(Sprites.JUMP.getImage());
    }

    /**
     * Set current image to Climb sprite
     */
    private void setClimb() {
        if (isClimbingSpecial) {
            if (cycle % 2 == 0 && isCycle) {
                dk.setScaleX(1);
            }
            else {
                dk.setScaleX(-1);
            }
            dk.setImage(Sprites.CLIMB3.getImage());
        }
        else if (cycle % 2 == 0 && isCycle) {
            dk.setImage(Sprites.CLIMB1.getImage());
        }
        else {
            dk.setImage(Sprites.CLIMB2.getImage());
        }
    }

    /**
     * Set current image to fall sprite
     */
    private void setFall() {
        dk.setImage(Sprites.FALL.getImage());
    }

    /**
     * Set current image to default sprite
     */
    private void setDefault() {
        dk.setImage(Sprites.WALK2.getImage());
    }

    /**
     * Set current animation cycle (current sprite to change to)
     *
     * @param cycle current cycle number
     */
    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    /**
     * Get current cycle number
     *
     * @return int current cycle
     */
    public int getCycle() {
        return this.cycle;
    }

    /**
     * Set if the current state of the player should cycle
     * through sprites
     *
     * @param bool true if cycling, false otherwise
     */
    public void isCycle(boolean bool) {
        this.isCycle = bool;
    }

    /**
     * Is the player cycling
     *
     * @return true if yes, false otherwise
     */
    public boolean isCycling() {
        return isCycle;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() { return this.score; }
}
