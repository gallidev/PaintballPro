package ai;


import players.AIPlayer;
import rendering.Map;

//Move towards the nearest target - this can be player or power-up
public class MoveBehaviour extends Behaviour {

    public MoveBehaviour(AIPlayer ai, Pathfinding pathfinder){
        super(ai, pathfinder);
    }

    private void findNext5Steps(){
        Path tempPath = pathfinder.getPath((int)ai.getLayoutX()/64, (int)ai.getLayoutY()/64, (int)closestX/64, (int)closestY/64);

    }
    @Override
    public void tick() {


    }

}
