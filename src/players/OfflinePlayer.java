package players;

import static gui.GUIManager.bluePlayerImage;
import static gui.GUIManager.redPlayerImage;

import java.util.ArrayList;

import audio.AudioManager;
import enums.TeamEnum;
import javafx.geometry.Point2D;
import offlineLogic.OfflineTeam;
import physics.Bullet;
import physics.CollisionsHandler;
import rendering.Map;

/**
 * The player, represented by an ImageView that should be running
 */
public class OfflinePlayer extends GeneralPlayer
{

	private double mx, my;
	private boolean controlScheme;
	private AudioManager audio;
	private OfflineTeam myTeam;
	private OfflineTeam oppTeam;


	/**
	 * Create a new player at the set location, and adds the rotation property to the player
	 *
	 * @param x             The x-coordinate of the player with respect to the map
	 * @param y             The y-coordinate of the player with respect to the map
	 * @param controlScheme True - movement with respect to cursor location, False - movement with respect to global position
	 * @param scene         The scene in which the player will be displayed
	 */
	public OfflinePlayer(double x, double y, int id, boolean controlScheme, Map map, AudioManager audio, TeamEnum team, CollisionsHandler collisionsHandler)
	{
		super(x, y, id, map, team, team == TeamEnum.RED ? redPlayerImage : bluePlayerImage, audio, collisionsHandler);
		this.audio = audio;
		this.mx = x;
		this.my = y;
		this.controlScheme = controlScheme;
		angle = 0.0;
		this.team = team;
		teamPlayers = new ArrayList<>();
		enemies = new ArrayList<>();

		
//		for(AIPlayer p : myTeam.getMembers()){
//			p.setOppTeam(oppTeam);
//			p.setMyTeam(myTeam);
//		}
//
//		for(AIPlayer p : oppTeam.getMembers()){
//			p.setOppTeam(myTeam);
//			p.setMyTeam(oppTeam);
//		}

	}

	public OfflinePlayer(double x, double y, int id, TeamEnum team)
	{
		super(x, y, id, team == TeamEnum.RED ? redPlayerImage : bluePlayerImage);
		controlScheme = false;
		this.team = team;
	}


	/**
	 * Tick is called every frame
	 * It updates the player location and angle, and shoots bullets if the shoot button is pressed
	 */
	@Override
	public void tick()
	{
		// handle the collisions with walls and props before moving the position
		// of the player so to understand if he can move or not in a specific direction
		collisionsHandler.handlePropWallCollision(this);
		//System.out.println("Collisionssss up : " + collUp);
		if(!eliminated)
		{
			updatePosition();
			updateShooting();
			updateAngle();
		}
		else
		{
			checkSpawn();
		}
		updatePlayerBounds();
		updateBullets();

		if(!invincible)
		{
			collisionsHandler.handleBulletCollision(this);
		}
		else
		{
			checkInvincibility();
		}
	}

	/**
	 * Updates the score of the opponent team when the current player has been eliminated.
	 * @author atp575
	 */
	@Override
	public void updateScore() {

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
	protected void updatePosition()
	{
		if(controlScheme)
		{
			if(up)
			{
				setLayoutY(getLayoutY() - movementSpeed * Math.cos(angle));
				setLayoutX(getLayoutX() + movementSpeed * Math.sin(angle));
			}
			if(down)
			{
				setLayoutY(getLayoutY() + movementSpeed * Math.cos(angle));
				setLayoutX(getLayoutX() - movementSpeed * Math.sin(angle));
			}
			if(left)
			{
				setLayoutY(getLayoutY() - movementSpeed * Math.cos(angle - Math.PI / 2));
				setLayoutX(getLayoutX() + movementSpeed * Math.sin(angle - Math.PI / 2));
			}
			if(right)
			{
				setLayoutY(getLayoutY() - movementSpeed * Math.cos(angle + Math.PI / 2));
				setLayoutX(getLayoutX() + movementSpeed * Math.sin(angle + Math.PI / 2));
			}
		}
		else
		{
			//System.out.println("collup: " + collUp + " collDown:" + collDown + " collLeft:" + collLeft + " collRight: " + collRight );

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
	}



	//Calculates the angle the player is facing with respect to the mouse
	@Override
	protected void updateAngle()
	{
		Point2D temp = this.localToScene(1.65 * playerHeadX, playerHeadY);
		double x1 = temp.getX();
		double y1 = temp.getY();

		double deltax = mx - x1;
		double deltay = y1 - my;
		angle = Math.atan2(deltax, deltay);
		rotation.setAngle(Math.toDegrees(angle));
	}


	public void shoot()
	{

		double x1 = (83 * getImage().getWidth() / 120) - playerHeadX;
		double y1 = (12 * getImage().getHeight() / 255) - playerHeadY;

		double x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
		double y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);

		double bulletX = getLayoutX() + x2 + playerHeadX;
		double bulletY = getLayoutY() + y2 + playerHeadY;

		Bullet bullet = new Bullet(bulletX, bulletY, angle, team);
		audio.playSFX(audio.sfx.getRandomPaintball(), (float) 1.0);
		firedBullets.add(bullet);
	}


	public void setMX(double mx)
	{
		this.mx = mx;
	}

	public void setMY(double my)
	{
		this.my = my;
	}

	public void setAudio(AudioManager audio)
	{
		this.audio = audio;
	}


}