package ai;


import enums.Team;
import players.AIPlayer;
import rendering.Map;

import static players.GeneralPlayer.playerHeadX;
import static players.GeneralPlayer.playerHeadY;

public class CTFRetreatBehaviour extends Behaviour {

    private Mover mover;

    public CTFRetreatBehaviour(AIPlayer ai, BehaviourManager manager){
        super(ai, manager);
        mover = manager.getMover();
    }

    @Override
    public void tick() {
        if (ai.getTeam() == Team.RED){
            PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + playerHeadX) / 64), Math.floor((ai.getLayoutY() + playerHeadY) / 64), 4, 23);
            mover.setPath(ai.getHashMaps().getPathMap().get(p));
        } else {
            PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + playerHeadX) / 64), Math.floor((ai.getLayoutY() + playerHeadY) / 64), 26, 5);
            mover.setPath(ai.getHashMaps().getPathMap().get(p));
        }
    }

}
