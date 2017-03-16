package players;

import enums.TeamEnum;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import physics.Bullet;
import physics.CollisionsHandler;
import physics.Flag;
import rendering.Spawn;
import serverLogic.Team;

import java.util.ArrayList;
import java.util.List;

/**
 *  The player, represented by an ImageView
 */
public abstract class EssentialPlayer extends ImageView {

	public static final double PLAYER_HEAD_X = 12.5, PLAYER_HEAD_Y = 47.5;
	static final long SHOOT_DELAY = 450;
	static final double MOVEMENT_SPEED = 2;
	private static final long SPAWN_DELAY = 2000;
	protected Rotate rotation, boundRotation;
	protected int id;
	protected TeamEnum team;
	protected CollisionsHandler collisionsHandler;
	boolean up, down, left, right, shoot, eliminated, invincible;
	boolean collUp, collDown, collLeft, collRight;
	double angle, lastAngle;
	double mouseX, mouseY;
	ArrayList<Bullet> firedBullets = new ArrayList<Bullet>();
	long shootTimer, spawnTimer;
	double lastX, lastY;
	int bulletCounter;
	private Spawn[] spawn;
	private Polygon bounds = new Polygon();
	private boolean hasFlag;

	private String nickname;

	/**
	 * Create a new player at the set location, and adds the rotation property to the player,
	 * this a General class for the Client Side which needs to store the Image
	 * @param x The x-coordinate of the player with respect to the map
	 * @param y The y-coordinate of the player with respect to the map
	 * @param id The id of the player
	 * @param image
	 *
	 */
	public EssentialPlayer(double x, double y, int id,Spawn[] spawn, TeamEnum team, CollisionsHandler collisionsHandler, Image image){
		super(image);
		this.angle = 0;
		setLayoutX(x);
		setLayoutY(y);
		setEffect(new DropShadow(16, 0, 0, Color.BLACK));
		this.lastX = x;
		this.lastY = y;
		this.lastAngle = angle;
		this.team = team;
		this.id = id;
		rotation = new Rotate(Math.toDegrees(angle), 0, 0, 0, Rotate.Z_AXIS);
		rotation.setPivotX(PLAYER_HEAD_X);
		rotation.setPivotY(PLAYER_HEAD_Y);
		getTransforms().add(rotation);
		this.spawn = spawn;
		eliminated = false;
		invincible = false;
		this.collisionsHandler = collisionsHandler;
		createPlayerBounds();
		boundRotation = new Rotate(Math.toDegrees(angle), 0, 0, 0, Rotate.Z_AXIS);
		bounds.getTransforms().add(boundRotation);
		boundRotation.setPivotX(PLAYER_HEAD_X);
		boundRotation.setPivotY(PLAYER_HEAD_Y);
		updatePlayerBounds();
		bulletCounter = 1;
	}

	protected abstract void updatePosition();

	protected abstract void updateShooting();

	//Updates the location of the bullets
	void updateBullets(){
		for(Bullet firedBullet : firedBullets)
			firedBullet.moveInDirection();
	}

	void cleanBullets(){
		if(firedBullets.size() > 0) {
			if (!firedBullets.get(0).isActive()) {
				firedBullets.remove(0);
			}
		}
	}
	//Calculates the angle the player is facing with respect to the mouse
	protected abstract void updateAngle();


	/**
	 * Method to update the team score once a player has been eliminated.
	 * Will be implemented differently depending on the player type:
	 * 		- The client player will have to send the updated information to the server
	 * 		- The offline player will have to update the internal score of the team
	 * 		- The AI player will have to adjust its behaviour depending on the the game type (single player or online)
	 *
	 */
	public abstract void updateScore();

	protected void checkSpawn() {
		if(spawnTimer + SPAWN_DELAY <= System.currentTimeMillis()){
			int i = 0;

			if(team == TeamEnum.BLUE) i = 4;
			setLayoutX( spawn[i].x * 64 );
			setLayoutY( spawn[i].y * 64 );
			eliminated = false;
			invincible = true;
			spawnTimer = System.currentTimeMillis();
			updatePosition();
			setVisible(true);
		}
	}

	protected void checkInvincibility() {
		boolean visible = true;
		//Invincible animation
		if(spawnTimer + SPAWN_DELAY > System.currentTimeMillis()){
			if(System.currentTimeMillis() >= spawnTimer + SPAWN_DELAY /8 && System.currentTimeMillis() < spawnTimer + 2 * SPAWN_DELAY /8)
				visible = false;
			if(System.currentTimeMillis() >= spawnTimer + 2* SPAWN_DELAY /8 && System.currentTimeMillis() < spawnTimer + 3* SPAWN_DELAY /8)
				visible = true;
			if(System.currentTimeMillis() >= spawnTimer + 3* SPAWN_DELAY /8 && System.currentTimeMillis() < spawnTimer + 4* SPAWN_DELAY /8)
				visible = false;
			if(System.currentTimeMillis() >= spawnTimer + 4* SPAWN_DELAY /8 && System.currentTimeMillis() < spawnTimer + 5* SPAWN_DELAY /8)
				visible = true;
			if(System.currentTimeMillis() >= spawnTimer + 5* SPAWN_DELAY /8 && System.currentTimeMillis() < spawnTimer + 6* SPAWN_DELAY /8)
				visible = false;
			if(System.currentTimeMillis() >= spawnTimer + 6* SPAWN_DELAY /8 && System.currentTimeMillis() < spawnTimer + 7* SPAWN_DELAY /8)
				visible = true;
			if(System.currentTimeMillis() >= spawnTimer + 7* SPAWN_DELAY /8 && System.currentTimeMillis() < spawnTimer + 8* SPAWN_DELAY /8)
				visible = false;

		} else {
			invincible = false;
			visible = true;
		}
		setVisible(visible);
	}

