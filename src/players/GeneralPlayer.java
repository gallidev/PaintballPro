package players;

import audio.AudioManager;
import enums.TeamEnum;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import logic.GameObject;
import physics.Bullet;
import physics.CollisionsHandler;
import rendering.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *  The player, represented by an ImageView
 */
public abstract class GeneralPlayer extends ServerMinimumPlayer implements GameObject{

	public static final double playerHeadX = 12.5, playerHeadY = 47.5;
	protected static long shootDelay = 450;
	protected static long spawnDelay = 2000;
	protected final double movementSpeed = 2;
	protected boolean up, down, left, right, shoot, eliminated, invincible;
	protected boolean collUp, collDown, collLeft, collRight;
	protected double angle, lastAngle;
	protected ArrayList<Bullet> firedBullets = new ArrayList<Bullet>();
	protected Rotate rotation, boundRotation;
	protected Map map;
	protected int id;
	protected long shootTimer, spawnTimer;
	protected double lastX, lastY;
	protected TeamEnum team;
	protected ArrayList<GeneralPlayer> enemies;
	protected ArrayList<GeneralPlayer> teamPlayers;
	protected Polygon bounds = new Polygon();
	protected ArrayList<Rectangle> propsWalls;
	protected boolean scoreChanged = false;
	protected AudioManager audio;
	protected CollisionsHandler collisionsHandler;
	protected Random rand;
	protected Label nameTag;

	/**
	 * Create a new player at the set location, and adds the rotation property to the player,
	 * this a General class for the Client Side which needs to store the Image
	 * @param x The x-coordinate of the player with respect to the map
	 * @param y The y-coordinate of the player with respect to the map
	 * @param id The id of the player
	 * @param map The map in which the player is playing
	 *
	 */
	public GeneralPlayer(double x, double y, int id, Map map, TeamEnum team, Image image, AudioManager audio, CollisionsHandler collisionsHandler){
		super(y, y, id, map.getSpawns(), team, collisionsHandler, image);
		setLayoutX(x);
		setLayoutY(y);
		this.lastX = x;
		this.lastY = y;
		this.lastAngle = angle;
		this.team = team;
		this.id = id;
		this.rand = new Random();
		rotation = new Rotate(Math.toDegrees(angle), 0, 0, 0, Rotate.Z_AXIS);
	    getTransforms().add(rotation);
		rotation.setPivotX(playerHeadX);
		rotation.setPivotY(playerHeadY);
		this.map = map;
		propsWalls = map.getRecProps();
	    propsWalls.addAll(map.getRecWalls());
		eliminated = false;
		invincible = false;
		this.audio = audio;
		this.collisionsHandler = collisionsHandler;
		createPlayerBounds();
		boundRotation = new Rotate(Math.toDegrees(angle), 0, 0, 0, Rotate.Z_AXIS);
		bounds.getTransforms().add(boundRotation);
		boundRotation.setPivotX(playerHeadX);
		boundRotation.setPivotY(playerHeadY);
		updatePlayerBounds();

		nameTag = new Label("Player");
		nameTag.setStyle("-fx-background-color: rgba(64, 64, 64, 0.75);" +
				"-fx-font-size: 10pt; -fx-text-fill: white");
		nameTag.setPadding(new Insets(5));
		nameTag.relocate(x - 15, y - 32);
	}


	public void beenShot() {
		audio.playSFX(audio.sfx.splat, (float) 1.0);
		spawnTimer = System.currentTimeMillis();
		eliminated = true;
		setVisible(false);
		nameTag.setVisible(false);
		updateScore();
	}


	  public void addTeamPlayer(GeneralPlayer p){
		  teamPlayers.add(p);
	  }

	  public void addEnemy(GeneralPlayer p){
		  enemies.add(p);
	  }

	//Getters and setters below this point
	//-----------------------------------------------------------------------------


	public ArrayList<GeneralPlayer> getGeneralEnemies(){
		return this.enemies;
	}

	public void setEnemies(ArrayList<GeneralPlayer> enemies) {
		this.enemies = enemies;
	}

	public ArrayList<GeneralPlayer> getTeamPlayers(){
		return teamPlayers;
	}

	public void setTeamPlayers(ArrayList<GeneralPlayer> teamPlayers) {
		this.teamPlayers = teamPlayers;
	}

	public void setMX(double newX) {
	}
	public void setMY(double newY){
	}

	public Label getNameTag()
	{
		return nameTag;
	}



}

