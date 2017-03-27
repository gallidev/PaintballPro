package players;


import enums.GameMode;
import enums.TeamEnum;
import gui.GUIManager;
import javafx.scene.image.Image;
import logic.server.Team;
import physics.CollisionsHandler;
import rendering.Spawn;

/**
 *  The client player representation that runs on the server and gets the input updates from all the clients.
 *
 * @author Filippo Galli
 */
public class UserPlayer extends EssentialPlayer{

	/** The is ticked. */
	/* For testing purposes */
	public static boolean isTicked = false;

	/** The counter frame. */
	int counterFrame;

	/** The opp team. */
	private Team oppTeam;

	/** The mouse X and Y positions. */
	double mouseX, mouseY;

	/**
	 * Instantiates a new user player.
	 *
	 * @param x the x position of the player
	 * @param y the y position of the player
	 * @param id the id of the player
	 * @param spawn the spawn locations of the player
	 * @param team the team of the player
	 * @param collisionsHandler the collisions handler of the game simulation
	 * @param image the image of the player
	 * @param mode the mode
	 * @param currentFPS the current FPS in which the simulation is running on.
	 */
	public UserPlayer(double x, double y, int id, Spawn[] spawn, TeamEnum team,
			CollisionsHandler collisionsHandler, Image image, GameMode game, double currentFPS) {
		super(x, y, id, spawn, team, collisionsHandler, image, game, currentFPS);
		counterFrame = 0;
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#tick()
	 */
	public void tick()
	{
		cleanBullets();
		// handle the collisions with walls and props before moving the position
		// of the player so to understand if he can move or not in a specific direction

		collisionsHandler.handlePropWallCollision(this);
		collisionsHandler.handleFlagCollision(this);
		if(!eliminated)
		{
			collisionsHandler.handlePowerUpCollision(this);
			lastX = getLayoutX();
			lastY = getLayoutY();
			lastAngle = angle;
			updatePosition();
			updateShooting();
			//updateAngle();
		}
		else
		{
			checkSpawn();
		}

		updatePlayerBounds();
		updateBullets();
		handlePowerUp();

		if(!invincible)
		{
			collisionsHandler.handleBulletCollision(this);
		}
		else
		{
			checkInvincibility();
		}

		isTicked = true;
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updatePosition()
	 */
	protected void updatePosition()
	{
		//System.out.println("position :" + getLayoutX() + " " + getLayoutY());
		if(up && !collUp){
			setLayoutY(getLayoutY() - movementSpeed);
		}else if(!up && collUp){
			setLayoutY(getLayoutY() + movementSpeed);
		}
		if(down && !collDown){
			setLayoutY(getLayoutY() + movementSpeed);
		}else if(!down && collDown){
			setLayoutY(getLayoutY() - movementSpeed);
		}
		if(left && !collLeft) {
			setLayoutX(getLayoutX() - movementSpeed);
		} else if(!left && collLeft){
			setLayoutX(getLayoutX() + movementSpeed);
		}
		if(right && !collRight){
			setLayoutX(getLayoutX() + movementSpeed);
		}else if (!right && collRight){
			setLayoutX(getLayoutX() - movementSpeed);
		}
	}


	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updateAngle()
	 */
	//Calculates the angle the player is facing with respect to the mouse
	@Override
	protected void updateAngle()
	{
		String[] resolution = GUIManager.getUserSettings().getResolution().split("x");
		double screenWidth = Double.parseDouble(resolution[0]);
		double screenHeight = Double.parseDouble(resolution[1]);

		double deltax = mouseX  - screenWidth/2;
		double deltay = screenHeight/2 + PLAYER_HEAD_Y /6 -  mouseY ;
		angle = Math.atan2(deltax, deltay);
		double degrees = Math.toDegrees(angle);
		rotation.setAngle(degrees);
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
	 * @see players.EssentialPlayer#updateScore()
	 */
	@Override
	public void updateScore() {
		if (gameMode == GameMode.TEAM_MATCH)
			oppTeam.incrementScore();

		scoreChanged = true;
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#setMyTeam(logic.server.Team)
	 */
	@Override
	public void setMyTeam(Team team) {
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#setOppTeam(logic.server.Team)
	 */
	@Override
	public void setOppTeam(Team oppTeam) {
		this.oppTeam = oppTeam;
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updateRotation(double)
	 */
	public void updateRotation(double angle){
		this.angle = angle;
		rotation.setAngle(Math.toDegrees(angle));
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#getCounterFrame()
	 */
	public int getCounterFrame() {
		return counterFrame;
	}

	/**
	 * Sets the counter frame.
	 *
	 * @param counterFrame the new counter frame
	 */
	public void setCounterFrame(int counterFrame) {
		this.counterFrame = counterFrame;
	}

}

