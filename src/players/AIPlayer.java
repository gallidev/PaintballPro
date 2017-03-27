package players;

import ai.BehaviourManager;
import ai.HashMapGen;
import enums.GameMode;
import enums.TeamEnum;
import logic.server.Team;
import physics.CollisionsHandler;
import rendering.ImageFactory;
import rendering.Map;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class AIPlayer.
 */
public class AIPlayer extends EssentialPlayer{

	/** The b manager. */
	private BehaviourManager bManager;

	/** The hash maps. */
	private HashMapGen hashMaps;

	/** The movement angle. */
	private double movementAngle;

	/** The opp team. */
	private Team oppTeam;

	/** The my team. */
	private Team myTeam;

	/** The moving. */
	private boolean moving;

	/** The map. */
	private Map map;


	/**
	 * Instantiates a new AI player.
	 *
	 * @param x the x
	 * @param y the y
	 * @param id the id
	 * @param map the map
	 * @param team the team
	 * @param collisionsHandler the collisions handler
	 * @param hashMaps the hash maps
	 * @param mode the mode
	 * @param currentFPS the current FPS
	 */
	public AIPlayer(double x, double y, int id, Map map, TeamEnum team, CollisionsHandler collisionsHandler, HashMapGen hashMaps, GameMode mode, double currentFPS){
		super(x, y, id, map.getSpawns(), team, collisionsHandler, ImageFactory.getPlayerImage(team), mode, currentFPS);
		this.hashMaps = hashMaps;
		angle = Math.toRadians(90);
		movementAngle = 0;
		right = true;
		this.map = map;
		this.moving = true;
		bManager = new BehaviourManager(this);
	}

	/**
	 * Tick is called every frame
	 * It updates the player location and angle, and shoots bullets if the shoot button is pressed.
	 */
	@Override
	public void tick() {
		bManager.tick();
		cleanBullets();
		collisionsHandler.handlePropWallCollision(this);
		collisionsHandler.handleFlagCollision(this);
		if(!eliminated){
			collisionsHandler.handlePowerUpCollision(this);
			lastX = getLayoutX();
			lastY = getLayoutY();
			lastAngle = angle;
			updatePosition();
			updateAngle();
			updateShooting();
		} else {
			checkSpawn();
		}
		updatePlayerBounds();
		updateBullets();
		handlePowerUp();

		if(!invincible){
			collisionsHandler.handleBulletCollision(this);
		} else {
			checkInvincibility();
		}
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updatePosition()
	 */
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

		if (gameMode == GameMode.TEAM_MATCH)
			oppTeam.incrementScore();

		scoreChanged = true;
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updateAngle()
	 */
	@Override
	protected void updateAngle(){
		rotation.setAngle(Math.toDegrees(angle));
	}

	/**
	 * Gets the movement angle.
	 *
	 * @return the movement angle
	 */
	public double getMovementAngle() { return this.movementAngle;}

	/**
	 * Sets the movement angle.
	 *
	 * @param angle the new movement angle
	 */
	public void setMovementAngle(double angle){
		this.movementAngle = angle;
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updateShooting()
	 */
	protected void updateShooting(){
		if(shoot && shootTimer < System.currentTimeMillis() - SHOOT_DELAY){
			shoot();
			shootTimer = System.currentTimeMillis();
		}
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#setOppTeam(logic.server.Team)
	 */
	public void setOppTeam(Team oppTeam){
		this.oppTeam = oppTeam;
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#setMyTeam(logic.server.Team)
	 */
	public void setMyTeam(Team t){
		myTeam = t;
	}

	/**
	 * Sets the moving.
	 *
	 * @param b the new moving
	 */
	public void setMoving(boolean b) { this.moving = b;}

	/**
	 * Gets the map.
	 *
	 * @return the map
	 */
	public Map getMap(){
		return map;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public double getWidth(){
		return ImageFactory.getPlayerImage(TeamEnum.RED).getWidth();
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public double getHeight(){
		return ImageFactory.getPlayerImage(TeamEnum.RED).getHeight();
	}


	/**
	 * Gets the hash maps.
	 *
	 * @return the hash maps
	 */
	public HashMapGen getHashMaps(){
		return this.hashMaps;
	}

	/**
	 * Gets the enemies.
	 *
	 * @return the enemies
	 */
	public ArrayList<EssentialPlayer> getEnemies() {
		return oppTeam.getMembers();
	}

	/**
	 * Gets the team players.
	 *
	 * @return the team players
	 */
	public ArrayList<EssentialPlayer> getTeamPlayers() {
		return myTeam.getMembers();
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updateRotation(double)
	 */
	@Override
	public void updateRotation(double angleRotation) {

	}

	/**
	 * Gets the behaviour manager.
	 *
	 * @return the behaviour manager
	 */
	public BehaviourManager getBehaviourManager(){
		return bManager;
	}

}
