package ai;
import audio.AudioManager;
import javafx.scene.image.Image;
import physics.GeneralPlayer;
import rendering.*;
import enums.TeamEnum;

import static gui.GUIManager.bluePlayerImage;
import static gui.GUIManager.redPlayerImage;

public class AIPlayer extends GeneralPlayer{

	private RandomBehaviour rb;
	private AudioManager audio;
	private double movementAngle;
	private Map map;


	public AIPlayer(double x, double y, int id, Map map, TeamEnum team, AudioManager audio){
		super(x, y, id, map, team, team == TeamEnum.RED ? redPlayerImage : bluePlayerImage);
		this.audio = audio;
		angle = Math.toRadians(90);
		movementAngle = 0;
		right = true;
		rb = new RandomBehaviour(this);
		this.audio = audio;
		this.map = map;
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
		if(collUp || collLeft || collRight || collDown) rb.change();

		double yToReduce = movementSpeed * Math.cos(movementAngle);
		double xToAdd = movementSpeed * Math.sin(movementAngle);

		if((yToReduce > 0 && !collUp) || (yToReduce < 0 && !collDown )) setLayoutY(getLayoutY() - yToReduce);
		if((xToAdd > 0 && !collRight) || (xToAdd < 0 && !collLeft ) ) setLayoutX(getLayoutX() + xToAdd);
	}

	@Override
	protected void updateAngle(){
		rotation.setAngle(Math.toDegrees(angle));
	}

	public void setMovementAngle(double angle){
		this.movementAngle = angle;
	}

	public Map getMap(){
		return this.map;
	}

}
