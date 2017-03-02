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

    public Mover(AIPlayer ai){
        super(ai);
    }

    public void setPath(Path path){
        this.path = path;
        this.originalPath = path;
    }

    public void followPath(){
        finished = false;
        if(path.getLength() == 0) {
            finished = true;
            return;
        }
        //System.out.println(path.getNode(0).toString());
        targetReached = false;
        target = path.getNode(0);
        move();
        if(targetReached){
            path.removeFirst();
        }
    }

    private void move(){
        double targetX = target.x * 64;
        double targetY = target.y * 64;
        double deltaX = targetX - ai.getLayoutX();
        double deltaY = ai.getLayoutY() - targetY;
        if(Math.abs(deltaX) < 1 && Math.abs(deltaY) < 1) targetReached = true;
        double movementAngle = Math.atan2(deltaX, deltaY);
        ai.setMovementAngle(movementAngle);
    }

    public void tick(){
        enemies = ai.getEnemies();
        updateAngle();
        ai.setAngle(angle);

        ai.setShoot(updateShooting(closestX, closestY));
        timer = System.currentTimeMillis();
        if(ai.isEliminated()) {
            finished = false;
            path = originalPath;
        }
        followPath();
    }

    public boolean isFinished(){
        return this.finished;
    }
}
