package ai;

import javafx.geometry.Point2D;
import players.AIPlayer;

import java.util.ArrayList;

import static players.EssentialPlayer.PLAYER_HEAD_X;
import static players.EssentialPlayer.PLAYER_HEAD_Y;

/**
 * Moves an AI Player along a path
 */
public class Mover {

    private static long delay = 4000;
    private boolean targetReached;
    private boolean finished = false;
    private ArrayList<Point2D> path;
    private AIPlayer ai;
    private long timer;
    private Point2D target;

    public Mover(AIPlayer ai){
        this.ai = ai;
        path = new ArrayList<>();
    }

    public void setPath(ArrayList<Point2D> path){
        if(path == null) {
            finished = true;
            return;
        }
        timer = System.currentTimeMillis();
        this.path = (ArrayList<Point2D>)path.clone();
    }

    private void followPath(){
        finished = false;

        if(path == null) {
            finished = true;
            return;
        }
        if(path.size() == 0) {
            finished = true;
            return;
        }

        targetReached = false;
        move();
        if(targetReached){
            path.remove(0);
        }
    }

    private void move(){
        target = path.get(0);
        double deltaX = (target.getX() * 64) - (ai.getLayoutX() + PLAYER_HEAD_X) + 32;
        double deltaY = (ai.getLayoutY() + PLAYER_HEAD_Y) - (target.getY() * 64 + 32);
        if(Math.abs(deltaX) < 20 && Math.abs(deltaY) < 20) targetReached = true;
        double movementAngle = Math.atan2(deltaX, deltaY);
        ai.setMovementAngle(movementAngle);
    }

    public void tick(){
        followPath();
        if(ai.isEliminated() || timer < System.currentTimeMillis() - delay) {
            finished = true;
        }
    }

    public Point2D getTarget(){
        return this.target;
    }

    public ArrayList<Point2D> getPath(){return this.path;}
}
