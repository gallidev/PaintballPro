package ai;

import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import players.AIPlayer;
import players.GeneralPlayer;

import java.util.ArrayList;

public class BehaviourManager{
    private AIPlayer ai;
    private Mover mover;
    private Behaviour move;
    private Behaviour combat;
    private Behaviour capture;
    private Behaviour retreat;

    private ArrayList<GeneralPlayer> enemies;
    private GeneralPlayer closestEnemy;
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
        //temp
        move.tick();

    }

    private boolean updateShooting(double x, double y){

        double distance = Math.sqrt(Math.pow(x - ai.getLayoutX(), 2) + (Math.pow(ai.getLayoutY() - y, 2)));
        if(closestEnemy == null) return false;
        if(closestEnemy.isEliminated()) return false;
        if(canSee(x, y) && distance < 350) return true;
        return false;
    }

    public boolean canSee(double x, double y){
        Line line = new Line(ai.getLayoutX() + ai.getImage().getWidth()/2, ai.getLayoutY() + ai.getImage().getHeight()/2, x, y);
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
        for(GeneralPlayer enemy: enemies){
            if(!enemy.isEliminated()) {
                double temp = Math.sqrt((Math.pow(enemy.getLayoutX() - ai.getLayoutX(), 2) + Math.pow(enemy.getLayoutY() - ai.getLayoutY(), 2)));
                if (temp < minDistance) {
                    closestEnemy = enemy;
                    closestX = enemy.getLayoutX();
                    closestY = enemy.getLayoutY();
                    minDistance = temp;
                }
            }
        }
        closestDistance = minDistance;
        if(minDistance < 400){
            double deltaX = closestX - ai.getLayoutX();
            double deltaY = ai.getLayoutY() - closestY;
            angle = Math.atan2(deltaX, deltaY);

        } else {
            angle = ai.getMovementAngle();
        }
    }

    public Mover getMover(){
        return this.mover;
    }

    public GeneralPlayer getClosestEnemy(){
        return this.closestEnemy;
    }

    public double getClosestX(){
        return this.closestX;
    }

    public double getClosestY(){
        return this.closestY;
    }

}
