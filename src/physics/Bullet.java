package physics;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * A bullet is represented as a circle, that travels along a given direction
 */
public class Bullet extends Circle{
	
	private double angle;
	private float speed = 6f;
	private double x, y;

	/**
	 * Create a bullet at the given global coordinates with the given global angle
	 * @param x The x-coordinate of the bullet
	 * @param y The y-coordinate of the bullet
	 * @param angle The angle at which the bullet will travel
	 */
	public Bullet(double x, double y, double angle) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		setCenterX(x);
		setCenterY(y);
		setRadius(3);
		setFill(Color.RED);
	}

	/**
	 * Updates the position of the bullet
	 */
	public void moveInDirection() {
		y -= speed * Math.cos(angle);
		x += speed * Math.sin(angle);
		setCenterX(x);
		setCenterY(y);
	}
	
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
}