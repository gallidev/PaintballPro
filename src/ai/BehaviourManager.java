package ai;

import players.AIPlayer;

public class BehaviourManager {
    private AIPlayer ai;
    private Behaviour aggressive = new AggressiveBehaviour(ai);
    private Behaviour defensive = new DefensiveBehaviour(ai);
    private Behaviour capture = new CaptureBehaviour(ai);
    private Behaviour retreat = new RetreatBehaviour(ai);
    private Behaviour random = new RandomBehaviour(ai);

    //Behaviours
    //    Aggressive - Move a lot closer to enemies
    //    Defensive  - Keep at a distance from the enemies, but still within range
    //    Capture - Goes to the flag
    //    Retreat - Returns to base i.e after capturing the flag
    //    Random - Random movement

    public BehaviourManager(AIPlayer ai){
        this.ai = ai;
        aggressive = new AggressiveBehaviour(ai);
        defensive = new DefensiveBehaviour(ai);
        capture = new CaptureBehaviour(ai);
        retreat = new RetreatBehaviour(ai);
        random = new RandomBehaviour(ai);

    }

    //TODO - Implement behaviours
}
