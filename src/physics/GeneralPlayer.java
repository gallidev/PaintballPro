package physics;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import logic.GameObject;
import rendering.*;
import java.util.ArrayList;
import java.util.List;

import ai.AIPlayer;
import enums.Teams;
import rendering.Spawn;

/**
 *  The player, represented by an ImageView
 */
public abstract class GeneralPlayer extends GameObject{

	protected final double playerHeadX = 12.5, playerHeadY = 47.5;
	protected final double movementSpeed = 2;
	protected static long shootDelay = 450;
	protected static long spawnDelay = 2000;
	protected boolean up, down, left, right, shoot, eliminated, invincible;
	protected double angle;
	protected ArrayList<Bullet> firedBullets = new ArrayList<Bullet>();
	protected Rotate rotation;
	protected Map map;
	protected String nickname;
	protected long shootTimer, spawnTimer;
	protected Teams team;
	protected ArrayList<GeneralPlayer> enemies;
	protected ArrayList<GeneralPlayer> teamPlayers;
	protected Polygon bounds = new Polygon();

	/**
	 * Create a new player at the set location, and adds the rotation property to the player
	 * @param x The x-coordinate of the player with respect to the map
	 * @param y The y-coordinate of the player with respect to the map
	 * @param scene The scene in which the player will be displayed
	 *
	 */
	public GeneralPlayer(double x, double y, String nickname, Renderer scene, Teams team, Image image){
		super(x, y, image);
		this.team = team;
		this.nickname = nickname;
		rotation = new Rotate(Math.toDegrees(angle), 0, 0, 0, Rotate.Z_AXIS);
	    getTransforms().add(rotation);
		rotation.setPivotX(playerHeadX);
		rotation.setPivotY(playerHeadY);
		map = scene.getMap();
		eliminated = false;
		invincible = false;
	}

	/**
	 * Constructor needed for the game logic.
	 * @param x The x coordinate of the player.
	 * @param y the y coordinate of the player.
	 * @param nickname The player's nickname.
	 *
	 * @ atp575
	 */
	public GeneralPlayer(double x, double y, String nickname, Image image) {
		super(x, y, image);
		this.nickname = nickname;
	}

	public void understandPlayers(ArrayList<GeneralPlayer> players){
		// understand teamPlayers and enemies
		for(GeneralPlayer player : players){
			if(player.getTeam() == this.team){
				teamPlayers.add(player);
			}else{
				enemies.add(player);
			}
		}
	}

	protected abstract void updatePosition();

	protected void updateShooting(){
		if(shoot && shootTimer < System.currentTimeMillis() - shootDelay){
			shoot();
			shootTimer = System.currentTimeMillis();
		}
	}

	//Updates the location of the bullets
	protected void updateBullets(){
		for(int i = 0; i < firedBullets.size(); i++){
			firedBullets.get(i).moveInDirection();
		}
	}

	//Calculates the angle the player is facing with respect to the mouse
	protected abstract void updateAngle();

	protected void handlePropCollision(){
		ArrayList<ImageView> props = map.getProps();
		for(ImageView prop : props){
			if(bounds.intersects(prop.getBoundsInParent())) {
				double propX = prop.getX();
				double propY = prop.getY();
				double propWidth = prop.getImage().getWidth();
				double propHeight = prop.getImage().getHeight();

				//find angle between center of player and center of the prop
				double propCenterX = propX + (propWidth/2);
				double propCenterY = propY + (propHeight/2);
				double playerCenterX = x + image.getWidth()/2;
				double playerCenterY = y + image.getHeight()/2;
				double deltax = propCenterX - playerCenterX;
				double deltay = playerCenterY - propCenterY;

				double tempAngle = Math.atan2(deltax, deltay);
				double propAngle = Math.toDegrees(tempAngle);

				if(propAngle >= 45 && propAngle <= 135){
					x -= movementSpeed; //can't go right
				}
				if(propAngle >= -135 && propAngle <= -45 ){
					x += movementSpeed; //can't go left
				}
				if(propAngle > 135 || propAngle < -135){
					y -= movementSpeed; //can't go down
				}
				if(propAngle > -45 && propAngle < 45 ){
					y += movementSpeed; //can't go up
				}
			}
			for(Bullet bullet : firedBullets){
				if(bullet.getBoundsInParent().intersects(prop.getBoundsInParent())){
					bullet.setActive(false);
				}
			}
		}
	}

