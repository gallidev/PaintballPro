package ai;

import rendering.Map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

/**
 * Pathfinding uses A* search to compute an efficient path from the current position to a given target position
 */
public class Pathfinding {

    // The set of nodes that have been searched
    private ArrayList<Node> closed= new ArrayList<Node>();

    // The set of nodes to be explored
    private PriorityQueue<Node> open= new PriorityQueue<Node>();

    // The set of all nodes across the map
    private Node[][] nodes;

    private Map map;

    private Path path;

    //Initialise Pathfinding for each AI
    public Pathfinding(Map map) {
        this.map = map;
        //Initialise nodes array from map
    }

    private void processObstacles(){
        //add all obstacle nodes to the closed list
    }

    private void AStar(int x, int y, int tx, int ty){
        closed.clear();
        open.clear();
        processObstacles();
        open.add(nodes[x][y]);

        Node start = nodes[x][y];
        start.heuristicCost = euclideanCost(x, y, tx, ty);
        Node current;
        Node goal = nodes[tx][ty];

        while(true){
            current = open.poll();
            if (current == null) break;
            closed.add(current);

            if (current.equals(goal)) return;

            //Check neighbours
            Node n;
            //Left
            if (current.x - 1 >= 0){
                n = nodes[x-1][y];
                processNode(current, n, goal);
            }

            //Right
            if (current.x + 1 < nodes.length){
                n = nodes[x+1][y];
                processNode(current, n, goal);
            }

            //Top
            if (current.y - 1 >= 0){
                n = nodes[x][y-1];
                processNode(current, n, goal);
            }

            //Bottom
            if (current.y + 1 < nodes[0].length){
                n = nodes[x][y+1];
                processNode(current, n, goal);
            }

            //Top Left
            if (current.x - 1 >= 0 && current.y - 1 >= 0){
                n = nodes[x-1][y-1];
                processNode(current, n, goal);
            }

            //Bottom Left
            if (current.x - 1 >= 0 && current.y + 1 < nodes[0].length){
                n = nodes[x-1][y+1];
                processNode(current, n, goal);
            }

            //Top Right
            if (current.x + 1 < nodes.length && current.y - 1 >= 0){
                n = nodes[x+1][y-1];
                processNode(current, n, goal);
            }

            //Bottom Right
            if (current.x + 1 < nodes.length && current.y + 1 < nodes[0].length){
                n = nodes[x+1][y+1];
                processNode(current, n, goal);
            }
        }

        createPath(start, goal);
        for(int i = 0; i < nodes.length; i++){
            for(int j = 0; j < nodes[0].length; j++){
                nodes[i][j].finalCost = 0;
            }
        }
    }


    private void processNode(Node current, Node target, Node goal){
        if(closed.contains(target)) return;
        target.heuristicCost = euclideanCost(target.x, target.y, goal.x, goal.y);

        float tempFinalCost = target.heuristicCost + current.finalCost + 1;

        if(!open.contains(target) || tempFinalCost < target.finalCost){
            target.finalCost = tempFinalCost;
            target.parent = current;
            if(!open.contains(target)){
                open.add(target);
            }
        }

    }

    //Sqrt is expensive, but this takes into account diagonal movements
    private float euclideanCost(int x, int y, int tx, int ty) {
        float dx = tx - x;
        float dy = ty - y;

        float result = (float) (Math.sqrt((dx*dx)+(dy*dy)));

        return result;
    }

    //Cheaper, but not efficient for diagonal movements
    private float manhattanDistance(int x, int y, int tx, int ty) {
        float dx = Math.abs(tx - x);
        float dy = Math.abs(ty - y);

        return dx + dy;
    }

    private void createPath(Node start, Node goal){
        Path path = new Path();
        Node n = goal;

        while (n.parent != null){
            path.prependNode(n);
            n = n.parent;
        }

        if (!path.getNode(0).equals(start)) path = null;
        this.path = path;
    }

    public Path getPath(int x, int y, int tx, int ty){
        AStar(x, y, tx, ty);
        return path;
    }
}
