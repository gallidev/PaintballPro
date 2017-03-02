package ai;

import enums.TeamEnum;
import players.AIPlayer;
import rendering.Map;

public class BehaviourManager {
    private AIPlayer ai;
    private Pathfinding pathFinder;
    private Path path;
    private Mover mover;
    private Behaviour aggressive;
    private Behaviour defensive;
    private Behaviour capture;
    private Behaviour retreat;
    private Behaviour random;

    //Behaviours
    //    Aggressive - Move a lot closer to enemies
    //    Defensive  - Keep at a distance from the enemies, but still within range
    //    Capture - Goes to the flag
    //    Retreat - Returns to base i.e after capturing the flag
    //    Random - Random movement

    public BehaviourManager(AIPlayer ai){
        this.ai = ai;
        pathFinder = new Pathfinding(ai.getMap());
        aggressive = new AggressiveBehaviour(ai);
        defensive = new DefensiveBehaviour(ai);
        capture = new CaptureBehaviour(ai);
        retreat = new RetreatBehaviour(ai);
        random = new RandomBehaviour(ai);
        if(ai.getTeam() == TeamEnum.RED) {
            path = pathFinder.getPath(ai.getMap().getSpawns()[0].x, ai.getMap().getSpawns()[0].y, 17, 10);
        } else {
            path = pathFinder.getPath(ai.getMap().getSpawns()[6].x, ai.getMap().getSpawns()[6].y, 13, 10);

        }
        mover = new Mover(ai);
        mover.setPath(path);
    }

    public void tick(){
        if(!mover.isFinished()) {
            mover.tick();
        } else {
            random.tick();
        }
    }

    public void change(){
        if(mover.isFinished()) {
            random.change();
        }
    }




    //TODO - Implement behaviours
}