	protected void handleWallCollision(){
		ArrayList<ImageView> walls = map.getWalls();
		for(ImageView wall : walls){
			if(bounds.intersects(wall.getBoundsInParent())) {
				double wallX = wall.getX();
				double wallY = wall.getY();
				double wallWidth = wall.getImage().getWidth();
				double wallHeight = wall.getImage().getHeight();

				//find angle between center of player and center of wall
				double wallCenterX = wallX + (wallWidth/2);
				double wallCenterY = wallY + (wallHeight/2);
				double playerCenterX = x + image.getWidth()/2;
				double playerCenterY = y + image.getHeight()/2;
				double deltax = wallCenterX - playerCenterX;
				double deltay = playerCenterY - wallCenterY;

				double tempAngle = Math.atan2(deltax, deltay);
				double wallAngle = Math.toDegrees(tempAngle);

				if(wallAngle >= 45 && wallAngle <= 135){
					x -= movementSpeed; //can't go right
				}
				if(wallAngle >= -135 && wallAngle <= -45 ){
					x += movementSpeed; //can't go left
				}
				if(wallAngle > 135 || wallAngle < -135){
					y -= movementSpeed; //can't go down
				}
				if(wallAngle > -45 && wallAngle < 45 ){
					y += movementSpeed; //can't go up
				}
			}

			for(Bullet bullet : firedBullets){
				if(bullet.getBoundsInParent().intersects(wall.getBoundsInParent())){
					bullet.setActive(false);
				}
			}
		}
	}
	/**
	 * handles the bullet collisions from enemies, if the player has been shot then it goes to the respawn point
	 */
	protected void handleBulletCollision()
	{
		for(GeneralPlayer enemy : enemies){

			for(Bullet bullet : enemy.getBullets()){
				if(bullet.isActive() && bounds.intersects(bullet.getBoundsInParent()) && !eliminated){
					spawnTimer = System.currentTimeMillis();
					eliminated = true;
					setVisible(false);
					bullet.setActive(false);
					return;
				}
			}
		}

	}
	
	protected void checkSpawn() {
		if(spawnTimer + spawnDelay <= System.currentTimeMillis()){
			x = map.getSpawns()[0].x * 64;
			y = map.getSpawns()[0].y * 64;
			eliminated = false;
			invincible = true;
			spawnTimer = System.currentTimeMillis();
			updatePosition();
			setVisible(true);
		}
	}
	
	protected void checkInvincibility() {
		//Invincible animation
		if(spawnTimer + spawnDelay > System.currentTimeMillis()){
			if(System.currentTimeMillis() >= spawnTimer + spawnDelay/8 && System.currentTimeMillis() < spawnTimer + 2 * spawnDelay/8)  
				setVisible(false);
			if(System.currentTimeMillis() >= spawnTimer + 2* spawnDelay/8 && System.currentTimeMillis() < spawnTimer + 3* spawnDelay/8)
				setVisible(true);
			if(System.currentTimeMillis() >= spawnTimer + 3* spawnDelay/8 && System.currentTimeMillis() < spawnTimer + 4* spawnDelay/8)
				setVisible(false);
			if(System.currentTimeMillis() >= spawnTimer + 4* spawnDelay/8 && System.currentTimeMillis() < spawnTimer + 5* spawnDelay/8)
				setVisible(true);
			if(System.currentTimeMillis() >= spawnTimer + 5* spawnDelay/8 && System.currentTimeMillis() < spawnTimer + 6* spawnDelay/8)
				setVisible(false);
			if(System.currentTimeMillis() >= spawnTimer + 6* spawnDelay/8 && System.currentTimeMillis() < spawnTimer + 7* spawnDelay/8)
				setVisible(true);
			if(System.currentTimeMillis() >= spawnTimer + 7* spawnDelay/8 && System.currentTimeMillis() < spawnTimer + 8* spawnDelay/8)
				setVisible(false);
			
		} else {
			invincible = false;
			setVisible(true);
			
		}
	}
	
