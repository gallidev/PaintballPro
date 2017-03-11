package ai;


import enums.TeamEnum;
import players.AIPlayer;
import rendering.Map;

import static players.EssentialPlayer.playerHeadX;
import static players.EssentialPlayer.playerHeadY;

public class CTFRetreatBehaviour extends Behaviour {

    private Mover mover;

    public CTFRetreatBehaviour(AIPlayer ai, BehaviourManager manager){
        super(ai, manager);
        mover = manager.getMover();
    }

    @Override
    public void tick() {
        if (ai.getTeam() == TeamEnum.RED){
            PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + playerHeadX) / 64), Math.floor((ai.getLayoutY() + playerHeadY) / 64), 4, 23);
            mover.setPath(ai.getHashMaps().getPathMap().get(p));
        } else {
            PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + playerHeadX) / 64), Math.floor((ai.getLayoutY() + playerHeadY) / 64), 26, 5);
            mover.setPath(ai.getHashMaps().getPathMap().get(p));
        }
    }

}
