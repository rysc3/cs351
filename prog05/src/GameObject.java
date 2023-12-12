import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

  /**
 * @author Luka Bazar
 * @author Ryan Scherbarth
 * @author Roget Davis 
 * @author Samuel Dauk
 * <p>
 * Standard GameObject (has only x,y and shape to be collided)
 */
public class GameObject {
    private final Rectangle gameObject = new Rectangle();

    /**
     * Creates standard game object to be used in the game
     *
     * @param x      initial x
     * @param y      initial y
     * @param width  object width
     * @param height object height
     */
    public GameObject(int x, int y, int width, int height) {
        gameObject.setFill(Color.TRANSPARENT);
        gameObject.setTranslateX(x);
        gameObject.setTranslateY(y);
        gameObject.setWidth(width);
        gameObject.setHeight(height);
    }

    /**
     * Get Node that makes up the GameObject
     *
     * @return Node
     */
    public Node getGameObject() {
        return this.gameObject;
    }

    /**
     * Get current x coordinate
     *
     * @return double x
     */
    public double getX() {
        return gameObject.getTranslateX();
    }

    /**
     * Set x coordinate
     *
     * @param x new x coordinate
     */
    public void setX(double x) {
        gameObject.setTranslateX(x);
    }

    /**
     * Set y coordinate
     *
     * @param y new y coordinate
     */
    public void setY(double y) {
        gameObject.setTranslateY(y);
    }

    /**
     * Get current y coordinate
     *
     * @return double y
     */
    public double getY() {
        return gameObject.getTranslateY();
    }

    /**
     * Get width of GameObject
     *
     * @return double width
     */
    public double getWidth() {
        return gameObject.getWidth();
    }

    /**
     * Get height of GameObject
     *
     * @return double height
     */
    public double getHeight() {
        return gameObject.getHeight();
    }

}
