package players;

import ai.HashMapGen;
import audio.AudioManager;
import enums.GameMode;
import enums.TeamEnum;
import gui.GUIManager;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import logic.server.Team;
import physics.CollisionsHandler;
import physics.InputHandler;
import physics.Pellet;
import rendering.ImageFactory;
import rendering.Map;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

// TODO: Auto-generated Javadoc
/**
 * The player, represented by an ImageView that should be running.
 */
public class OfflinePlayer extends EssentialPlayer
{

	/** The my team. */
	//game information
	private Team myTeam;

	/** The opp team. */
	private Team oppTeam;

	/** The audio. */
	private AudioManager audio;

	/** The input handler. */
	private InputHandler inputHandler;

	/** The name tag. */
	private Label nameTag;

	/** The rand. */
	private Random rand;

	/**
	 * Create a new player at the set location, and adds the rotation property to the player.
	 *
	 * @author Alexandra Paduraru
	 * @param x             The x-coordinate of the player with respect to the map
	 * @param y             The y-coordinate of the player with respect to the map
	 * @param id the id
	 * @param map the map
	 * @param guiManager the gui manager
	 * @param team the team
	 * @param collisionsHandler the collisions handler
	 * @param inputHandler the input handler
	 * @param mode the mode
	 * @param currentFPS the current FPS
	 */
	public OfflinePlayer(double x, double y, int id, Map map, GUIManager guiManager, TeamEnum team, CollisionsHandler collisionsHandler, InputHandler inputHandler, GameMode mode, double currentFPS)
	{
		super(x, y, id, map.getSpawns(), team, collisionsHandler, ImageFactory.getPlayerImage(team), mode, currentFPS);
		this.audio = guiManager.getAudioManager();
		this.inputHandler = inputHandler;
		this.team = team;

		angle = 0.0;
		rand = new Random();

		nameTag = new Label("Player");
		nameTag.setStyle("-fx-background-color: rgba(64, 64, 64, 0.75);" +
				"-fx-font-size: 10pt; -fx-text-fill: white");
		nameTag.setPadding(new Insets(5));
		nameTag.relocate(x - 15, y - 32);
		HashMapGen hashMaps = new HashMapGen(map);

		//populating the players team and creating a corresponding OfflineTeam for the members
		//ArrayList<EssentialPlayer> myTeamMembers = new ArrayList<EssentialPlayer>();
		myTeam = new Team(team);

		myTeam.addMember(this);
		for(int i = 1; i < 4; i++){
			AIPlayer p = new AIPlayer(map.getSpawns()[i].x * 64, map.getSpawns()[i].y * 64, i, map, team, collisionsHandler, hashMaps, map.getGameMode(), currentFPS);
			myTeam.addMember(p);
		}

		//populating the opponent team and creating a corresponding OfflineTeam for the members
		//ArrayList<AIPlayer> oppTeamMembers = new ArrayList<>();

		oppTeam = new Team(team == TeamEnum.RED ? TeamEnum.BLUE : TeamEnum.RED);


		for (int i = 0; i < 4; i++){
				AIPlayer p = new AIPlayer(map.getSpawns()[i+4].x * 64, map.getSpawns()[i+4].y * 64, i + 4, map, team == TeamEnum.RED ? TeamEnum.BLUE : TeamEnum.RED, collisionsHandler, hashMaps, gameMode, currentFPS);
				oppTeam.addMember(p);
		}

		setPlayerNames();

		for(EssentialPlayer p : myTeam.getMembers()){
			p.setOppTeam(oppTeam);
			p.setMyTeam(myTeam);
		}

		for(EssentialPlayer p : oppTeam.getMembers()){
			p.setOppTeam(myTeam);
			p.setMyTeam(oppTeam);
		}

		collisionsHandler.setRedTeam(myTeam);
		collisionsHandler.setBlueTeam(oppTeam);

	}

