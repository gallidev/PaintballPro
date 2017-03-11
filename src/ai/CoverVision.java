package ai;

import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import rendering.Floor;
import rendering.Map;
import rendering.Prop;
import rendering.Wall;

import java.util.ArrayList;
import java.util.Random;

public class CoverVision {
    private Node[][] nodes; //null for obstacles
    private PropNode[] propNodes; //null for anything that is not a prop
    private Map map;

    public CoverVision(Map map) {
        //Initialise nodes array from map
        this.map = map;
        nodes = new Node[48][48];
        Floor[] floors = map.getFloors();
        Prop[] props = map.getProps();
        Wall[] walls = map.getWalls();

        for(Floor floor : floors) {
            int x = floor.getX();
            int y = floor.getY();
            int width = floor.getWidth();
            int height = floor.getHeight();
            for(int w = x; w < x + width; w++) {
                for(int h = y; h < y + height; h++) {
                    nodes[w][h] = new Node(w, h);
                }
            }
        }
        propNodes = new PropNode[props.length];
        for (int i = 0; i < props.length; i++){
            int x = props[i].getX();
            int y = props[i].getY();
            nodes[x][y] = null;
            propNodes[i] = new PropNode(x, y);
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

    public boolean inSight(int x, int y, int tx, int ty){
        Line line = new Line(x*64, y*64, tx*64, ty*64);
        ArrayList<Rectangle> propsWalls = map.getRecProps();
        propsWalls.addAll(map.getRecWalls());
        for(Rectangle propWall : propsWalls){
            if(Shape.intersect(line, propWall).getBoundsInParent().isEmpty() == false) {
                return false;
            }
        }
        return true;
    }

    public Node[][] getNodeGrid(){
        return this.nodes;
    }

    public PropNode[] getPropNodes() {return this.propNodes;}
}
