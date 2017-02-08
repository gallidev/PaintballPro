package ai;

import java.util.Random;

public class RandomBehaviour {
	
	private AIPlayer ai;
	private long timer = 0;
	private long delay = 3000;
	private Random rand;
	
	public RandomBehaviour(AIPlayer ai){
		this.ai = ai;
		rand = new Random();
	}
	
	private double randomDirection(){
		double x = (double)rand.nextInt(1800);
		double y = (double)rand.nextInt(800);
		double aiX = ai.getXCoord();
		double aiY = ai.getYCoord();
		double deltax = x - aiX;
		double deltay = aiY - y;
		return Math.atan2(deltax, deltay);
	}
	
	private boolean randomShooting(){
		return (rand.nextInt(4) == 1);
	}
	
	public void change(){
		timer = System.currentTimeMillis();
	}
	
	
	public void tick() {
		if(timer < System.currentTimeMillis() - delay){
			ai.setAngle(randomDirection());
			ai.setShoot(randomShooting());
			timer = System.currentTimeMillis();
		}	
	}	
}
