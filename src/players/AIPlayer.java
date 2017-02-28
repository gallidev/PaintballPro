package players;

import ai.*;
import audio.AudioManager;
import enums.TeamEnum;
import offlineLogic.OfflineTeam;
import rendering.Map;

import static gui.GUIManager.bluePlayerImage;
import static gui.GUIManager.redPlayerImage;

public class AIPlayer extends GeneralPlayer{

	private BehaviourManager bManager;
	private AudioManager audio;
	private double movementAngle;
	private OfflineTeam oppTeam;
	private OfflineTeam myTeam;


	public AIPlayer(double x, double y, int id, Map map, TeamEnum team, AudioManager audio){
		super(x, y, id, map, team, team == TeamEnum.RED ? redPlayerImage : bluePlayerImage, audio);
		this.audio = audio;
		angle = Math.toRadians(90);
		movementAngle = 0;
		right = true;
		this.map = map;
		bManager = new BehaviourManager(this);
		this.audio = audio;
		
	}

	/**
	 * Tick is called every frame
	 * It updates the player location and angle, and shoots bullets if the shoot button is pressed
	 */
	@Override
	public void tick() {
		bManager.tick();
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
		if(collUp || collLeft || collRight || collDown) bManager.change();

		double yToReduce = movementSpeed * Math.cos(movementAngle);
		double xToAdd = movementSpeed * Math.sin(movementAngle);

		if((yToReduce > 0 && !collUp) || (yToReduce < 0 && !collDown )) setLayoutY(getLayoutY() - yToReduce);
		if((xToAdd > 0 && !collRight) || (xToAdd < 0 && !collLeft ) ) setLayoutX(getLayoutX() + xToAdd);
	}
	

	/**
	 * Updates the opponent team score, when the current player has been eliminated.
	 * @author atp575
	 */
	public void updateScore(){
		oppTeam.incrementScore();
		
		if (myTeam.getColour() == TeamEnum.RED){
			System.out.println( "Red team score: " + myTeam.getScore());
			System.out.println( "Blue team score: " + oppTeam.getScore());
		}
		else{
			System.out.println( "Blue team score: " + myTeam.getScore());
			System.out.println( "Red team score: " + oppTeam.getScore());
		}
		
		
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
