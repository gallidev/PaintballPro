package ai;

import javafx.geometry.Point2D;
import players.AIPlayer;

import java.util.ArrayList;

import static players.EssentialPlayer.PLAYER_HEAD_X;
import static players.EssentialPlayer.PLAYER_HEAD_Y;

/**
 * Moves an AI Player along a path
 * @author Sivarjuen Ravichandran
 */
public class Mover {

    private static long delay = 4000;
    private boolean targetReached;
    private boolean finished = false;
    private ArrayList<Point2D> path;
    private AIPlayer ai;
    private long timer;
    private Point2D target;

    /**
     * Instatiates the mover for the given AI
     * @param ai The ai player
     */
    public Mover(AIPlayer ai){
        this.ai = ai;
        path = new ArrayList<>();
    }

    /**
     * Sets the path to follow
     * @param path
     */
    public void setPath(ArrayList<Point2D> path){
        if(path == null) {
            finished = true;
            return;
        }
        timer = System.currentTimeMillis();
        this.path = (ArrayList<Point2D>)path.clone();
    }

    /**
     * Moves the AI along the path one node at a time
     */
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

    /**
     * Moves the AI to the next node
     */
    private void move(){
        target = path.get(0);
        double deltaX = (target.getX() * 64) - (ai.getLayoutX() + PLAYER_HEAD_X) + 32;
        double deltaY = (ai.getLayoutY() + PLAYER_HEAD_Y) - (target.getY() * 64 + 32);
        if(Math.abs(deltaX) < 20 && Math.abs(deltaY) < 20) targetReached = true;
        double movementAngle = Math.atan2(deltaX, deltaY);
        ai.setMovementAngle(movementAngle);
    }

    /**
     * Update method which checks whether a new path needs to be calculated
     */
    public void tick(){
        followPath();
        if(ai.isEliminated() || timer < System.currentTimeMillis() - delay) {
            finished = true;
        }
    }

    /**
     * Returns the next node the AI moves towards
     * @return
     */
    public Point2D getTarget(){
        return this.target;
    }

    /**
     * Returns the path the AI is following
     * @return
     */
    public ArrayList<Point2D> getPath(){return this.path;}
}
