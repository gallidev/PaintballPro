package physics;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import logic.GameObject;
import logic.LocalPlayer;
import rendering.*;
import java.util.ArrayList;
import java.util.List;

import enums.TeamEnum;
/**
 *  The player, represented by an ImageView
 */
public abstract class GeneralPlayer extends ImageView implements GameObject{

	public static final double playerHeadX = 12.5, playerHeadY = 47.5;
	protected final double movementSpeed = 2;
	protected static long shootDelay = 450;
	protected static long spawnDelay = 2000;
	protected boolean up, down, left, right, shoot, eliminated, invincible;
	protected boolean collUp, collDown, collLeft, collRight;
	protected double angle, lastAngle;
	protected ArrayList<Bullet> firedBullets = new ArrayList<Bullet>();
	protected Rotate rotation;
	protected Map map;
	protected int id;
	protected long shootTimer, spawnTimer;
	protected TeamEnum team;
	protected ArrayList<GeneralPlayer> enemies;
	protected ArrayList<GeneralPlayer> teamPlayers;
	protected Polygon bounds = new Polygon();
	protected ArrayList<Rectangle> propsWalls;

	/**
	 * Create a new player at the set location, and adds the rotation property to the player,
	 * this a General class for the Client Side which needs to store the Image
	 * @param x The x-coordinate of the player with respect to the map
	 * @param y The y-coordinate of the player with respect to the map
	 * @param id The id of the player
	 * @param map The map in which the player is playing
	 * @param Team The team of the player
	 *
	 */
	public GeneralPlayer(double x, double y, int id, Map map, TeamEnum team, Image image){
		super(image);
		setLayoutX(x);
		setLayoutY(y);
		this.team = team;
		this.id = id;
		rotation = new Rotate(Math.toDegrees(angle), 0, 0, 0, Rotate.Z_AXIS);
	    getTransforms().add(rotation);
		rotation.setPivotX(playerHeadX);
		rotation.setPivotY(playerHeadY);
		this.map = map;
		propsWalls = map.getRecProps();
	    propsWalls.addAll(map.getRecWalls());
		eliminated = false;
		invincible = false;
		updatePlayerBounds();

	}

	/**
	 * Constructor needed for the game logic.
	 * @param x The x coordinate of the player.
	 * @param y the y coordinate of the player.
	 * @param nickname The player's nickname.
	 *
	 * @ atp575
	 */
	public GeneralPlayer(double x, double y, int id, Image image) {
		super(image);
		setLayoutX(x);
		setLayoutY(y);
		this.id = id;
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

	protected void handlePropWallCollision(){
		collUp = false;
		collDown = false;
		collRight = false;
		collLeft = false;
		for(Rectangle propWall : propsWalls){
			if(Shape.intersect(bounds, propWall).getBoundsInLocal().isEmpty() == false) {
				double propX = propWall.getX();
				double propY = propWall.getY();
				double propWidth = propWall.getWidth();
				double propHeight = propWall.getHeight();

				//find angle between center of player and center of the prop
				double propCenterX = propX + (propWidth/2);
				double propCenterY = propY + (propHeight/2);
				double playerCenterX = getLayoutX() + getImage().getWidth()/2;
				double playerCenterY = getLayoutY() + getImage().getHeight()/2;
				double deltax = propCenterX - playerCenterX;
				double deltay = playerCenterY - propCenterY;

				double tempAngle = Math.atan2(deltax, deltay);
				double propAngle = Math.toDegrees(tempAngle);

				if(propAngle >= 45 && propAngle <= 135){
					collRight = true;
					//x -= movementSpeed; //can't go right
				}
				if(propAngle >= -135 && propAngle <= -45 ){
					collLeft = true;
					//x += movementSpeed; //can't go left
				}
				if(propAngle > 135 || propAngle < -135){
					collDown = true;
					//y -= movementSpeed; //can't go down
				}
				if(propAngle > -45 && propAngle < 45 ){
					collUp = true;
					//y += movementSpeed; //can't go up
				}
			}
			for(Bullet bullet : firedBullets){
				if(bullet.getBoundsInParent().intersects(propWall.getBoundsInParent())){
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
			int i = 0;
			if(team == TeamEnum.BLUE) i = 4;
			setLayoutX(map.getSpawns()[i].x * 64);
			setLayoutY(map.getSpawns()[i].y * 64);
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
	public void updatePlayerBounds(){
		//Point1
		double x1 = (83 * getImage().getWidth()/120) - playerHeadX;
		double y1 = (5 * getImage().getHeight()/255) - playerHeadY;
		double x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
		double y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);
		double boundx1 = getLayoutX() + x2 + playerHeadX;
		double boundy1 = getLayoutY() + y2 + playerHeadY;
		//Point2
		x1 = (getImage().getWidth()) - playerHeadX;
		y1 = (233 * getImage().getHeight()/255) - playerHeadY;
		x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
		y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);
		double boundx2 = getLayoutX() + x2 + playerHeadX;
		double boundy2 = getLayoutY() + y2 + playerHeadY;
		//Point3
		x1 = (57 * getImage().getWidth()/120) - playerHeadX;
		y1 = (getImage().getHeight()) - playerHeadY;
		x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
		y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);
		double boundx3 = getLayoutX() + x2 + playerHeadX;
		double boundy3 = getLayoutY() + y2 + playerHeadY;
		//Point4
		x1 = (1 * getImage().getWidth()/120) - playerHeadX;
		y1 = (183 * getImage().getHeight()/255) - playerHeadY;
		x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
		y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);
		double boundx4 = getLayoutX() + x2 + playerHeadX;
		double boundy4 = getLayoutY() + y2 + playerHeadY;
		//Point5
		x1 = (1 * getImage().getWidth()/120) - playerHeadX;
		y1 = (128 * getImage().getHeight()/255) - playerHeadY;
		x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
		y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);
		double boundx5 = getLayoutX() + x2 + playerHeadX;
		double boundy5 = getLayoutY() + y2 + playerHeadY;
		bounds.getPoints().clear();
		bounds.getPoints().addAll(boundx1, boundy1,
				boundx2, boundy2,
				boundx3, boundy3,
				boundx4, boundy4,
				boundx5, boundy5);

	}



	/**
	 * Creates a bullet at the player's location that travels in the direction the player is facing.
	 * The bullet is added to the arraylist "firedBullets"
	 * It is called every time the player presses the left mouse button
	 */
	public void shoot(){

		double x1 = (83 * getImage().getWidth()/120) - playerHeadX;
		double y1 = (12 * getImage().getHeight()/255) - playerHeadY;

		double x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
		double y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);

		double bulletX = getLayoutX() + x2 + playerHeadX;
		double bulletY = getLayoutY() + y2 + playerHeadY;

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

	public TeamEnum getTeam() {
		return team;
	}

	public void setTeamPlayers(ArrayList<GeneralPlayer> teamPlayers) {
		this.teamPlayers = teamPlayers;
	}

	public void setEnemies(ArrayList<GeneralPlayer> enemies) {
		this.enemies = enemies;
	}

	public ArrayList<GeneralPlayer> getEnemies(){
		return this.enemies;
	}

	public int getPlayerId(){
		return id;
	}
}
