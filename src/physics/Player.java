package physics;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import logic.GameObject;
import rendering.*;
import java.util.ArrayList;
import java.util.List;

import enums.Teams;

/**
 *  The player, represented by an ImageView
 */
public class Player extends GeneralPlayer{

	private double mx, my;
	private boolean controlScheme;


	/**
	 * Create a new player at the set location, and adds the rotation property to the player
	 * @param x The x-coordinate of the player with respect to the map
	 * @param y The y-coordinate of the player with respect to the map
	 * @param controlScheme True - movement with respect to cursor location, False - movement with respect to global position
	 * @param scene The scene in which the player will be displayed
	 *
	 */
	public Player(double x, double y, String nickname, boolean controlScheme, Renderer scene, Teams team, Image image){
		super(x, y, nickname, scene, team, image);
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
		if(!eliminated){
			updatePosition();
			updateShooting();
			updateAngle();
		} else {
			checkSpawn();
		}
		updatePlayerBounds();
		updateBullets();
		handlePropWallCollision();
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
			if(up) y -= movementSpeed;
			if(down) y += movementSpeed;
			if(left) x -= movementSpeed;
			if(right) x += movementSpeed;
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

		angle = Math.atan2(deltax, deltay);
		rotation.setAngle(Math.toDegrees(angle));
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
