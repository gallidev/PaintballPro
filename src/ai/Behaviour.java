package ai;


import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import players.AIPlayer;
import players.GeneralPlayer;
import players.ServerMinimumPlayer;
import rendering.Map;

import java.util.ArrayList;
import java.util.Random;

public abstract class Behaviour {
    protected Map map;
    protected AIPlayer ai;
    protected long timer = 0;
    protected long delay = 3000;
    protected Random rand;
    protected ArrayList<ServerMinimumPlayer> enemies;
    protected ServerMinimumPlayer closestEnemy;
    protected double angle;
    protected double closestX, closestY;
    protected Pathfinding pathfinder;

    public Behaviour(AIPlayer ai, Pathfinding pathfinder){
        this.ai = ai;
        this.map = ai.getMap();
        this.angle = 0.0;
        this.enemies = new ArrayList<ServerMinimumPlayer>();
        rand = new Random();
        this.pathfinder = pathfinder;
    }

        protected boolean updateShooting(double x, double y){
            double distance = Math.sqrt(Math.pow(x - ai.getLayoutX(), 2) + (Math.pow(ai.getLayoutY() - y, 2)));
            if(closestEnemy != null){
                if(closestEnemy.isEliminated()) return false;
            }
            if(canSee(x, y) && distance < 500) return true;
            return false;
        }

        public void change(){
            //timer = System.currentTimeMillis() - delay;
        }

        public void updateAngle(){
            double minDistance = Double.MAX_VALUE;
            for(ServerMinimumPlayer enemy: enemies){
                double temp = Math.sqrt((Math.pow(enemy.getLayoutX() - ai.getLayoutX(), 2) + Math.pow(enemy.getLayoutY() - ai.getLayoutY(), 2)));
                if(temp < minDistance){
                    closestEnemy = enemy;
                    closestX = enemy.getLayoutX();
                    closestY = enemy.getLayoutY();
                    minDistance = temp;
                }
            }

            if(minDistance < 400){
                double deltaX = closestX - ai.getLayoutX();
                double deltaY = ai.getLayoutY() - closestY;
                angle = Math.atan2(deltaX, deltaY);

            } else {
                angle = ai.getMovementAngle();
            }
        }

        public boolean canSee(double x, double y){
            Line line = new Line(ai.getLayoutX() + ai.getWidth()/2, ai.getLayoutY() + ai.getHeight()/2, x, y);
            ArrayList<Rectangle> propsWalls = ai.getMap().getRecProps();
            propsWalls.addAll(ai.getMap().getRecWalls());
            for(Rectangle propWall : propsWalls){
                if(Shape.intersect(line, propWall).getBoundsInLocal().isEmpty() == false) {
                    return false;
                }
            }
            return true;
        }


        public abstract void tick();
}


