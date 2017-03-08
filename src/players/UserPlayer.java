package players;

import java.util.List;


import enums.TeamEnum;
import javafx.scene.shape.Polygon;
import physics.Bullet;
import physics.CollisionsHandler;
import rendering.Map;
import rendering.Spawn;
/**
 *  The player, represented by an ImageView
 */
public class UserPlayer extends ServerMinimumPlayer{

    public final int widthScreen = 1024;
    public final int heightScreen = 576;

	public UserPlayer(double x, double y, int id, double width, double height, Spawn[] spawn, TeamEnum team,
			CollisionsHandler collisionsHandler) {
		super(x, y, id, width, height, spawn, team, collisionsHandler);
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
		double deltax = mouseX + (2* playerHeadX) - widthScreen/2;
		double deltay = heightScreen/2-  (mouseY - playerHeadY) ;
		angle = Math.atan2(deltax, deltay);
		double degrees = Math.toDegrees(angle);
		rotation.setAngle(degrees);
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


	@Override
	public void updateScore() {
		// TODO Auto-generated method stub

	}


}

