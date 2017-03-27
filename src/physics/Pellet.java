package physics;

import enums.TeamEnum;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


/**
 * A pellet is represented as a circle, that travels along a given direction.
 *
 * @author Filippo Galli
 * @author Sivarjuen Ravichandran
 *
 */

public class Pellet extends Circle
{

	/** The speed of the pellet. */
	private double speed = 10f;

	/** The angle of the pellet. */
	private double angle;

	/** The current x and y positions of the pellet. */
	private double x, y;

	/** The origin x and y where the pellet was created. */
	private double originX, originY;

	/** can the pellet elimate players is it active. */
	private boolean active;

	/** The id of the pellet. */
	private int id;

	/** The rectangle for checking collisions. */
	private Rectangle collision;

	/** The colour of the pellet. */
	private TeamEnum colour;

	/**
	 * Create a pellet at the given global coordinates with the given global angle.
	 *
	 * @param id 	the id of the pellet
	 * @param x     The x-coordinate of the pellet
	 * @param y     The y-coordinate of the pellet
	 * @param angle The angle at which the pellet will travel
	 * @param team the team of the player that has shot the pellet
	 * @param gameSpeed the game speed the game speed at the simulation is running
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
	 * Updates the position of the pellet in the direction of the angle.
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

	/**
	 * Checks if is active.
	 *
	 * @return true, if is active
	 */
	public boolean isActive(){
		return this.active;
	}

	/**
	 * Sets the active.
	 *
	 * @param b the new active
	 */
	public void setActive(boolean b){
		this.active = b;
	}

	/**
	 * Disable the pellet.
	 */
	void disable()
	{
		active = false;
	}

	/**
	 * Disable the pellet and set a new rectangle for collisions.
	 *
	 * @param collision the collision
	 */
	void disable(Rectangle collision)
	{
		active = false;
		this.collision = collision;
	}

	/**
	 * Gets the collision.
	 *
	 * @return the collision
	 */
	public Rectangle getCollision()
	{
		return collision;
	}

	/**
	 * Gets the colour.
	 *
	 * @return the colour
	 */
	public TeamEnum getColour(){
		return colour;
	}

	/**
	 * Gets the pellet id.
	 *
	 * @return the pellet id
	 */
	public int getPelletId(){
		return this.id;
	}
}