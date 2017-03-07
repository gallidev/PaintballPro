package ai;

import players.AIPlayer;

public class MoveBehaviour extends Behaviour {

    private Mover mover;

    public MoveBehaviour(AIPlayer ai, BehaviourManager manager){
        super(ai, manager);
        mover = manager.getMover();
    }

    @Override
    public void tick(){
        if(!mover.isFinished()) {
            PointPairs p = new PointPairs(ai.getLayoutX() / 64, ai.getLayoutY() / 64, manager.getClosestX() / 64, manager.getClosestY() / 64);
            mover.setPath(ai.getHashMaps().getPathMap().get(p));
        }
        mover.tick();
    }
}
