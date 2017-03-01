package ai;

import rendering.Map;

import java.util.ArrayList;
import java.util.PriorityQueue;
import rendering.Floor;
import rendering.Prop;
import rendering.Wall;

/**
 * Pathfinding uses A* search to compute an efficient path from the current position to a given target position
 */
public class Pathfinding {

    // The set of nodes that have been searched
    private ArrayList<Node> closed= new ArrayList<Node>();

    // The set of nodes to be explored
    private PriorityQueue<Node> open= new PriorityQueue<Node>(10, new CostComparator());

    // The set of all nodes across the map
    private Node[][] nodes; //null for obstacles

    private Path path;

    //Initialise Pathfinding for each AI
    public Pathfinding(Map map) {
        //Initialise nodes array from map
        nodes = new Node[48][48];
        Floor[] floors = map.getFloors();
        Prop[] props = map.getProps();
        Wall[] walls = map.getWalls();

        path = new Path();
        for(int i = 0; i < floors.length; i++){
            int x = floors[i].getX();
            int y = floors[i].getY();
            int width = floors[i].getWidth();
            int height = floors[i].getHeight();
            for(int w = x; w <= x + width; w ++){
                for(int h = y; h <= y + height; h++){
                    nodes[w][h] = new Node(w, h);
                }
            }
        }

        for(int i = 0; i < props.length; i++){
            int x = props[i].getX();
            int y = props[i].getY();
            nodes[x][y] = null;
        }

        for(int i = 0; i < walls.length; i++){
            int x = walls[i].getX();
            int y = walls[i].getY();
            int length = walls[i].getLength();
            boolean orientation = walls[i].getOrientation();
            if(orientation){
                for(int w = x; w <= x + length; w++){
                    nodes[w][y] = null;
                }
            } else {
                for(int h = y; h <= y + length; h++){
                    nodes[x][h] = null;
                }
            }
        }
    }

    private void AStar(int x, int y, int tx, int ty){
        path.clearPath();
        closed.clear();
        open.clear();
        open.add(nodes[x][y]);

        Node start = nodes[x][y];
        start.heuristicCost = euclideanCost(x, y, tx, ty);
        start.finalCost = 0;
        Node current;
        Node goal = nodes[tx][ty];
        if(goal == null) return;

        while(true){
            current = open.poll();
            if (current == null) break;
            closed.add(current);
            if (current.equals(goal)) {
                break;
            }

            //Check neighbours
            Node n;
            //Left
            if (current.x - 1 >= 0){
                n = nodes[current.x-1][current.y];
                processNode(current, n, goal);
            }

            //Right
            if (current.x + 1 < nodes.length){
                n = nodes[current.x+1][current.y];
                processNode(current, n, goal);
            }

            //Top
            if (current.y - 1 >= 0){
                n = nodes[current.x][current.y-1];
                processNode(current, n, goal);
            }

            //Bottom
            if (current.y + 1 < nodes[0].length){
                n = nodes[current.x][current.y+1];
                processNode(current, n, goal);
            }

            //Top Left
            if (current.x - 1 >= 0 && current.y - 1 >= 0){
                //check of top and left are not obstacles
                if(nodes[current.x][current.y-1] != null && nodes[current.x-1][current.y] != null) {
                    n = nodes[current.x - 1][current.y - 1];
                    processNode(current, n, goal);
                }
            }

            //Bottom Left
            if (current.x - 1 >= 0 && current.y + 1 < nodes[0].length){
                //check if bottom and left are not obstacles
                if(nodes[current.x][current.y+1] != null && nodes[current.x-1][current.y] != null) {
                    n = nodes[current.x - 1][current.y + 1];
                    processNode(current, n, goal);
                }
            }

            //Top Right
            if (current.x + 1 < nodes.length && current.y - 1 >= 0){
                //check if top and right are not obstacles
                if(nodes[current.x+1][current.y] != null && nodes[current.x][current.y-1] != null){
                    n = nodes[current.x + 1][current.y - 1];
                    processNode(current, n, goal);
                }
            }

            //Bottom Right
            if (current.x + 1 < nodes.length && current.y + 1 < nodes[0].length){
                //check if bottom and right are not obstacles
                if(nodes[current.x+1][current.y] != null && nodes[current.x][current.y+1] != null) {
                    n = nodes[current.x + 1][current.y + 1];
                    processNode(current, n, goal);
                }
            }
        }

        createPath(start, goal);
        for(int i = 0; i < nodes.length; i++){
            for(int j = 0; j < nodes[0].length; j++){
                if(nodes[i][j] != null) {
                    nodes[i][j].finalCost = 0;
                }
            }
        }
    }

    private void processNode(Node current, Node target, Node goal){
        if(closed.contains(target)) return;
        if(current == null || target == null) return;
        target.heuristicCost = euclideanCost(target.x, target.y, goal.x, goal.y);

        float tempFinalCost = target.heuristicCost + current.finalCost + 1;
        if(!closed.contains(target) && tempFinalCost < target.finalCost){
            target.finalCost = tempFinalCost;
            target.parent = current;
            if(!open.contains(target)){
                open.offer(target);
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
        Node n = goal;
        while (n.parent != null){
            path.prependNode(n.parent);
            n = n.parent;
        }

        path.appendNode(goal);
        if (!path.getNode(0).equals(start)) path = null;
    }

    public Path getPath(int x, int y, int tx, int ty){
        AStar(x, y, tx, ty);
        //for(int i = 0; i < path.getLength(); i++){
        //    System.out.println(path.getX(i) + ", " + path.getY(i));
        //}
        //System.out.println("\n");
        return path;
    }
}
