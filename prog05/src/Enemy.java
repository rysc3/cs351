import java.util.Random;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

 /**
 * @author Luka Bazar
 * @author Ryan Scherbarth
 * @author Roget Davis 
 * @author Samuel Dauk
 * <p>
 * Enemy GameObjects (chompers)
 */
public class Enemy extends GameObject {

    /**
     * Enemy sprites
     */
    private enum EnemySprites {
        BLUE1("enemies/blue-01.png"), BLUE2("enemies/blue-02.png");
        private final ImageView img;

        EnemySprites(String imgStr) {
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

    private int xDir = new Random(System.currentTimeMillis()).nextInt(0, 2);
    private final ImageView enemy = new ImageView();
    private int cycle = 0;
    private int rotate = 0;
    private double xVelocity = 0;
    private double yVelocity = 0;

    /**
     * Enemy GameObject
     *
     * @param x      x coordinate
     * @param y      y coordinate
     * @param width  enemy width
     * @param height enemy height
     */
    public Enemy(int x, int y, int width, int height) {
        super(x, y, width, height);
        enemy.setTranslateX(x);
        enemy.setTranslateY(y);
        enemy.setPreserveRatio(true);
        enemy.setFitWidth(width);
    }

    /**
     * Set the number of the current cycle (for sprite animation)
     *
     * @param cycle cycle number
     */
    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    /**
     * Changes the sprite based on color and current cycle
     */
    public void changeSprite() {
        if (xVelocity < 0) {
            enemy.setScaleX(-1);
        }
        else if (xVelocity > 0) {
            enemy.setScaleX(1);
        }
        if (cycle == 0) {
            enemy.setImage(EnemySprites.BLUE2.getImage());
        }
        else {
            enemy.setImage(EnemySprites.BLUE1.getImage());
        }
    }

    /**
     * Get current yVelocity
     *
     * @return double yVelocity
     */
    public double yVelocity() {
        return yVelocity;
    }


    /**
     * Get current xVelocity
     *
     * @return double xVelocity
     */
    public double xVelocity() {
        return xVelocity;
    }

    public void setXVelocity(double xVelocity) {
        if (enemy.getRotate() != 0) {
            this.xVelocity = 0;
        }
        else if (xDir == 0) {
            this.xVelocity = xVelocity;
        }
        else {
            this.xVelocity = -xVelocity;
        }
    }

    /**
     * Set the velocity in y direction
     *
     * @param yVelocity double
     */
    public void setYVelocity(double yVelocity) {
        if (enemy.getRotate() != 0) {
        }
        else {
            this.yVelocity = yVelocity;
        }
    }

    /**
     * Switch the direction in x
     */
    public void switchXDir() {
        if (this.xDir == 0) {
            this.xDir = 1;
        }
        else {
            this.xDir = 0;
        }
    }

    /**
     * Changes the rotation of the sprite
     */
    public void changeRotate() {
        if (rotate == 0) {
            if (xVelocity > 0) {
                rotate = 90;
            }
            else if (xVelocity < 0) {
                rotate = -90;
            }
            enemy.setRotate(rotate);
        }
        else {
            rotate = 0;
            enemy.setRotate(rotate);
        }
    }

    /**
     * Set new x coordinate
     *
     * @param x new x coordinate
     */
    @Override
    public void setX(double x) {
        if (enemy.getRotate() != 0) {
            enemy.setTranslateX(enemy.getTranslateX());
        }
        else {
            enemy.setTranslateX(x);
        }
    }

    /**
     * Set new y coordinate
     *
     * @param y new y coordinate
     */
    @Override
    public void setY(double y) {
        enemy.setTranslateY(y);
    }

    /**
     * Get current x coordinate
     *
     * @return double x
     */
    @Override
    public double getX() {
        return enemy.getTranslateX();
    }

    /**
     * Get current y coordinate
     *
     * @return double y
     */
    @Override
    public double getY() {
        return enemy.getTranslateY();
    }

    /**
     * Get the current GameObject of enemy
     *
     * @return Node of the GameObject
     */
    @Override
    public Node getGameObject() {
        return enemy;
    }

}
