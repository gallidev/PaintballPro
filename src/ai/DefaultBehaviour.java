package ai;

import players.AIPlayer;

public class DefaultBehaviour extends Behaviour {

    public DefaultBehaviour(AIPlayer ai, Pathfinding pathfinder){
        super(ai, pathfinder);
    }

    public void tick(){
        enemies = ai.getEnemies();
        updateAngle();
        ai.setAngle(angle);
        if(timer < System.currentTimeMillis() - delay){
            ai.setShoot(updateShooting(closestX, closestY));
            timer = System.currentTimeMillis();
        }
    }
}
