package physics;

import enums.TeamEnum;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * A bullet is represented as a circle, that travels along a given direction
 */

public class Bullet extends Circle
{
	private static final float speed = 10f;
	private double angle;
	private double x, y;
	private double originX, originY;
	private boolean active;
	private int id;
	private Rectangle collision;

	/**
	 * Create a bullet at the given global coordinates with the given global angle
	 *
	 * @param x     The x-coordinate of the bullet
	 * @param y     The y-coordinate of the bullet
	 * @param angle The angle at which the bullet will travel
	 */

	public Bullet(int id, double x, double y, double angle, TeamEnum team) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.originX = x;
		this.originY = y;
		this.angle = angle;
		active = true;
		setCenterX(x);
		setCenterY(y);
		setRadius(3);
		setFill(team == TeamEnum.RED ? Color.RED : Color.BLUE);
	}

	/**
	 * Updates the position of the bullet
	 */
	public void moveInDirection()
	{
		y -= speed * Math.cos(angle);
		x += speed * Math.sin(angle);
		setCenterX(x);
		setCenterY(y);
		double distance = Math.sqrt(Math.pow((x - originX), 2) + Math.pow((originY - y), 2));
		if(distance > 500) active = false;
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

	public boolean isActive(){
		return this.active;
	}

	public void setActive(boolean b){
		this.active = b;
	}

	public int getBulletId(){
		return this.id;
	}

	public void setBulletId(int id){
		this.id = id;
	}

	public void disable()
	{
		active = false;
	}

	void disable(Rectangle collision)
	{
		active = false;
		this.collision = collision;
	}

	public double getAngle()
	{
		return this.angle;
	}

	public Rectangle getCollision()
	{
		return collision;
	}
}