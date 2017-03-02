package ai;

import players.AIPlayer;

public class BehaviourManager {
    private AIPlayer ai;
    private Pathfinding pathFinder;
    private Path path;
    private Mover mover;
    private Behaviour move;
    private Behaviour tactical;
    private Behaviour capture;
    private Behaviour retreat;
    private Behaviour random;
    private Behaviour defaultB;

    //Behaviours
    //    Default - no movement, just shooting
    //    Move - Move towards enemy
    //    Tactical  - Move towards cover
    //    Capture - Goes to the flag
    //    Retreat - Goes to base
    //    Random - Random movement

    public BehaviourManager(AIPlayer ai){
        this.ai = ai;
        pathFinder = new Pathfinding(ai.getMap());
        defaultB = new DefaultBehaviour(ai, pathFinder);
        move = new MoveBehaviour(ai, pathFinder);
        tactical = new TacticalBehaviour(ai, pathFinder);
        capture = new CaptureBehaviour(ai, pathFinder);
        retreat = new RetreatBehaviour(ai, pathFinder);
        random = new RandomBehaviour(ai, pathFinder);
        mover = new Mover(ai, pathFinder);
        path = pathFinder.getPath((int)ai.getLayoutX()/64, (int)ai.getLayoutY()/64, 15, 10);
        mover.setPath(path);
    }

    public void tick(){
        if(!mover.isFinished()) {
            mover.tick();
        } else {
            defaultB.tick();
        }
    }

    public void change(){
        if(mover.isFinished()) {
            random.change();
        } else {
            mover.change();
        }
    }




    //TODO - Implement behaviours
}
