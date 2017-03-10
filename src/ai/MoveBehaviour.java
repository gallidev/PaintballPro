package ai;

import enums.GameMode;
import players.AIPlayer;

import java.util.Random;

import static players.GeneralPlayer.playerHeadX;
import static players.GeneralPlayer.playerHeadY;

public class MoveBehaviour extends Behaviour {

    private Mover mover;
    private long startTimer;
    private long startDelay;
    private boolean started = true;
    private boolean timerStarted = false;
    private Random rand = new Random();

    public MoveBehaviour(AIPlayer ai, BehaviourManager manager) {
        super(ai, manager);
        mover = manager.getMover();
        if(ai.getMap().getGameMode() == GameMode.ELIMINATION){
            startDelay = 6000;
        } else {
            startDelay = 12000;
        }
    }

    @Override
    public void tick() {
        if(started){
            if(!timerStarted){
                boolean b = rand.nextBoolean();
                if(b){
                    if(ai.getMap().getGameMode() == GameMode.ELIMINATION) {
                        PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + playerHeadX) / 64), Math.floor((ai.getLayoutY() + playerHeadY) / 64), 14, 2);
                        mover.setPath(ai.getHashMaps().getPathMap().get(p));
                    } else {
                        PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + playerHeadX) / 64), Math.floor((ai.getLayoutY() + playerHeadY) / 64), 8, 7);
                        mover.setPath(ai.getHashMaps().getPathMap().get(p));
                    }
                } else {
                    if(ai.getMap().getGameMode() == GameMode.ELIMINATION) {
                        PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + playerHeadX) / 64), Math.floor((ai.getLayoutY() + playerHeadY) / 64), 14, 10);
                        mover.setPath(ai.getHashMaps().getPathMap().get(p));
                    } else {
                        PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + playerHeadX) / 64), Math.floor((ai.getLayoutY() + playerHeadY) / 64), 27, 22);
                        mover.setPath(ai.getHashMaps().getPathMap().get(p));
                    }
                }
                startTimer = System.currentTimeMillis();
                timerStarted = true;
            }
            if(startTimer < System.currentTimeMillis() - startDelay){
                started = false;
                PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + playerHeadX) / 64), Math.floor((ai.getLayoutY() + playerHeadY) / 64), Math.floor(manager.getClosestX() / 64), Math.floor(manager.getClosestY() / 64));
                mover.setPath(ai.getHashMaps().getPathMap().get(p));
            }
        }
        else if (!mover.isFinished()) {
            PointPairs p = new PointPairs(Math.floor((ai.getLayoutX() + playerHeadX) / 64), Math.floor((ai.getLayoutY() + playerHeadY) / 64), Math.floor(manager.getClosestX() / 64), Math.floor(manager.getClosestY() / 64));
            mover.setPath(ai.getHashMaps().getPathMap().get(p));
        }
        mover.tick();
    }
}
