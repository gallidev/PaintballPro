package ai;

import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import players.AIPlayer;
import java.util.ArrayList;

public class CombatBehaviour extends Behaviour {

    private Mover mover;

    public CombatBehaviour(AIPlayer ai, BehaviourManager manager){
        super(ai, manager);
        mover = manager.getMover();
    }


    @Override
    public void tick() {
        //take cover
    }

}