	//Consists of 5 points around player
	//Point1
	public void createPlayerBounds(){
			double x1 = (83 * getImage().getWidth()/120) - PLAYER_HEAD_X;
			double y1 = (5 * getImage().getHeight()/255) - PLAYER_HEAD_Y;
			double x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
			double y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);
			double boundx1 = x2 + PLAYER_HEAD_X;
			double boundy1 = y2 + PLAYER_HEAD_Y;
			//Point2
			x1 = (getImage().getWidth()) - PLAYER_HEAD_X;
			y1 = (233 * getImage().getHeight()/255) - PLAYER_HEAD_Y;
			x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
			y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);
			double boundx2 = x2 + PLAYER_HEAD_X;
			double boundy2 = y2 + PLAYER_HEAD_Y;
			//Point3
			x1 = (57 * getImage().getWidth()/120) - PLAYER_HEAD_X;
			y1 = (getImage().getHeight()) - PLAYER_HEAD_Y;
			x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
			y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);
			double boundx3 = x2 + PLAYER_HEAD_X;
			double boundy3 = y2 + PLAYER_HEAD_Y;
			//Point4
			x1 = (1 * getImage().getWidth()/120) - PLAYER_HEAD_X;
			y1 = (183 * getImage().getHeight()/255) - PLAYER_HEAD_Y;
			x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
			y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);
			double boundx4 = x2 + PLAYER_HEAD_X;
			double boundy4 = y2 + PLAYER_HEAD_Y;
			//Point5
			x1 = (1 * getImage().getWidth()/120) - PLAYER_HEAD_X;
			y1 = (128 * getImage().getHeight()/255) - PLAYER_HEAD_Y;
			x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
			y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);
			double boundx5 = x2 + PLAYER_HEAD_X;
			double boundy5 = y2 + PLAYER_HEAD_Y;
			bounds.getPoints().clear();
			bounds.getPoints().addAll(boundx1, boundy1,
					boundx2, boundy2,
					boundx3, boundy3,
					boundx4, boundy4,
					boundx5, boundy5);
		}

		public void updatePlayerBounds(){
			bounds.setLayoutX(getLayoutX());
			bounds.setLayoutY(getLayoutY());
			boundRotation.setAngle(Math.toDegrees(angle));
		}


	/**
	 * Creates a bullet at the player's location that travels in the direction the player is facing.
	 * The bullet is added to the arraylist "firedBullets"
	 * It is called every time the player presses the left mouse button
	 */
	public void shoot(){

		double x1 = (83 * getImage().getWidth()/120) - PLAYER_HEAD_X;
		double y1 = (12 * getImage().getHeight()/255) - PLAYER_HEAD_Y;

		double x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
		double y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);

		double bulletX = getLayoutX() + x2 + PLAYER_HEAD_X;
		double bulletY = getLayoutY() + y2 + PLAYER_HEAD_Y;

		Bullet bullet = new Bullet(bulletCounter,bulletX, bulletY, angle, team);

		firedBullets.add(bullet);

		bulletCounter ++;
	}

	public void beenShot() {
		spawnTimer = System.currentTimeMillis();
		eliminated = true;
		setVisible(false);
		updateScore();
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

	public double getAngleDegrees(){
		return Math.toDegrees(this.angle);
	}

	public synchronized void setUp(boolean up){
		this.up = up;
	}

	public synchronized void setDown(boolean down){
		this.down = down;
	}

	public synchronized void setLeft(boolean left){
		this.left = left;
	}

	public synchronized void setRight(boolean right){
		this.right = right;
	}

	public synchronized void setShoot(boolean shoot){
		this.shoot = shoot;
	}

	public boolean isShooting(){
		return shoot;
	}

	public TeamEnum getTeam() {
		return team;
	}

	public int getPlayerId(){
		return id;
	}

	public synchronized void setMouseX(int newX) {
		mouseX = newX;
	}
	public synchronized void setMouseY(int newY){
		mouseY = newY;
	}

	public boolean hasFlag()
	{
		return hasFlag;
	}

	public void setHasFlag(boolean hasFlag)
	{
		this.hasFlag = hasFlag;
	}

	public boolean isEliminated(){
		return eliminated;
	}

	public Polygon getPolygonBounds() {
		return bounds;
	}

	public void setCollUp(boolean collUp) {
		this.collUp = collUp;
	}
	public void setCollDown(boolean collDown) {
		this.collDown = collDown;
	}
	public void setCollLeft(boolean collLeft) {
		this.collLeft = collLeft;
	}
	public void setCollRight(boolean collRight) {
		this.collRight = collRight;
	}

	public TeamEnum getColour(){
		return team;
	}

	public abstract void setMyTeam(Team team);
	public abstract void setOppTeam(Team team);
	public abstract void tick();

	public void setNickname(String n){
		nickname = n;
	}

	public String getNickname(){
		return nickname;
	}

	public CollisionsHandler getCollisionsHandler(){
		return collisionsHandler;
	}

}

