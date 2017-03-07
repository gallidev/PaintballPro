package players;

import ai.BehaviourManager;
import audio.AudioManager;
import enums.Team;
import offlineLogic.OfflineTeam;
import physics.CollisionsHandler;
import rendering.ImageFactory;
import rendering.Map;
import rendering.Renderer;

public class AIPlayer extends GeneralPlayer{

	private BehaviourManager bManager;
	private AudioManager audio;
	private double movementAngle;
	private OfflineTeam oppTeam;
	private OfflineTeam myTeam;
	private boolean moving;


	public AIPlayer(double x, double y, int id, Map map, Team team, AudioManager audio, CollisionsHandler collisionsHandler){
		super(x, y, id, map, team, ImageFactory.getPlayerImage(team), audio, collisionsHandler);
		this.audio = audio;
		angle = Math.toRadians(90);
		movementAngle = 0;
		right = true;
		this.map = map;
		this.moving = true;
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

		collisionsHandler.handlePropWallCollision(this);
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
			collisionsHandler.handleBulletCollision(this);
		} else {
			checkInvincibility();
		}

	}

	@Override
	protected void updatePosition(){
		if(collUp || collLeft || collRight || collDown) bManager.change();

		double yToReduce = movementSpeed * Math.cos(movementAngle);
		double xToAdd = movementSpeed * Math.sin(movementAngle);

		if(moving) {
			if ((yToReduce > 0 && !collUp) || (yToReduce < 0 && !collDown)) setLayoutY(getLayoutY() - yToReduce);
			if ((xToAdd > 0 && !collRight) || (xToAdd < 0 && !collLeft)) setLayoutX(getLayoutX() + xToAdd);
		}
	}


	/**
	 * Updates the opponent team score, when the current player has been eliminated.
	 * @author atp575
	 */
	public void updateScore(){
		oppTeam.incrementScore();
		Renderer.incrementScore(oppTeam.getColour());

//		if (myTeam.getColour() == Team.RED){
//			System.out.println( "Red team score: " + myTeam.getScore());
//			System.out.println( "Blue team score: " + oppTeam.getScore());
//		}
//		else{
//			System.out.println( "Blue team score: " + myTeam.getScore());
//			System.out.println( "Red team score: " + oppTeam.getScore());
//		}


	}

	@Override
	protected void updateAngle(){
		rotation.setAngle(Math.toDegrees(angle));
	}

	public double getMovementAngle() { return this.movementAngle;}

	public void setMovementAngle(double angle){
		this.movementAngle = angle;
	}

	public Map getMap() {return this.map;}

	public void setOppTeam(OfflineTeam t){
		oppTeam = t;
	}

	public void setMyTeam(OfflineTeam t){
		myTeam = t;
	}

	public void setMoving(boolean b) { this.moving = b;}
}