	/**
	 * Tick is called every frame
	 * It updates the player location and angle, and shoots bullets if the shoot button is pressed.
	 */
	@Override
	public void tick()
	{
		cleanBullets();
		// handle the collisions with walls and props before moving the position
		// of the player so to understand if he can move or not in a specific direction
		collisionsHandler.handlePropWallCollision(this);
		//System.out.println("Collisionssss up : " + collUp);
		collisionsHandler.handleFlagCollision(this);
		if(!eliminated)
		{
			collisionsHandler.handlePowerUpCollision(this);
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
		handlePowerUp();

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
	 * @author Alexandra Paduraru
	 */
	@Override
	public void updateScore() {
		if (gameMode == GameMode.TEAM_MATCH)
			oppTeam.incrementScore();

		scoreChanged = true;
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updatePosition()
	 */
	@Override
	protected void updatePosition()
	{
		//System.out.println("collup: " + collUp + " collDown:" + collDown + " collLeft:" + collLeft + " collRight: " + collRight );

		if(inputHandler.isUp() && !collUp){
			setLayoutY(getLayoutY() - movementSpeed);
		}else if(!inputHandler.isUp() && collUp){
			setLayoutY(getLayoutY() + movementSpeed);
		}
		if(inputHandler.isDown() && !collDown){
			setLayoutY(getLayoutY() + movementSpeed);
		}else if(!inputHandler.isDown() && collDown){
			setLayoutY(getLayoutY() - movementSpeed);
		}
		if(inputHandler.isLeft() && !collLeft) {
			setLayoutX(getLayoutX() - movementSpeed);
		} else if(!inputHandler.isLeft() && collLeft){
			setLayoutX(getLayoutX() + movementSpeed);
		}
		if(inputHandler.isRight() && !collRight){
			setLayoutX(getLayoutX() + movementSpeed);
		}else if (!inputHandler.isRight() && collRight){
			setLayoutX(getLayoutX() - movementSpeed);
		}

	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updateShooting()
	 */
	protected void updateShooting(){
		if(inputHandler.isShooting() && shootTimer < System.currentTimeMillis() - shootDelay){
			shoot();
			shootTimer = System.currentTimeMillis();
		}
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updateAngle()
	 */
	//Calculates the angle the player is facing with respect to the mouse
	@Override
	protected void updateAngle()
	{
		Point2D temp = this.localToScene(1.65 * PLAYER_HEAD_X, PLAYER_HEAD_Y);
		double x1 = temp.getX();
		double y1 = temp.getY();

		double deltax = inputHandler.getMouseX() - x1;
		double deltay = y1 - inputHandler.getMouseY();
		angle = Math.atan2(deltax, deltay);
		rotation.setAngle(Math.toDegrees(angle));
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#shoot()
	 */
	public void shoot()
	{
		double x1 = (83 * getImage().getWidth() / 120) - PLAYER_HEAD_X;
		double y1 = (12 * getImage().getHeight() / 255) - PLAYER_HEAD_Y;

		double x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
		double y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);

		double bulletX = getLayoutX() + x2 + PLAYER_HEAD_X;
		double bulletY = getLayoutY() + y2 + PLAYER_HEAD_Y;

		double bulletAngle = angle;
		boolean sign= rand.nextBoolean();
		double deviation = (double)rand.nextInt(60)/1000;
		if(sign){
			bulletAngle += deviation;
		} else {
			bulletAngle -= deviation;
		}
		Pellet pellet = new Pellet(bulletCounter++, bulletX, bulletY, bulletAngle, team, gameSpeed);
		audio.playSFX(audio.sfx.getRandomPaintball(), (float) 1.0);
		firedPellets.add(pellet);
	}

	/**
	 * Sets the player names.
	 */
	private void setPlayerNames(){
		File names = new File("res/names.txt");
		Scanner readNames;
		try {
			readNames = new Scanner(names);
			for (EssentialPlayer p : myTeam.getMembers()){
				if (p != this)
					p.setNickname(readNames.nextLine());
			}

			for (EssentialPlayer p : oppTeam.getMembers()){
				if (p != this)
					p.setNickname(readNames.nextLine());
			}

			readNames.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Sets the mx.
	 *
	 * @param mx the new mx
	 */
	public void setMX(double mx)
	{
		this.mouseX = mx;
	}

	/**
	 * Sets the my.
	 *
	 * @param my the new my
	 */
	public void setMY(double my)
	{
		this.mouseY = my;
	}

	/**
	 * Gets the my team.
	 *
	 * @return the my team
	 */
	public Team getMyTeam(){
		return this.myTeam;
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#setMyTeam(logic.server.Team)
	 */
	@Override
	public void setMyTeam(Team team) {
		this.myTeam = team;

	}

	/**
	 * Gets the opp team.
	 *
	 * @return the opp team
	 */
	public Team getOppTeam(){
		return this.oppTeam;
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#setOppTeam(logic.server.Team)
	 */
	@Override
	public void setOppTeam(Team team) {
		this.oppTeam = team;

	}


	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updateRotation(double)
	 */
	@Override
	public void updateRotation(double angleRotation) {
		// TODO Auto-generated method stub

	}

}
