package ai;

import physics.CollisionsHandler;
import players.AIPlayer;

import static players.GeneralPlayer.playerHeadX;
import static players.GeneralPlayer.playerHeadY;

public class MoveBehaviour extends Behaviour {

    private Mover mover;

    public MoveBehaviour(AIPlayer ai, BehaviourManager manager){
        super(ai, manager);
        mover = manager.getMover();
    }

    @Override
    public void tick(){
        if(!mover.isFinished()) {
            PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + playerHeadX) / 64), Math.floor((ai.getLayoutY() + playerHeadY) / 64), Math.floor(manager.getClosestX() / 64), Math.floor(manager.getClosestY() / 64));
            mover.setPath(ai.getHashMaps().getPathMap().get(p));
        }
        mover.tick();
    }
}
