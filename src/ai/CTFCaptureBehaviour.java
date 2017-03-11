package ai;


import enums.TeamEnum;
import players.AIPlayer;
import rendering.Map;

import static players.EssentialPlayer.playerHeadX;
import static players.EssentialPlayer.playerHeadY;

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
