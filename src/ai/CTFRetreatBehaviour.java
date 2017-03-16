package ai;


import enums.TeamEnum;
import players.AIPlayer;

import static players.EssentialPlayer.PLAYER_HEAD_X;
import static players.EssentialPlayer.PLAYER_HEAD_Y;

public class CTFRetreatBehaviour extends Behaviour {


    public CTFRetreatBehaviour(AIPlayer ai, BehaviourManager manager){
        super(ai, manager);
    }

    @Override
    public void tick() {
        if (ai.getTeam() == TeamEnum.RED){
            PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + PLAYER_HEAD_X) / 64), Math.floor((ai.getLayoutY() + PLAYER_HEAD_Y) / 64), 4, 23);
            mover.setPath(ai.getHashMaps().getPathMap().get(p));
        } else {
            PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + PLAYER_HEAD_X) / 64), Math.floor((ai.getLayoutY() + PLAYER_HEAD_Y) / 64), 26, 5);
            mover.setPath(ai.getHashMaps().getPathMap().get(p));
        }
        mover.tick();
    }

}
