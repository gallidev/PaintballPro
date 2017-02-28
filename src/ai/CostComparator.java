package ai;

import java.util.Comparator;

public class CostComparator implements Comparator<Node> {
    @Override
    public int compare(Node a, Node b) {
            if (a.finalCost == b.finalCost) return 0;
            if (a.finalCost < b.finalCost) return -1;
            if (a.finalCost > b.finalCost) return 1;
        return 0;
    }



}
