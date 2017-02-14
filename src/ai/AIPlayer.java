package ai;
import audio.AudioManager;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import logic.GameObject;
import physics.Bullet;
import physics.GeneralPlayer;
import physics.ClientPlayer;
import rendering.*;
import java.util.ArrayList;
import java.util.List;
import enums.TeamEnum;

/**
 * This class should be running on the server
 * @author gallifilippo
 *
 */
public class AIPlayer extends GeneralPlayer{

	private RandomBehaviour rb;
	private AudioManager audio;
	private double movementAngle;

	public AIPlayer(double x, double y, int id, Map map, TeamEnum team, Image image, AudioManager audio){
		super(x, y, id, map, team, image);
		this.audio = audio;
		angle = Math.toRadians(90);
		movementAngle = 0;
		right = true;
		rb = new RandomBehaviour(this);
		this.audio = audio;

	}

	/**
	 * Tick is called every frame
	 * It updates the player location and angle, and shoots bullets if the shoot button is pressed
	 */
	@Override
	public void tick() {
		rb.tick();
		handlePropWallCollision();
		if(!eliminated){
			updatePosition();
			updateAngle();
			updateShooting();
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

//		if(up && !collUp) y -= movementSpeed;
//		if(down && !collDown) y += movementSpeed;
//		if(left  && !collLeft) x -= movementSpeed;
//		if(right  && !collRight) x += movementSpeed;
		
		if(collUp || collLeft || collRight || collDown) rb.change();
		
		double yToReduce = movementSpeed * Math.cos(movementAngle);
		double xToAdd = movementSpeed * Math.sin(movementAngle);

		if((yToReduce > 0 && !collUp) || (yToReduce < 0 && !collDown )) setLayoutY(getLayoutY() - yToReduce);
		if((xToAdd > 0 && !collRight) || (xToAdd < 0 && !collLeft ) ) setLayoutX(getLayoutX() + xToAdd);
	}


	//Calculates the angle the player is facing with respect to the mouse
	@Override
	protected void updateAngle(){
//		if(collUp){
//			y += movementSpeed;
//		} else if(collDown) {
//			y -= movementSpeed;
//		} else if(collLeft) {
//			x += movementSpeed;
//		} else if(collRight) {
//			x -= movementSpeed;
//		}
		rotation.setAngle(Math.toDegrees(angle));
	}
	
	public void setMovementAngle(double angle){
		this.movementAngle = angle;
	}

}
