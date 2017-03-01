package ai;

import players.AIPlayer;

public class BehaviourManager {
    private AIPlayer ai;
    private Pathfinding pathFinder;
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
        //pathFinder.getPath(2, 7, 25, 7);
        pathFinder.getPath(2, 7, 25, 7);
    }

    public void tick(){
        random.tick();
    }

    public void change(){
        random.change();
    }
    //TODO - Implement behaviours
}
