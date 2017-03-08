package physics;

import enums.TeamEnum;
import javafx.scene.CacheHint;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * A bullet is represented as a circle, that travels along a given direction
 */
public class GhostBullet extends Circle{

	private double x, y;
	private int id;

	/**
	 * Create a bullet at the given global coordinates with the given global angle
	 * @param x The x-coordinate of the bullet
	 * @param y The y-coordinate of the bullet
	 * @param angle The angle at which the bullet will travel
	 */
	public GhostBullet(int id,double x, double y, TeamEnum team) {
		setCache(true);
		setCacheHint(CacheHint.SPEED);
		this.id = id;
		this.x = x;
		this.y = y;
		setCenterX(x);
		setCenterY(y);
		setRadius(3);
		if(team == TeamEnum.RED){
			setFill(Color.RED);
		} else {
			setFill(Color.BLUE);
		}

	}

//	/**
//	 * Updates the position of the bullet
//	 */
//	public void moveInDirection() {
//		y -= speed * Math.cos(angle);
//		x += speed * Math.sin(angle);
//		setCenterX(x);
//		setCenterY(y);
//		double distance = Math.sqrt(Math.pow((x-originX), 2) + Math.pow((originY-y), 2));
//		if(distance > 500) active = false;
//	}

	//Getters and setters for the coordinates

	public double getX(){
		return this.x;
	}

	public void setX(double x){
		this.x = x;
	}

	public double getY(){
		return this.y;
	}

	public void setY(double y){
		this.y = y;
	}

	public int getBulletId(){
		return this.id;
	}

	public void setBulletId(int id){
		this.id = id;
	}

}