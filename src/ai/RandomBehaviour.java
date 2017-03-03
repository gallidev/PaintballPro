package ai;

import players.AIPlayer;

public class RandomBehaviour extends Behaviour{

	public RandomBehaviour(AIPlayer ai, Pathfinding pathfinder){

		super(ai, pathfinder);
	}

	private void randomMovement(){
		double targetX = (double)rand.nextInt(1900);
		double targetY = (double)rand.nextInt(800);
		double deltaX = targetX - ai.getLayoutX();
		double deltaY = ai.getLayoutY() - targetY;
		double movementAngle = Math.atan2(deltaX, deltaY);
		ai.setMovementAngle(movementAngle);
	}

	@Override
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

	public void change(){
		timer = System.currentTimeMillis() - delay;
	}
}
