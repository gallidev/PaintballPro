package ai;

import players.AIPlayer;
/**
 * Moves an AI Player along a path
 */
public class Mover extends Behaviour{

    private Path path;
    private Path originalPath;
    private Node target;
    private boolean targetReached;
    private boolean finished = false;

    public Mover(AIPlayer ai, Pathfinding pathfinder){
        super(ai, pathfinder);
    }

    public void setPath(Path path){
        this.path = path;
        this.originalPath = path;
    }

    public void followPath(){
        finished = false;
        if(path == null) {
            finished = true;
            return;
        }
        if(path.getLength() == 0) {
            finished = true;
            return;
        }
        targetReached = false;
        target = path.getNode(0);
        //check distance from current position to the target, if too big calculate again
        double deltaX = (target.x * 64) - (ai.getLayoutX());
        double deltaY = (ai.getLayoutY()) - (target.y * 64);
        double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        if (distance > 250) {
            Node last = path.getNode((path.getLength() - 1));
            path = pathfinder.getPath((int)ai.getLayoutX()/64, (int)ai.getLayoutY()/64, last.x, last.y);
        } else {
            ai.setMoving(true);
        }


        move();
        if(targetReached){
            path.removeFirst();
        }
    }

    private void move(){
        double deltaX = (target.x * 64) - ai.getLayoutX() + 16;
        double deltaY = ai.getLayoutY() - (target.y * 64) + 16;
        if(Math.abs(deltaX) < 20 && Math.abs(deltaY) < 20) targetReached = true;
        double movementAngle = Math.atan2(deltaX, deltaY);
        ai.setMovementAngle(movementAngle);
    }

    public void tick(){
        enemies = ai.getEnemies();
        updateAngle();
        ai.setAngle(angle);
        ai.setShoot(updateShooting(closestX, closestY));
        timer = System.currentTimeMillis();
        followPath();
        if(ai.isEliminated()) {
            path = null;
            finished = true;
        }
    }

    public void change(){
    }

    public boolean isFinished(){
        return this.finished;
    }
}
