package players;


import enums.GameMode;
import enums.TeamEnum;
import gui.GUIManager;
import javafx.scene.image.Image;
import physics.CollisionsHandler;
import rendering.Spawn;
import serverLogic.Team;
/**
 *  The player, represented by an ImageView
 */
public class UserPlayer extends EssentialPlayer{

	private Team oppTeam;
	private Team myTeam;
	
	/* For testing purposes */
	public static boolean isTicked = false;

	public UserPlayer(double x, double y, int id, Spawn[] spawn, TeamEnum team,
			CollisionsHandler collisionsHandler, Image image, GameMode game, double currentFPS) {
		super(x, y, id, spawn, team, collisionsHandler, image, game, currentFPS);
	}

	public void tick()
	{
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

	protected void updatePosition()
	{
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

	protected void updateShooting(){
		if(shoot && shootTimer < System.currentTimeMillis() - SHOOT_DELAY){
			shoot();
			shootTimer = System.currentTimeMillis();
		}
	}


	@Override
	public void updateScore() {
		if (gameMode == GameMode.ELIMINATION)
			oppTeam.incrementScore();

		scoreChanged = true;
	}

	@Override
	public void setMyTeam(Team team) {
		this.myTeam = team;

	}

	@Override
	public void setOppTeam(Team oppTeam) {
		this.oppTeam = oppTeam;
	}

	public void updateRotation(double angle){
		this.angle = angle;
		rotation.setAngle(Math.toDegrees(angle));
	}


}

