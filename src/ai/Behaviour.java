package ai;

import players.AIPlayer;


public abstract class Behaviour {

    protected AIPlayer ai;
    protected BehaviourManager manager;

    public Behaviour(AIPlayer ai, BehaviourManager manager){
        this.ai = ai;
        this.manager = manager;
    }

    public abstract void tick();

}


