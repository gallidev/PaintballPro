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


/**
 * This class represents an Ai player that could run on server or client simulation,
 * depending on single player or multiplayer mode.
 *
 * @author Filippo Galli
 * @author Sivarjuen Ravichandran
 *
 */
public class AIPlayer extends EssentialPlayer{

	/** The behaviour manager. */
	private BehaviourManager bManager;

	/** The hash maps for finding the shortest paths. */
	private HashMapGen hashMaps;

	/** The movement angle. */
	private double movementAngle;

	/** The opp team. */
	private Team oppTeam;

	/** The my team. */
	private Team myTeam;

	/** Is the player moving. */
	private boolean moving;

	/** The map. */
	private Map map;


	/**
	 * Instantiates a new AI player.
	 *
	 * @param x the x position of the player
	 * @param y the y position of the player
	 * @param id the id of the player
	 * @param map the map in which the player is playing
	 * @param team the team of the player
	 * @param collisionsHandler the collisions handler of the game simulation
	 * @param hashMaps the hash maps with the shortest paths of the map
	 * @param mode the mode
	 * @param currentFPS the current FPS in which the simulation is running on.
	 */
	public AIPlayer(double x, double y, int id, Map map, TeamEnum team, CollisionsHandler collisionsHandler,
			HashMapGen hashMaps, GameMode mode, double currentFPS){
		super(x, y, id, map.getSpawns(), team, collisionsHandler, ImageFactory.getPlayerImage(team), mode, currentFPS);
		this.hashMaps = hashMaps;
		angle = Math.toRadians(90);
		movementAngle = 0;
		right = true;
		this.map = map;
		this.moving = true;
		bManager = new BehaviourManager(this);
	}


	/* (non-Javadoc)
	 * @see players.EssentialPlayer#tick()
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
	 *
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
		if(shoot && shootTimer < System.currentTimeMillis() - shootDelay){
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
