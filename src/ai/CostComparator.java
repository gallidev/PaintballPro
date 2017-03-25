package ai;

import java.util.Comparator;

/**
 * The comparator used in pathfinding to check which node has a lower final cost
 * @author Sivarjuen Ravichandran
 */
public class CostComparator implements Comparator<Node> {
    @Override
    public int compare(Node a, Node b) {
            if (a.finalCost < b.finalCost) return -1;
            if (a.finalCost > b.finalCost) return 1;
        return 0;
    }



}
