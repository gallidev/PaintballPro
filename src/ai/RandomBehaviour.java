package ai;

import java.util.ArrayList;
import java.util.Random;

import physics.GeneralPlayer;

public class RandomBehaviour {
	
	private AIPlayer ai;
	private long timer = 0;
	private long delay = 3000;
	private Random rand;
	private ArrayList<GeneralPlayer> enemies;
	private double angle;
	
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
		double deltaX = targetX - ai.getXCoord();
		double deltaY = ai.getYCoord() - targetY;
		double movementAngle = Math.atan2(deltaX, deltaY);
		ai.setMovementAngle(movementAngle);
	}
	
	private boolean randomShooting(){
		return (rand.nextInt(4) == 1);
	}
	
	public void change(){
		timer = System.currentTimeMillis() - delay;
	}
	
	public void updateAngle(){
		double closestX = 0;
		double closestY = 0;
		double minDistance = Double.MAX_VALUE;
		for(GeneralPlayer enemy: enemies){
			double temp = Math.sqrt((Math.pow(enemy.getXCoord() - ai.getXCoord(), 2) + Math.pow(enemy.getYCoord() - ai.getYCoord(), 2)));
			if(temp < minDistance){
				closestX = enemy.getXCoord();
				closestY = enemy.getYCoord();
				minDistance = temp;
			}
		}
		double deltaX = closestX - ai.getXCoord();
		double deltaY = ai.getYCoord() - closestY;
		angle = Math.atan2(deltaX, deltaY);
	}
	
	
	public void tick() {
		enemies = ai.getEnemies();
		updateAngle();
		ai.setAngle(angle);
		if(timer < System.currentTimeMillis() - delay){
			randomMovement();
			ai.setShoot(randomShooting());
			timer = System.currentTimeMillis();
		}	
	}	
}
