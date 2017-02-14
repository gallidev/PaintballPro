package ai;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import physics.GeneralPlayer;

public class RandomBehaviour {
	
	private AIPlayer ai;
	private long timer = 0;
	private long delay = 3000;
	private Random rand;
	private ArrayList<GeneralPlayer> enemies;
	private double angle;
	private double closestX, closestY;
	
	public RandomBehaviour(AIPlayer ai){
		this.ai = ai;
		this.angle = 0.0;
		this.enemies = new ArrayList<GeneralPlayer>();
		rand = new Random();
	}
	
	private void randomMovement(){
		//0 = Up, 1 = Down, 2 = Vertically stationary
		//0 = Left, 1 = Right, 2 = Horizontally stationary
		double targetX = (double)rand.nextInt(1900);
		double targetY = (double)rand.nextInt(800);
		double deltaX = targetX - ai.getLayoutX();
		double deltaY = ai.getLayoutY() - targetY;
		double movementAngle = Math.atan2(deltaX, deltaY);
		ai.setMovementAngle(movementAngle);
	}
	
	private boolean updateShooting(double x, double y){
		double distance = Math.sqrt(Math.pow(x - ai.getLayoutX(), 2) + (Math.pow(ai.getLayoutY() - y, 2)));
		if(canSee(x, y) && distance < 500) return true;
		return false;
	}
	
	public void change(){
		timer = System.currentTimeMillis() - delay;
	}
	
	public void updateAngle(){
		double minDistance = Double.MAX_VALUE;
		for(GeneralPlayer enemy: enemies){
			double temp = Math.sqrt((Math.pow(enemy.getLayoutX() - ai.getLayoutX(), 2) + Math.pow(enemy.getLayoutY() - ai.getLayoutY(), 2)));
			if(temp < minDistance){
				closestX = enemy.getLayoutX();
				closestY = enemy.getLayoutY();
				minDistance = temp;
			}
		}
		double deltaX = closestX - ai.getLayoutX();
		double deltaY = ai.getLayoutY() - closestY;
		angle = Math.atan2(deltaX, deltaY);
	}
	
	public boolean canSee(double x, double y){
		Line line = new Line(ai.getLayoutX(), ai.getLayoutY(), x, y);
		ArrayList<Rectangle> propsWalls = ai.getMap().getRecProps();
		propsWalls.addAll(ai.getMap().getRecWalls());
		for(Rectangle propWall : propsWalls){
			if(Shape.intersect(line, propWall).getBoundsInLocal().isEmpty() == false) {
				return false;
			}
		}
		return true;
	}
	
	
	public void tick() {
		enemies = ai.getEnemies();
		updateAngle();
		ai.setAngle(angle);
		if(timer < System.currentTimeMillis() - delay){
			randomMovement();
			ai.setShoot(updateShooting(closestX, closestY));
			timer = System.currentTimeMillis();
		}	
	}	
}
