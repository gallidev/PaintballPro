package ai;


/**
 * A node stores a set coordinate, which can be linked with other nodes to form a path
 */
public class Node {
    public int x;
    public int y;
    public Node parent;
    public float heuristicCost = 0;
    public float finalCost = 99999;

    /**
     * Create a node with the given coordinates
     * @param x The x coordinate of the node
     * @param y The y coordinate of the node
     */
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * HashCode is used to object comparison
     * @return x*y
     */
    public int hashCode() {
        return x*y;
    }

    /**
     * Compares an object to the node. If the other object is a node, the two nodes are equal of they represent the same coordinates
     * @param other
     * @return True if the object is equal to the node, false otherwise
     */
    public boolean equals(Object other) {
        if (other instanceof Node) {
            Node o = (Node) other;

            return (o.x == x) && (o.y == y);
        }

        return false;
    }

    @Override
    public String toString(){
        return "(" + x + ", " + y + ", Cost: " + finalCost + ")";
    }
}
