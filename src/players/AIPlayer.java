package players;

import java.util.ArrayList;

import ai.BehaviourManager;
import enums.TeamEnum;
import javafx.scene.image.Image;
import offlineLogic.OfflineTeam;
import physics.CollisionsHandler;
import rendering.ImageFactory;
import rendering.Map;
import rendering.Spawn;
import serverLogic.Team;

public class AIPlayer extends ServerMinimumPlayer{

	private BehaviourManager bManager;
	//private AudioManager audio;
	private double movementAngle;
	private Team oppTeam;
	private Team myTeam;
	private boolean moving;
	private Map map;



	public AIPlayer(double x, double y, int id, Map map, TeamEnum team, CollisionsHandler collisionsHandler){
		super(x, y, id, map.getSpawns(), team, collisionsHandler, ImageFactory.getPlayerImage(team));

		//this.audio = audio;
		angle = Math.toRadians(90);
		movementAngle = 0;
		right = true;
		this.map = map;
		this.moving = true;
		bManager = new BehaviourManager(this);
		//this.audio = audio;
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
//			if (collUp) setLayoutY(getLayoutY() + 1);
//			if (collDown) setLayoutY(getLayoutY() - 1);
//			if (collLeft) setLayoutX(getLayoutX() + 1);
//			if (collRight) setLayoutX(getLayoutX() - 1);

		}
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

	public double getMovementAngle() { return this.movementAngle;}

	public void setMovementAngle(double angle){
		this.movementAngle = angle;
	}

	//public Map getMap() {return this.map;}

	public void setOppTeam(Team t){
		oppTeam = t;
	}

	public void setMyTeam(Team t){
		myTeam = t;
	}

	public void setMoving(boolean b) { this.moving = b;}
	
	public Map getMap(){
		return map;
	}
	
	public ArrayList<ServerMinimumPlayer> getEnemies(){
		return oppTeam.getMembers();
	}
	
	public double getWidth(){
		return ImageFactory.getPlayerImage(TeamEnum.RED).getWidth();
	}

	public double getHeight(){
		return ImageFactory.getPlayerImage(TeamEnum.RED).getHeight();
	}
	
}
