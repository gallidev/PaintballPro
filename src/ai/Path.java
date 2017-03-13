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

    public void clearPath(){
        Nodes.clear();
    }

    public void removeFirst() { Nodes.remove(0);}

    @Override
    public String toString(){
        return Nodes.toString();
    }

    /**
     * HashCode is used to object comparison
     * @return x*y
     */
    public int hashCode() {
        int sum = 0;
        for(int i = 0; i < Nodes.size(); i++){
            sum += (Nodes.get(i).x * Nodes.get(i).y);
        }
        return sum;
    }

    /**
     * Compares an object to the node. If the other object is a node, the two nodes are equal of they represent the same coordinates
     * @param other
     * @return True if the object is equal to the node, false otherwise
     */
    public boolean equals(Object other) {
        if (other instanceof Path) {
            Path o = (Path) other;
            if(Nodes.size() != o.getLength()) return false;
            for(int i = 0; i < Nodes.size(); i++){
                if(!Nodes.get(i).equals(o.getNode(i))) return false;
            }
            return true;
        }

        return false;
    }

}
