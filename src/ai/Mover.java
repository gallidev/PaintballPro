package ai;

import javafx.geometry.Point2D;
import players.AIPlayer;

import java.util.ArrayList;

import static players.GeneralPlayer.playerHeadX;
import static players.GeneralPlayer.playerHeadY;

/**
 * Moves an AI Player along a path
 */
public class Mover {

    private boolean targetReached;
    private boolean finished = false;
    private ArrayList<Point2D> path;
    private AIPlayer ai;
    private long timer;
    private static long delay = 4000;
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
        double deltaX = (target.getX() * 64) - (ai.getLayoutX() + playerHeadX) + 32;
        double deltaY = (ai.getLayoutY() + playerHeadY) - (target.getY() * 64 + 32);
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

    public boolean isFinished(){
        return this.finished;
    }

    public Point2D getTarget(){
        return this.target;
    }
}
