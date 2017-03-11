package players;

import java.util.ArrayList;

import ai.BehaviourManager;
import enums.TeamEnum;
import javafx.scene.image.Image;
import oldCode.offlineLogic.OfflineTeam;
import physics.CollisionsHandler;
import rendering.ImageFactory;
import rendering.Map;
import rendering.Spawn;
import serverLogic.Team;
import ai.HashMapGen;
import audio.AudioManager;
import rendering.Renderer;

public class AIPlayer extends EssentialPlayer{

	private BehaviourManager bManager;
	private HashMapGen hashMaps;
	private double movementAngle;
	private Team oppTeam;
	private Team myTeam;
	private boolean moving;
	private Map map;
	//private AudioManager audio;
	private ArrayList<EssentialPlayer> enemies;
	private ArrayList<EssentialPlayer> teamPlayers;


	public AIPlayer(double x, double y, int id, Map map, TeamEnum team, CollisionsHandler collisionsHandler, HashMapGen hashMaps){
		super(x, y, id, map.getSpawns(), team, collisionsHandler, ImageFactory.getPlayerImage(team));
		this.hashMaps = hashMaps;
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
		Renderer.incrementScore(oppTeam.getColour(), 1);


//		if (myTeam.getColour() == TeamEnum.RED){
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

	protected void updateShooting(){
		if(shoot && shootTimer < System.currentTimeMillis() - shootDelay){
			shoot();
			shootTimer = System.currentTimeMillis();
		}
	}

	//public Map getMap() {return this.map;}

	public void setOppTeam(Team oppTeam2){
		oppTeam = oppTeam2;
	}

	public void setMyTeam(Team t){
		myTeam = t;
	}

	public void setMoving(boolean b) { this.moving = b;}

	public Map getMap(){
		return map;
	}

	public double getWidth(){
		return ImageFactory.getPlayerImage(TeamEnum.RED).getWidth();
	}

	public double getHeight(){
		return ImageFactory.getPlayerImage(TeamEnum.RED).getHeight();
	}


	public HashMapGen getHashMaps(){
		return this.hashMaps;
	}

	public ArrayList<EssentialPlayer> getEnemies() {
		return enemies;
	}

	public void setEnemies(ArrayList<EssentialPlayer> enemies) {
		this.enemies = enemies;
	}

	public ArrayList<EssentialPlayer> getTeamPlayers() {
		return teamPlayers;
	}

	public void setTeamPlayers(ArrayList<EssentialPlayer> teamPlayers) {
		this.teamPlayers = teamPlayers;
	}


}
