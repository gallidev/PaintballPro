package physics;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import logic.GameObject;
import networkingClient.ClientSender;
import rendering.*;
import java.util.ArrayList;
import java.util.List;

import enums.Teams;

/**
 *  The player, represented by an ImageView that should be running
 */
public class ClientPlayer extends GeneralPlayer{

	private double mx, my;
	private boolean controlScheme;
	private ClientSender sender;


	/**
	 * Create a new player at the set location, and adds the rotation property to the player
	 * @param x The x-coordinate of the player with respect to the map
	 * @param y The y-coordinate of the player with respect to the map
	 * @param controlScheme True - movement with respect to cursor location, False - movement with respect to global position
	 * @param scene The scene in which the player will be displayed
	 *
	 */
	public ClientPlayer(double x, double y, int id, boolean controlScheme, Renderer scene, Teams team, Image image){
		super(x, y, id, scene, team, image);
		this.mx = x;
		this.my = y;
		this.controlScheme = controlScheme;
		angle = 0.0;
	}


	/**
	 * Tick is called every frame
	 * It updates the player location and angle, and shoots bullets if the shoot button is pressed
	 */
	@Override
	public void tick() {
		// handle the collisions with walls and props before moving the position
		// of the player so to understand if he can move or not in a specific direction
		handlePropWallCollision();
		if(!eliminated){
			updatePosition();
			updateShooting();
			updateAngle();
			sendServer();
		} else {
			checkSpawn();
		}
		updatePlayerBounds();
		updateBullets();
		if(!invincible){
			handleBulletCollision();
		} else {
			checkInvincibility();
		}
	}

	@Override
	protected void updatePosition(){
		if(controlScheme){
			if(up){
				y -= movementSpeed * Math.cos(angle);
				x += movementSpeed * Math.sin(angle);
			}
			if(down){
				y += movementSpeed * Math.cos(angle);
				x -= movementSpeed * Math.sin(angle);
			}
			if(left){
				y -= movementSpeed * Math.cos(angle - Math.PI/2);
				x += movementSpeed * Math.sin(angle - Math.PI/2);
			}
			if(right){
				y -= movementSpeed * Math.cos(angle + Math.PI/2);
				x += movementSpeed * Math.sin(angle + Math.PI/2);
			}
		} else {
			if(up && !collUp) y -= movementSpeed;
			if(down && !collDown) y += movementSpeed;
			if(left  && !collLeft) x -= movementSpeed;
			if(right  && !collRight) x += movementSpeed;
		}

		setLayoutX(x);
		setLayoutY(y);
	}


	//Calculates the angle the player is facing with respect to the mouse
	@Override
	protected void updateAngle(){
		Point2D temp = this.localToScene(1.65 * playerHeadX, playerHeadY);
		double x1 = temp.getX();
		double y1 = temp.getY();

		double deltax = mx - x1;
		double deltay = y1 - my;
		if(collUp){
			y += movementSpeed;
		} else if(collDown) {
			y -= movementSpeed;
		} else if(collLeft) {
			x += movementSpeed;
		} else if(collRight) {
			x -= movementSpeed;
		}
		angle = Math.atan2(deltax, deltay);
		rotation.setAngle(Math.toDegrees(angle));
	}

	private void sendServer() {

	}


	public double getMX(){
		return this.mx;
	}

	public void setMX(double mx){
		this.mx = mx;
	}

	public double getMY(){
		return this.my;
	}

	public void setMY(double my){
		this.my = my;
	}

}
