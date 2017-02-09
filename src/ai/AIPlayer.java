package ai;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import logic.GameObject;
import physics.Bullet;
import physics.GeneralPlayer;
import physics.Player;
import rendering.*;
import java.util.ArrayList;
import java.util.List;
import enums.Teams;


public class AIPlayer extends GeneralPlayer{

	private RandomBehaviour rb;

	public AIPlayer(double x, double y, String nickname, Renderer scene, Teams team, Image image){
		super(x, y, nickname, scene, team, image);
		angle = Math.toRadians(90);
		right = true;
		rb = new RandomBehaviour(this);
	}

	/**
	 * Tick is called every frame
	 * It updates the player location and angle, and shoots bullets if the shoot button is pressed
	 */
	@Override
	public void tick() {
		rb.tick();
		if(!eliminated){
			updateAngle();
			updatePosition();
			updateShooting();
		} else {
			checkSpawn();
		}
		updatePlayerBounds();
		updateBullets();
		handlePropCollision();
		handleWallCollision();
		if(!invincible){
			handleBulletCollision();
		} else {
			checkInvincibility();
		}
		
	}

	@Override
	protected void updatePosition(){
		y -= 2 * Math.cos(angle);
		x += 2 * Math.sin(angle);

		setLayoutX(x);
		setLayoutY(y);
	}


	//Calculates the angle the player is facing with respect to the mouse
	@Override
	protected void updateAngle(){
		rotation.setAngle(Math.toDegrees(angle));
	}

}
