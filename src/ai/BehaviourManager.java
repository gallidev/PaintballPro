package ai;

import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import players.AIPlayer;
import players.EssentialPlayer;

import java.util.ArrayList;

import static players.EssentialPlayer.playerHeadX;
import static players.EssentialPlayer.playerHeadY;

public class BehaviourManager{
    private AIPlayer ai;
    private Mover mover;
    private Behaviour move;
    private Behaviour combat;
    private Behaviour capture;
    private Behaviour retreat;

    private ArrayList<EssentialPlayer> enemies;
    private EssentialPlayer closestEnemy;
    private double closestDistance = 0;
    private double angle;
    private double closestX, closestY;

    //Behaviours
    //    Move - Move towards nearest enemy
    //    Combat  - Tactical movement i.e. take cover
    //    CTFCapture - Goes to the flag
    //    CTFRetreat - Goes to base
    //    Random - Random movement - TEMP

    public BehaviourManager(AIPlayer ai){
        this.ai = ai;
        this.enemies = new ArrayList<>();
        mover = new Mover(ai);
        move = new MoveBehaviour(ai, this);
        combat = new CombatBehaviour(ai, this);
        capture = new CTFCaptureBehaviour(ai, this);
        retreat = new CTFRetreatBehaviour(ai, this);

    }

    public void tick(){
        enemies = ai.getEnemies();
        updateAngle();
        ai.setAngle(angle);
        ai.setShoot(updateShooting(closestX, closestY));
        if(closestDistance < 400){
            combat.tick();
        } else {
            //move.tick();
        }
        //temp until combat behaviour has been implemented
        move.tick();

    }

    private boolean updateShooting(double x, double y){

        double distance = Math.sqrt(Math.pow(x - (ai.getLayoutX() + playerHeadX), 2) + (Math.pow((ai.getLayoutY() + playerHeadY) - y, 2)));
        if(closestEnemy == null) return false;
        if(closestEnemy.isEliminated()) return false;
        if(canSee(x, y) && distance < 350) return true;
        return false;
    }

    public boolean canSee(double x, double y){
        Line line = new Line((ai.getLayoutX() + playerHeadX), (ai.getLayoutY() + playerHeadY), x, y);
        ArrayList<Rectangle> propsWalls = ai.getMap().getRecProps();
        propsWalls.addAll(ai.getMap().getRecWalls());
        for(Rectangle propWall : propsWalls){
            if(Shape.intersect(line, propWall).getBoundsInLocal().isEmpty() == false) {
                return false;
            }
        }
        return true;
    }

    public void updateAngle(){
        double minDistance = Double.MAX_VALUE;
        for(EssentialPlayer enemy: enemies){
            if(!enemy.isEliminated()) {
                double temp = Math.sqrt((Math.pow((enemy.getLayoutX() + playerHeadX) - (ai.getLayoutX() + playerHeadX), 2) + Math.pow((enemy.getLayoutY() + playerHeadY) - (ai.getLayoutY() + playerHeadY), 2)));
                if (temp < minDistance) {
                    closestEnemy = enemy;
                    closestX = enemy.getLayoutX() + playerHeadX;
                    closestY = enemy.getLayoutY() + playerHeadY;
                    minDistance = temp;
                }
            }
        }
        closestDistance = minDistance;
        if(minDistance < 400){
            double deltaX = closestX - (ai.getLayoutX()+playerHeadX);
            double deltaY = (ai.getLayoutY()+playerHeadY) - closestY;
            angle = Math.atan2(deltaX, deltaY);

        } else {
            angle = ai.getMovementAngle();
        }
    }

    public Mover getMover(){
        return this.mover;
    }

    public EssentialPlayer getClosestEnemy(){
        return this.closestEnemy;
    }

    public double getClosestX(){
        return this.closestX;
    }

    public double getClosestY(){
        return this.closestY;
    }

}
