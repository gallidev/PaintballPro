package ai;
import java.util.ArrayList;

/**
 * A Path is a list of Nodes leading towards some target. This path is computed bya path finding algorithm (i.e A*)
 */
public class Path {
    private ArrayList<Node> Nodes;

    /**
     * Create an empty Path
     */
    public Path() {
        Nodes = new ArrayList<Node>();
    }

    /**
     * Returns the length of the computed path
     * @return Length of path
     */
    public int getLength() {
        return Nodes.size();
    }

    /**
     * Returns the Node of the path at the index
     * @param index
     * @return The Node at the given index
     */
    public Node getNode(int index) {
        return Nodes.get(index);
    }

    /**
     * Returns the x coordinate of the Node at the given index
     * @param index
     * @return x coordinate of Node
     */
    public int getX(int index) {
        return getNode(index).x;
    }

    /**
     * Returns the y coordinate of the Node at the given index
     * @param index
     * @return y coordinate of Node
     */
    public int getY(int index) {
        return getNode(index).y;
    }

    /**
     * Add a Node to the end of the path
     */
    public void appendNode(Node n) {
        Nodes.add(n);
    }

    /**
     * Add a Node to the beginning of the path
     */
    public void prependNode(Node n) {
        Nodes.add(0, n);
    }

    /**
     * Check if a given Node is already in the path
     * @return
     */
    public boolean contains(Node n) {
        return Nodes.contains(n);
    }
}
