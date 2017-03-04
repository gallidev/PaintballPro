package ai;

import rendering.Floor;
import rendering.Map;
import rendering.Prop;
import rendering.Wall;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

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

    private Random random;

    //Initialise Pathfinding for each AI
    public Pathfinding(Map map) {
        //Initialise nodes array from map
        nodes = new Node[48][48];
        Floor[] floors = map.getFloors();
        Prop[] props = map.getProps();
        Wall[] walls = map.getWalls();
        random = new Random();

        path = new Path();
        for(Floor floor : floors) {
            int x = floor.getX();
            int y = floor.getY();
            int width = floor.getWidth();
            int height = floor.getHeight();
            for(int w = x; w <= x + width; w++) {
                for(int h = y; h <= y + height; h++) {
                    nodes[w][h] = new Node(w, h);
                }
            }
        }

        for(Prop prop : props) {
            int x = prop.getX();
            int y = prop.getY();
            nodes[x][y] = null;
        }

        for(Wall wall : walls) {
            int x = wall.getX();
            int y = wall.getY();
            int length = wall.getLength();
            boolean orientation = wall.getOrientation();
            if(orientation) {
                for(int w = x; w < x + length; w++) {
                    nodes[w][y] = null;
                }
            }
            else {
                for(int h = y; h < y + length; h++) {
                    nodes[x][h] = null;
                }
            }
        }
    }

    private void AStar(int x, int y, int tx, int ty){
        path.clearPath();
        closed.clear();
        open.clear();
        int origX = x;
        int origY = y;
        while(nodes[x][y] == null){
            if(x < 0 || x >= 64) x = origX;
            if(y < 0 || y >= 64) y = origY;
            int n = random.nextInt(8);
            switch (n){
                case 0: x++;
                        break;
                case 1: y++;
                        break;
                case 2: x--;
                        break;
                case 3: y--;
                        break;
                case 4: x++;
                        y++;
                        break;
                case 5: x++;
                        y--;
                        break;
                case 6: x--;
                        y++;
                        break;
                case 7: x--;
                        y--;
                        break;
            }
        }
        open.add(nodes[x][y]); //find nearest non null node

        Node start = nodes[x][y];
        start.heuristicCost = euclideanCost(x, y, tx, ty);
        start.finalCost = 0;
        Node current;
        Node goal = nodes[tx][ty];
        if(goal == null) return;

        while(true){ //use threading
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
                if(n != null) {
                    processNode(current, n, goal);
                }
            }

            //Right
            if (current.x + 1 < nodes.length){
                n = nodes[current.x+1][current.y];
                if(n != null) {
                    processNode(current, n, goal);
                }
            }

            //Top
            if (current.y - 1 >= 0){
                n = nodes[current.x][current.y-1];
                if(n != null) {
                    processNode(current, n, goal);
                }
            }

            //Bottom
            if (current.y + 1 < nodes[0].length){
                n = nodes[current.x][current.y+1];
                if(n != null) {
                    processNode(current, n, goal);
                }
            }

            //Top Left
            if (current.x - 1 >= 0 && current.y - 1 >= 0){
                //check if top and left are not obstacles
                if(nodes[current.x][current.y-1] != null && nodes[current.x-1][current.y] != null) {
                    n = nodes[current.x - 1][current.y - 1];
                    if(n != null) {
                        processNode(current, n, goal);
                    }
                }
            }

            //Bottom Left
            if (current.x - 1 >= 0 && current.y + 1 < nodes[0].length){
                //check if bottom and left are not obstacles
                if(nodes[current.x][current.y+1] != null && nodes[current.x-1][current.y] != null) {
                    n = nodes[current.x - 1][current.y + 1];
                    if(n != null) {
                        processNode(current, n, goal);
                    }
                }
            }

            //Top Right
            if (current.x + 1 < nodes.length && current.y - 1 >= 0){
                //check if top and right are not obstacles
                if(nodes[current.x+1][current.y] != null && nodes[current.x][current.y-1] != null){
                    n = nodes[current.x + 1][current.y - 1];
                    if(n != null) {
                        processNode(current, n, goal);
                    }
                }
            }

            //Bottom Right
            if (current.x + 1 < nodes.length && current.y + 1 < nodes[0].length){
                //check if bottom and right are not obstacles
                if(nodes[current.x+1][current.y] != null && nodes[current.x][current.y+1] != null) {
                    n = nodes[current.x + 1][current.y + 1];
                    if(n != null) {
                        processNode(current, n, goal);
                    }
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
    private float manhattanCost(int x, int y, int tx, int ty) {
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
        return path;
    }
}
