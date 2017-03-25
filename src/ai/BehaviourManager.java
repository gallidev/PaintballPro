package ai;

import enums.GameMode;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import players.AIPlayer;
import players.EssentialPlayer;

import java.util.ArrayList;
import java.util.Random;

import static players.EssentialPlayer.PLAYER_HEAD_X;
import static players.EssentialPlayer.PLAYER_HEAD_Y;

/**
 * The manager that represents tha finite state machine that switches between the different behaviours accordingly
 * @author Sivarjuen Ravichandran
 *
 */
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
    private Random random;

    private GameMode gameMode;

    //Behaviours
    //    Move - Move towards nearest enemy
    //    Combat  - Tactical movement i.e. take cover
    //    CTFCapture - Goes to the flag
    //    CTFRetreat - Goes to base
    //    Random - Random movement - TEMP

    /**
     * Initialise all behaviours
     * @param ai The ai player
     */
    public BehaviourManager(AIPlayer ai){
        this.ai = ai;
        this.enemies = new ArrayList<>();
        mover = new Mover(ai);
        move = new MoveBehaviour(ai, this);
        capture = new CTFCaptureBehaviour(ai, this);
        retreat = new CTFRetreatBehaviour(ai, this);
        random = new Random();
        gameMode = ai.getMap().getGameMode();
    }

    /**
     * Updates the active behaviour when certain conditions are met
     */
    public void tick(){
        defaultTick();
        if(gameMode == GameMode.CAPTURE_THE_FLAG) {
            if (ai.hasFlag()) {
                retreat.tick();
            } else {
                capture.tick();
            }
        } else {
            move.tick();
        }
    }

    /**
     * The default behaviour that is called every time
     * - finds nearest target
     * - aims/shoots at the target if the target is within range
     */
    private void defaultTick(){
        enemies = ai.getEnemies();
        updateAngle();
        ai.setAngle(angle);
        ai.setShoot(updateShooting(closestX, closestY));
    }

    /**
     * Checks to see if the nearest enemy can be shot
     * @param x The x-coordinate of the target
     * @param y The y-coordinate of the target
     * @return True if the target is within range, and in sight. False otherwise.
     */
    private boolean updateShooting(double x, double y){

        double distance = Math.sqrt(Math.pow(x - (ai.getLayoutX() + PLAYER_HEAD_X), 2) + (Math.pow((ai.getLayoutY() + PLAYER_HEAD_Y) - y, 2)));
        if(closestEnemy == null) return false;
        if(closestEnemy.isEliminated()) return false;
	    return canSee(x, y) && distance < 350;

    }

    /**
     * Checks to see if a particular location is in the AI's line of sight
     * @param x The x-coordinate of the target
     * @param y The y-coordinate of the target
     * @return True if the AI has clear line of sight to the target coordinates
     */
    public boolean canSee(double x, double y){
        Line line = new Line((ai.getLayoutX() + PLAYER_HEAD_X), (ai.getLayoutY() + PLAYER_HEAD_Y), x, y);
        ArrayList<Rectangle> propsWalls = ai.getMap().getPropCollisionBounds();
        propsWalls.addAll(ai.getMap().getWallCollisionBounds());
        for(Rectangle propWall : propsWalls){
            if(Shape.intersect(line, propWall).getBoundsInLocal().isEmpty() == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * Implicitly finds closest enemy and checks if the AI should face them if they are in range
     */
    public void updateAngle(){
        double minDistance = Double.MAX_VALUE;
        for(EssentialPlayer enemy: enemies){
            if(!enemy.isEliminated()) {
                double temp = Math.sqrt((Math.pow((enemy.getLayoutX() + PLAYER_HEAD_X) - (ai.getLayoutX() + PLAYER_HEAD_X), 2) + Math.pow((enemy.getLayoutY() + PLAYER_HEAD_Y) - (ai.getLayoutY() + PLAYER_HEAD_Y), 2)));
                if (temp < minDistance) {
                    closestEnemy = enemy;
                    closestX = enemy.getLayoutX() + PLAYER_HEAD_X;
                    closestY = enemy.getLayoutY() + PLAYER_HEAD_Y;
                    minDistance = temp;
                }
            }
        }

        closestDistance = minDistance;
        if(minDistance < 400){
            double deltaX = closestX - (ai.getLayoutX()+ PLAYER_HEAD_X);
            double deltaY = (ai.getLayoutY()+ PLAYER_HEAD_Y) - closestY;
            angle = Math.atan2(deltaX, deltaY);

        } else {
            angle = ai.getMovementAngle();
        }
    }

    /**
     * Returns the AI's mover
     * @return
     */
    public Mover getMover(){
        return this.mover;
    }

    /**
     * Returns the closest enemy to the AI
     * @return
     */
    public EssentialPlayer getClosestEnemy(){
        return this.closestEnemy;
    }

    /**
     * Returns the x-coordinate of the closest enemy
     * @return
     */
    public double getClosestX(){
        return this.closestX;
    }

    /**
     * Returns the y-coordinate of the closest enemy
     * @return
     */
    public double getClosestY(){
        return this.closestY;
    }

}
