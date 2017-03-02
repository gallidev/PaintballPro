package players;

import java.util.List;


import enums.TeamEnum;
import javafx.scene.shape.Polygon;
import physics.Bullet;
import physics.CollisionsHandler;
import rendering.Map;
/**
 *  The player, represented by an ImageView
 */
public class UserPlayer extends ServerMinimumPlayer{

	public UserPlayer(double x, double y, int id, double width, double height, Map map, TeamEnum team,
			CollisionsHandler collisionsHandler) {
		super(x, y, id, width, height, map, team, collisionsHandler);
	}

	public void tick()
	{
		// handle the collisions with walls and props before moving the position
		// of the player so to understand if he can move or not in a specific direction
		collisionsHandler.handlePropWallCollision(this);
		if(!eliminated)
		{
			lastX = getX();
			lastY = getY();
			lastAngle = angle;
			updatePosition();
			updateShooting();
			updateAngle();
		}
		else
		{
			checkSpawn();
		}
		//updatePlayerBounds();
		updateBullets();
//		if(!(lastX == getX() && lastY == getY() && lastAngle == angle)){
//			//sendServerNewPosition(getX(), getY(), angle);
//		}
		//sendActiveBullets();

		if(!invincible)
		{
			collisionsHandler.handleBulletCollision(this);
		}
		else
		{
			checkInvincibility();
		}
	}

	protected void updatePosition()
	{
		//System.out.println("collup: " + collUp + " collDown:" + collDown + " collLeft:" + collLeft + " collRight: " + collRight );
		if(up && !collUp){
			setY(getY() - movementSpeed);
		}else if(!up && collUp){
			setY(getY() + movementSpeed);
		}
		if(down && !collDown){
			setY(getY() + movementSpeed);
		}else if(!down && collDown){
			setY(getY() - movementSpeed);
		}
		if(left && !collLeft) {
			setX(getX() - movementSpeed);
		} else if(!left && collLeft){
			setX(getX() + movementSpeed);
		}
		if(right && !collRight){
			setX(getX() + movementSpeed);
		}else if (!right && collRight){
			setX(getX() - movementSpeed);
		}
	}


	//Calculates the angle the player is facing with respect to the mouse
	@Override
	protected void updateAngle()
	{
		//Point2D temp = this.localToScene(1.65 * playerHeadX, playerHeadY);
//		double x1 = getX() + (1.65*playerHeadX);
//		double y1 = getY() + playerHeadY;
		double x1 = getX() + getWidth()/2;
		double y1 = getY() + getHeight()/2;
		double deltax = mouseX - x1;
		double deltay = y1 - mouseY;
		angle = Math.atan2(deltax, deltay);
		rotation.setAngle(Math.toDegrees(angle));
	}

	protected void updateShooting(){
		if(shoot && shootTimer < System.currentTimeMillis() - shootDelay){
			shoot();
			shootTimer = System.currentTimeMillis();
		}
	}

	//Updates the location of the bullets
	protected void updateBullets(){
		for(int i = 0; i < firedBullets.size(); i++){
			firedBullets.get(i).moveInDirection();
		}
	}

	//Getters and setters below this point
	//-----------------------------------------------------------------------------

	public List<Bullet> getBullets(){
		return this.firedBullets;
	}

	public double getAngle(){
		return this.angle;
	}

	public void setAngle(double angle){
		this.angle = angle;
	}

	public void setUp(boolean up){
		this.up = up;
	}

	public void setDown(boolean down){
		this.down = down;
	}

	public void setLeft(boolean left){
		this.left = left;
	}

	public void setRight(boolean right){
		this.right = right;
	}

	public void setShoot(boolean shoot){
		this.shoot = shoot;
	}

	public TeamEnum getTeam() {
		return team;
	}

	public int getPlayerId(){
		return id;
	}

	public void setMX(double newX) {
	}
	public void setMY(double newY){
	}

	public boolean isEliminated(){
		return eliminated;
	}

	public Polygon getPolygonBounds() {
		return bounds;
	}

	public void setCollUp(boolean collUp) {
		this.collUp = collUp;
	}
	public void setCollDown(boolean collDown) {
		this.collDown = collDown;
	}
	public void setCollLeft(boolean collLeft) {
		this.collLeft = collLeft;
	}
	public void setCollRight(boolean collRight) {
		this.collRight = collRight;
	}

	@Override
	public void updateScore() {
		// TODO Auto-generated method stub

	}


}

