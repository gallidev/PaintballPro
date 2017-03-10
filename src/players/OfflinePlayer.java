package players;

import ai.HashMapGen;
import audio.AudioManager;
import enums.TeamEnum;
import gui.GUIManager;
import javafx.geometry.Point2D;
import oldCode.offlineLogic.OfflineTeam;
import physics.Bullet;
import physics.CollisionsHandler;
import rendering.ImageFactory;
import rendering.Map;
import rendering.Renderer;

import java.util.ArrayList;

/**
 * The player, represented by an ImageView that should be running
 */
public class OfflinePlayer extends GeneralPlayer
{
	private AudioManager audio;
	private OfflineTeam myTeam;
	private OfflineTeam oppTeam;
	/**
	 * Create a new player at the set location, and adds the rotation property to the player
	 *
	 * @param x             The x-coordinate of the player with respect to the map
	 * @param y             The y-coordinate of the player with respect to the map
	 */
	public OfflinePlayer(double x, double y, int id, Map map, GUIManager guiManager, TeamEnum team, CollisionsHandler collisionsHandler)
	{
		super(x, y, id, map, team, ImageFactory.getPlayerImage(team), guiManager.getAudioManager(), collisionsHandler);
		this.audio = guiManager.getAudioManager();
		this.mouseX = x;
		this.mouseY = y;
		angle = 0.0;
		this.team = team;
		teamPlayers = new ArrayList<>();
		enemies = new ArrayList<>();
		HashMapGen hashMaps = new HashMapGen(map);

		//populating the players team and creating a corresponding OfflineTeam for the members
		ArrayList<AIPlayer> myTeamMembers = new ArrayList<AIPlayer>();

		for(int i = 1; i < 4; i++){
			AIPlayer p = new AIPlayer(map.getSpawns()[i].x * 64, map.getSpawns()[i].y * 64, i, map, team, audio, collisionsHandler, hashMaps);
			teamPlayers.add(p);
			myTeamMembers.add(p);
		}

		myTeam = new OfflineTeam(myTeamMembers, team);

		//populating the opponent team and creating a corresponding OfflineTeam for the members
		ArrayList<AIPlayer> oppTeamMembers = new ArrayList<>();

		for (int i = 0; i < 4; i++){
				AIPlayer p = new AIPlayer(map.getSpawns()[i+4].x * 64, map.getSpawns()[i+4].y * 64, i + 4, map, team == TeamEnum.RED ? TeamEnum.BLUE : TeamEnum.RED, audio, collisionsHandler, hashMaps);
				oppTeamMembers.add(p);
				enemies.add(p);
		}

		oppTeam = new OfflineTeam(oppTeamMembers, oppTeamMembers.get(0).getTeam());

		for(AIPlayer p : myTeam.getMembers()){
			p.setOppTeam(oppTeam);
			p.setMyTeam(myTeam);
		}

		for(AIPlayer p : oppTeam.getMembers()){
			p.setOppTeam(myTeam);
			p.setMyTeam(oppTeam);
		}

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
		Renderer.incrementScore(oppTeam.getColour(), 1);

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
	protected void updatePosition()
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



	//Calculates the angle the player is facing with respect to the mouse
	@Override
	protected void updateAngle()
	{
		Point2D temp = this.localToScene(1.65 * playerHeadX, playerHeadY);
		double x1 = temp.getX();
		double y1 = temp.getY();

		double deltax = mouseX - x1;
		double deltay = y1 - mouseY;
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

		double bulletAngle = angle;
		boolean sign= rand.nextBoolean();
		double deviation = (double)rand.nextInt(60)/1000;
		if(sign){
			bulletAngle += deviation;
		} else {
			bulletAngle -= deviation;
		}
		Bullet bullet = new Bullet(bulletCounter++, bulletX, bulletY, bulletAngle, team);
		audio.playSFX(audio.sfx.getRandomPaintball(), (float) 1.0);
		firedBullets.add(bullet);
	}


	public void setMX(double mx)
	{
		this.mouseX = mx;
	}

	public void setMY(double my)
	{
		this.mouseY = my;
	}
}
