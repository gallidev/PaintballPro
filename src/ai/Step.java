package ai;

/**
 * A step stores a set coordinate, which can be linked with other steps to form a path
 */
public class Step {
    private int x;
    private int y;

    /**
     * Create a step with the given coordinates
     * @param x The x coordinate of the step
     * @param y The y coordinate of the step
     */
    public Step(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x coordinate of the step
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of the step
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     * HashCode is used to object comparison
     * @return x*y
     */
    public int hashCode() {
        return x*y;
    }

    /**
     * Compares an object to the step. If the other object is a step, the two steps are equal of they represent the same coordinates
     * @param other
     * @return True if the object is equal to the step, false otherwise
     */
    public boolean equals(Object other) {
        if (other instanceof Step) {
            Step o = (Step) other;

            return (o.x == x) && (o.y == y);
        }

        return false;
    }
}
