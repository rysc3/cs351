import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

/**
 * @author Luka Bazar
 * @author Ryan Scherbarth
 * @author Roget Davis 
 * @author Samuel Dauk
 * <p>
 * Collectable objects (fruit)
 */
public class Collectable extends GameObject {

    /**
     * Enum containing all Fruit sprites
     */
    public enum Fruit {
        CHERRY("fruits/cherry.png"), BANANA("fruits/banana.png"), GUAVA("fruits/guava.png");

        private final ImageView img;

        Fruit(String imgStr) {
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

    private final ImageView collectable = new ImageView();
    private final Rectangle hitBox = new Rectangle();
    private boolean isFalling = false;
    private final double spawnX;
    private final double spawnY;

    /**
     * Collectable GameObject to be used in the game
     *
     * @param type   Fruit type
     * @param x      initial x coordinate
     * @param y      initial y coordinate
     * @param width  width of collectable
     * @param height height of collectable
     */
    public Collectable(Fruit type, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.spawnX = x;
        this.spawnY = y;
        this.collectable.setPreserveRatio(true);
        this.collectable.setTranslateX(x);
        this.collectable.setTranslateY(y);
        this.hitBox.setTranslateX(x + width / 2.0);
        this.hitBox.setTranslateY(y + height / 2.0);
        this.collectable.setFitWidth(width);
        this.collectable.setFitHeight(height);
        createCollectable(type);
    }

    /**
     * Create collectable and set corresponding image
     *
     * @param type Fruit type to set
     */
    private void createCollectable(Fruit type) {
        if (type == Fruit.BANANA) {
            collectable.setImage(Fruit.BANANA.getImage());
        }
        else if (type == Fruit.CHERRY) {
            collectable.setImage(Fruit.CHERRY.getImage());
        }
        else if (type == Fruit.GUAVA) {
            collectable.setImage(Fruit.GUAVA.getImage());
        }
    }

    /**
     * Is collectible falling
     *
     * @return true if it is currently falling, false otherwise
     */
    public boolean isFalling() {
        return isFalling;
    }

    /**
     * Set the collectible to the falling state
     */
    public void setFalling(boolean bool) {
        isFalling = bool;
    }

    public void respawn() {
        collectable.setTranslateX(spawnX);
        collectable.setTranslateY(spawnY);
        hitBox.setTranslateX(spawnX + collectable.getFitWidth() / 2.0);
        hitBox.setTranslateY(spawnY + collectable.getFitHeight() / 2.0);
    }

    /**
     * Set new x coordinate
     *
     * @param x new x coordinate
     */
    @Override
    public void setX(double x) {
        collectable.setTranslateX(x);
        hitBox.setTranslateX(x + collectable.getFitWidth() / 2.0);
    }

    /**
     * Set new y coordinate
     *
     * @param y new y coordinate
     */
    @Override
    public void setY(double y) {
        collectable.setTranslateY(y);
        hitBox.setTranslateY(y + collectable.getFitWidth() / 2.0);
    }

    /**
     * Get current x coordinate
     *
     * @return double x
     */
    @Override
    public double getX() {
        return collectable.getTranslateX();
    }

    /**
     * Get current y coordinate
     *
     * @return double y
     */
    @Override
    public double getY() {
        return collectable.getTranslateY();
    }

    /**
     * Get Node of the Collectable GameObject
     *
     * @return Node of GameObject
     */
    @Override
    public Node getGameObject() {
        return this.collectable;
    }

    /**
     * Get HitBox of the Node (smaller than the ImageView)
     *
     * @return Node representing the hitbox
     */
    public Node getHitBox() {
        return this.hitBox;
    }
}
