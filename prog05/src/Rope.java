 /**
 * @author Luka Bazar
 * @author Ryan Scherbarth
 * @author Roget Davis 
 * @author Samuel Dauk
 * <p>
 * Level geometry GameObject (can be climbed)=
 */
public class Rope extends GameObject {
    private boolean isWinner = false;

    /**
     * Rope GameObject
     *
     * @param x      x coordinate
     * @param y      y coordinate
     * @param width  rope width
     * @param height rope height
     */
    public Rope(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * Is rope a winning state
     *
     * @return true if it is a winning state, false if not
     */
    public boolean isWinner() {
        return isWinner;
    }

    /**
     * Set rope as a winning state
     */
    public void setWinner() {
        this.isWinner = true;
    }
}
