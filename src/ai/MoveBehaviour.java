package ai;

import enums.GameMode;
import players.AIPlayer;

import java.util.Random;

import static players.EssentialPlayer.PLAYER_HEAD_X;
import static players.EssentialPlayer.PLAYER_HEAD_Y;

public class MoveBehaviour extends Behaviour {

    private long startTimer;
    private long startDelay;
    private boolean started = true;
    private boolean timerStarted = false;

    public MoveBehaviour(AIPlayer ai, BehaviourManager manager) {
        super(ai, manager);
        startDelay = 6000;
    }

    @Override
    public void tick() {
        if(started){
            if(!timerStarted){
                startAction();
                startTimer = System.currentTimeMillis();
                timerStarted = true;
            }
            if(startTimer < System.currentTimeMillis() - startDelay){
                started = false;
                PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + PLAYER_HEAD_X) / 64), Math.floor((ai.getLayoutY() + PLAYER_HEAD_Y) / 64), Math.floor(manager.getClosestX() / 64), Math.floor(manager.getClosestY() / 64));
                mover.setPath(ai.getHashMaps().getPathMap().get(p));
            }
        }
        else {
            PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + PLAYER_HEAD_X) / 64), Math.floor((ai.getLayoutY() + PLAYER_HEAD_Y) / 64), Math.floor(manager.getClosestX() / 64), Math.floor(manager.getClosestY() / 64));
            mover.setPath(ai.getHashMaps().getPathMap().get(p));
        }
        mover.tick();
    }
}
