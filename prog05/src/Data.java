import java.io.Serializable;

/**
 * @author Luka Bazar
 * @author Ryan Scherbarth
 * @author Roget Davis 
 * @author Samuel Dauk
 * <p>
 * Data structure to contain player data
 */
public class Data implements Serializable {
    private double x;
    private double y;
    private int score;
    private double xVelocity;
    private double yVelocity;
    private boolean isAlive;
    private boolean isJumping;
    private boolean isWalking;
    private boolean isGrounded;
    private boolean isClimbing;
    private boolean isClimbingSpecial;
    private boolean isCycle;
    private int direction;

    private boolean isReady;
    
    /**
     * Constructor for Data class
     * @param x player x coord
     * @param y player y coord
     * @param isAlive player alive status
     */
    public Data(double x, double y, int score, boolean isAlive) {
        this.x = x;
        this.y = y;
        this.score = score;
        this.xVelocity = 1;
        this.yVelocity = 1;
        this.isAlive = isAlive;
        isJumping = false;
        isWalking = false;
        isGrounded = true;
        isClimbing = false;
        isClimbingSpecial = false;
        isCycle = false;
        direction = 1;
        isReady = false;
    }

    /**
     * Get direction
     *
     * @return 1 for left, -1 for right
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Set direction
     *
     * @param direction (-1 or 1)
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Is playing cycling
     *
     * @return true if it is, false otherwise
     */
    public boolean isCycle() {
        return isCycle;
    }

    /**
     * Set if cycling
     *
     * @param cycle true if it is false if not
     */
    public void setCycle(boolean cycle) {
        isCycle = cycle;
    }

    /**
     * Get x coord
     * @return x val
     */
    public double getX() {
        return x;
    }

    /**
     * Set x coord
     * @param val new x val
     */
    public void setX(double val) {
        x = val;
    }

    /**
     * Get y coord
     * @return y val
     */
    public double getY() {
        return y;
    }

    /**
     * Set y coord
     * @param val new y val
     */
    public void setY(double val) {
        y = val;
    }

    /**
     * Set the xVelocity
     *
     * @param xVelocity double velocity in x direction
     */
    public void setXVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    /**
     * Set yVelocity
     *
     * @param yVelocity double velocity in y direction
     */
    public void setYVelocity(double yVelocity) { this.yVelocity = yVelocity; }

    /**
     * Get xVelocity
     *
     * @return velocity in x direction
     */
    public double xVelocity() {
        return this.xVelocity;
    }

    /**
     * Get yVelocity
     *
     * @return velocity in y direction
     */
    public double yVelocity() {
        return this.yVelocity;
    }
    /**
     * Get alive status
     * @return alive status
     */
    public boolean getIsAlive() {
        return isAlive;
    }

    /**
     * Set alive status
     * @param val new alive status
     */
    public void setIsAlive(boolean val) {
        isAlive = val;
    }

    /**
     * Set score
     *
     * @param val current score
     */
    public void setScore(int val) { score = val; }

    /**
     * Placement points upon game end
     */
    public void addScore(int val) { score += val; }

    /**
     * Get game score
     *
     * @return game score
     */
    public int getScore() { return score; }

    /**
     * Set if player is jumping
     *
     * @param jumping true if jumping, false otherwise
     */
    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }

    /**
     * Set if player is climbing
     *
     * @param climbing true if climbing, false otherwise
     */
    public void setClimbing(boolean climbing) {
        isClimbing = climbing;
    }

    /**
     * Set if player is grounded
     *
     * @param grounded true if grounded, false otherwise
     */
    public void setGrounded(boolean grounded) {
        isGrounded = grounded;
    }

    /**
     * Set if player is walking
     *
     * @param walking true if walking, false otherwise
     */
    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    /**
     * Set if player is climbing (special)
     *
     * @param climbingSpecial true if special climb, false otherwise
     */
    public void setClimbingSpecial(boolean climbingSpecial) {
        isClimbingSpecial = climbingSpecial;
    }

    /**
     * Get is player climbing
     *
     * @return true if climbing, false otherwise
     */
    public boolean isClimbing() {
        return isClimbing;
    }

    /**
     * Get is player climbing (special)
     *
     * @return true if special climb, false otherwise
     */
    public boolean isClimbingSpecial() {
        return isClimbingSpecial;
    }

    /**
     * Get if player is grounded
     *
     * @return true if grounded, false otherwise
     */
    public boolean isGrounded() {
        return isGrounded;
    }

    /**
     * Get if player is jumping
     *
     * @return true if jumping, false otherwise
     */
    public boolean isJumping() {
        return isJumping;
    }

    /**
     * Get if player is walking
     *
     * @return true if walking, false otherwise
     */
    public boolean isWalking() {
        return isWalking;
    }

    /**
     * Get is ready
     *
     * @return true if player is ready, false otherwise
     */
    public boolean isReady() { return isReady; }

    /**
     * Set is ready
     *
     * @param ready true for ready, false otherwise
     */
    public void setReady( boolean ready) { isReady = ready; }

}
