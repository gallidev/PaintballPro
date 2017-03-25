package ai;

import enums.GameMode;
import players.AIPlayer;

import java.util.Random;

import static players.EssentialPlayer.PLAYER_HEAD_X;
import static players.EssentialPlayer.PLAYER_HEAD_Y;


public abstract class Behaviour {

    protected AIPlayer ai;
    protected BehaviourManager manager;
    protected Mover mover;
    protected Random rand;

    public Behaviour(AIPlayer ai, BehaviourManager manager){
        this.ai = ai;
        this.manager = manager;
        this.mover = manager.getMover();
        this.rand = new Random();
    }

    public abstract void tick();

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


