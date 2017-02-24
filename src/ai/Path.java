package ai;
import java.util.ArrayList;

/**
 * A Path is a list of Steps leading towards some target. This path is computed bya path finding algorithm (i.e A*)
 */
public class Path {
    private ArrayList steps = new ArrayList();

    /**
     * Create an empty Path
     */
    public Path() {
    }

    /**
     * Returns the length of the computed path
     * @return Length of path
     */
    public int getLength() {
        return steps.size();
    }

    /**
     * Returns the step of the path at the index
     * @param index
     * @return The step at the given index
     */
    public Step getStep(int index) {
        return (Step) steps.get(index);
    }

    /**
     * Returns the x coordinate of the step at the given index
     * @param index
     * @return x coordinate of step
     */
    public int getX(int index) {
        return getStep(index).getX();
    }

    /**
     * Returns the y coordinate of the step at the given index
     * @param index
     * @return y coordinate of step
     */
    public int getY(int index) {
        return getStep(index).getY();
    }

    /**
     * Add a step to the end of the path
     * @param x
     * @param y
     */
    public void appendStep(int x, int y) {
        steps.add(new Step(x,y));
    }

    /**
     * Add a step to the beginning of the path
     * @param x
     * @param y
     */
    public void prependStep(int x, int y) {
        steps.add(0, new Step(x, y));
    }

    /**
     * Check if a given step is already in the path
     * @param x
     * @param y
     * @return
     */
    public boolean contains(int x, int y) {
        return steps.contains(new Step(x,y));
    }
}
