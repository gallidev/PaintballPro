<<<<<<< HEAD:src/ai/AIPlayer.java
package ai;
import static gui.GUIManager.bluePlayerImage;
import static gui.GUIManager.redPlayerImage;

import java.util.ArrayList;

import audio.AudioManager;
import enums.TeamEnum;
import logic.GameMode;
import logic.OfflineGameMode;
import logic.OfflineTeam;
import physics.GeneralPlayer;
import rendering.Map;
=======
package players;
import audio.AudioManager;
import javafx.scene.image.Image;
import rendering.*;
import enums.TeamEnum;

import static gui.GUIManager.bluePlayerImage;
import static gui.GUIManager.redPlayerImage;

import ai.RandomBehaviour;
>>>>>>> 32ad1e201d00ce6a669921a8fafd3a8134a47d0d:src/players/AIPlayer.java

public class AIPlayer extends GeneralPlayer{

	private RandomBehaviour rb;
	private AudioManager audio;
	private double movementAngle;
	private Map map;
	private OfflineTeam oppTeam;
	private OfflineTeam myTeam;


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
	

	public void updateScore(){
		
		oppTeam.incrementScore();
		System.out.println( "My team score: " + myTeam.getScore());
		System.out.println( "Opp team score: " + oppTeam.getScore());
		
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
	
	public void setOppTeam(OfflineTeam t){
		oppTeam = t;
	}
	
	public void setMyTeam(OfflineTeam t){
		myTeam = t;
	}
}