	//Consists of 5 points around player
	protected void updatePlayerBounds(){
		//Point1
		double x1 = (83 * image.getWidth()/120) - playerHeadX;
		double y1 = (5 * image.getHeight()/255) - playerHeadY;
		double x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
		double y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);
		double boundx1 = x + x2 + playerHeadX;
		double boundy1 = y + y2 + playerHeadY;
		//Point2
		x1 = (image.getWidth()) - playerHeadX;
		y1 = (233 * image.getHeight()/255) - playerHeadY;
		x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
		y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);
		double boundx2 = x + x2 + playerHeadX;
		double boundy2 = y + y2 + playerHeadY;
		//Point3
		x1 = (57 * image.getWidth()/120) - playerHeadX;
		y1 = (image.getHeight()) - playerHeadY;
		x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
		y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);
		double boundx3 = x + x2 + playerHeadX;
		double boundy3 = y + y2 + playerHeadY;
		//Point4
		x1 = (1 * image.getWidth()/120) - playerHeadX;
		y1 = (183 * image.getHeight()/255) - playerHeadY;
		x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
		y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);
		double boundx4 = x + x2 + playerHeadX;
		double boundy4 = y + y2 + playerHeadY;
		//Point5
		x1 = (1 * image.getWidth()/120) - playerHeadX;
		y1 = (128 * image.getHeight()/255) - playerHeadY;
		x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
		y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);
		double boundx5 = x + x2 + playerHeadX;
		double boundy5 = y + y2 + playerHeadY;
		bounds.getPoints().clear();
		bounds.getPoints().addAll(new Double[]{
			    boundx1, boundy1,
			    boundx2, boundy2,
			    boundx3, boundy3,
			    boundx4, boundy4,
			    boundx5, boundy5});
	}
	


	/**
	 * Creates a bullet at the player's location that travels in the direction the player is facing.
	 * The bullet is added to the arraylist "firedBullets"
	 * It is called every time the player presses the left mouse button
	 */
	public void shoot(){

		double x1 = (83 * image.getWidth()/120) - playerHeadX;
		double y1 = (12 * image.getHeight()/255) - playerHeadY;

		double x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
		double y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);

		double bulletX = x + x2 + playerHeadX;
		double bulletY = y + y2 + playerHeadY;

		Bullet bullet = new Bullet(bulletX, bulletY, angle, team);
		firedBullets.add(bullet);
	}


	//Getters and setters below this point
	//-----------------------------------------------------------------------------

	public List<Bullet> getBullets(){
		return this.firedBullets;
	}

	public double getAngle(){
		return this.angle;
	}

	public void setAngle(double angle){
		this.angle = angle;
	}

	public void setUp(boolean up){
		this.up = up;
	}

	public void setDown(boolean down){
		this.down = down;
	}

	public void setLeft(boolean left){
		this.left = left;
	}

	public void setRight(boolean right){
		this.right = right;
	}

	public void setShoot(boolean shoot){
		this.shoot = shoot;
	}

	public Teams getTeam() {
		return team;
	}

	public void setEnemies(ArrayList<GeneralPlayer> enemies) {
		this.enemies = enemies;
	}

	public void setTeamPlayers(ArrayList<GeneralPlayer> teamPlayers) {
		this.teamPlayers = teamPlayers;
	}


}
