package ai;


import enums.Team;
import players.AIPlayer;
import rendering.Map;

import static players.GeneralPlayer.playerHeadX;
import static players.GeneralPlayer.playerHeadY;

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
        PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + playerHeadX) / 64), Math.floor((ai.getLayoutY() + playerHeadY) / 64), flagX, flagY);
        mover.setPath(ai.getHashMaps().getPathMap().get(p));
    }


}
