package ai;


import enums.TeamEnum;
import players.AIPlayer;

import static players.EssentialPlayer.PLAYER_HEAD_X;
import static players.EssentialPlayer.PLAYER_HEAD_Y;

/**
 * The behaviour that makes an AI player move back towards its base after capturing a flag
 * @author Sivarjuen Ravichandran
 */
public class CTFRetreatBehaviour extends Behaviour {

    /**
     * Instantiates the behaviour
     * @param ai The AI player
     * @param manager The AI's behaviour manager
     */
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
