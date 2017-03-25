package physics;

import enums.TeamEnum;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * A pellet is represented as a circle, that travels along a given direction
 */

public class Pellet extends Circle
{
	private double speed = 10f;
	private double angle;
	private double x, y;
	private double originX, originY;
	private boolean active;
	private int id;
	private Rectangle collision;

	private TeamEnum colour;

	/**
	 * Create a pellet at the given global coordinates with the given global angle
	 *
	 * @param x     The x-coordinate of the pellet
	 * @param y     The y-coordinate of the pellet
	 * @param angle The angle at which the pellet will travel
	 */

	public Pellet(int id, double x, double y, double angle, TeamEnum team, double gameSpeed) {
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
		setCache(true);

		speed *= gameSpeed;
		colour = team;
	}

	/**
	 * Updates the position of the pellet
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

	public boolean isActive(){
		return this.active;
	}

	public void setActive(boolean b){
		this.active = b;
	}

	void disable()
	{
		active = false;
	}

	void disable(Rectangle collision)
	{
		active = false;
		this.collision = collision;
	}

	public Rectangle getCollision()
	{
		return collision;
	}

	public TeamEnum getColour(){
		return colour;
	}

	public int getPelletId(){
		return this.id;
	}
}