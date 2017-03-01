package ai;

import players.AIPlayer;
/**
 * Moves an AI Player along a path
 */
public class Mover {

    private Path path;
    private AIPlayer ai;
    private boolean targetReached;

    public Mover(AIPlayer ai){
        this.ai = ai;
    }

    public void followPath(Path path){
        this.path = path;
        while(path.getLength() > 0){
            targetReached = false;
            Node current = path.getNode(0);
            move(current);
            if(targetReached){
                path.removeFirst();
            }

        }
    }

    private void move(Node n){
        double targetX = n.x * 64;
        double targetY = n.y * 64;
        double deltaX = targetX - ai.getLayoutX();
        double deltaY = ai.getLayoutY() - targetY;
        if(Math.abs(deltaX) < 10 && Math.abs(deltaY) < 10) targetReached = true;
        double movementAngle = Math.atan2(deltaX, deltaY);
        ai.setMovementAngle(movementAngle);
    }
}
