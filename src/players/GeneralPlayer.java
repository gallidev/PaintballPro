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

	protected Map map;
	protected ArrayList<ServerMinimumPlayer> enemies;
	protected ArrayList<ServerMinimumPlayer> teamPlayers;
	protected boolean scoreChanged = false;
	protected AudioManager audio;
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
		this.map = map;
		this.audio = audio;
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


	public ArrayList<ServerMinimumPlayer> getEnemies(){
		return this.enemies;
	}

	public void setEnemies(ArrayList<ServerMinimumPlayer> enemies) {
		this.enemies = enemies;
	}

	public ArrayList<ServerMinimumPlayer> getTeamPlayers(){
		return teamPlayers;
	}

	public void setTeamPlayers(ArrayList<ServerMinimumPlayer> teamPlayers) {
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

