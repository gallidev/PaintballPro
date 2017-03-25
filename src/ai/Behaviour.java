package ai;

import enums.GameMode;
import players.AIPlayer;

import java.util.Random;

import static players.EssentialPlayer.PLAYER_HEAD_X;
import static players.EssentialPlayer.PLAYER_HEAD_Y;

/**
 * Abstract class for all AI Behaviours
 *
 * @author Sivarjuen Ravichandran
 *
 */
public abstract class Behaviour {

    protected AIPlayer ai;
    protected BehaviourManager manager;
    protected Mover mover;
    protected Random rand;

    /**
     * Initialise the Behaviour super class
     * @param ai The ai player
     * @param manager The ai's Behaviour manager
     */
    public Behaviour(AIPlayer ai, BehaviourManager manager){
        this.ai = ai;
        this.manager = manager;
        this.mover = manager.getMover();
        this.rand = new Random();
    }

    public abstract void tick();

    /**
     * Called right at the beginning of a game to allow the AI's to spread out
     */
    public void startAction(){
        boolean b = rand.nextBoolean();
        if(b){
            if(ai.getMap().getGameMode() == GameMode.TEAM_MATCH) {
                PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + PLAYER_HEAD_X) / 64), Math.floor((ai.getLayoutY() + PLAYER_HEAD_Y) / 64), 14, 2);
                mover.setPath(ai.getHashMaps().getPathMap().get(p));
            } else {
                PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + PLAYER_HEAD_X) / 64), Math.floor((ai.getLayoutY() + PLAYER_HEAD_Y) / 64), 8, 7);
                mover.setPath(ai.getHashMaps().getPathMap().get(p));
            }
        } else {
            if(ai.getMap().getGameMode() == GameMode.TEAM_MATCH) {
                PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + PLAYER_HEAD_X) / 64), Math.floor((ai.getLayoutY() + PLAYER_HEAD_Y) / 64), 14, 10);
                mover.setPath(ai.getHashMaps().getPathMap().get(p));
            } else {
                PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + PLAYER_HEAD_X) / 64), Math.floor((ai.getLayoutY() + PLAYER_HEAD_Y) / 64), 27, 22);
                mover.setPath(ai.getHashMaps().getPathMap().get(p));
            }
        }
    }

}


