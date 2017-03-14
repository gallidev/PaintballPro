package ai;


import players.AIPlayer;

import static players.EssentialPlayer.PLAYER_HEAD_X;
import static players.EssentialPlayer.PLAYER_HEAD_Y;

public class CTFCaptureBehaviour extends Behaviour {

    private Mover mover;

    public CTFCaptureBehaviour(AIPlayer ai, BehaviourManager manager){
        super(ai, manager);
        mover = manager.getMover();
    }

    @Override
    public void tick() {
        int flagX = 3; //temp value
        int flagY = 3; //temp value
        PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + PLAYER_HEAD_X) / 64), Math.floor((ai.getLayoutY() + PLAYER_HEAD_Y) / 64), flagX, flagY);
        mover.setPath(ai.getHashMaps().getPathMap().get(p));
    }


}
