package players;

import enums.GameMode;
import enums.TeamEnum;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import logic.server.Team;
import networking.game.GameUpdateListener;
import physics.CollisionsHandler;
import physics.Pellet;
import rendering.ImageFactory;
import rendering.Spawn;

import java.util.ArrayList;
import java.util.List;

/**
 *  The essential player abstract class, represents player by an ImageView.
 *
 * @author Filippo Galli
 * @author Sivarjuen Ravichandran
 */
public abstract class EssentialPlayer extends ImageView {

	/** The player head Y and X used to calculate the exact angle of the player. */
	public static final double PLAYER_HEAD_X = 12.5, PLAYER_HEAD_Y = 47.5;

	/** The Constant targetFPS. */
	private static final double targetFPS = 60.0;

	/** The shoot delay. */
	protected long shootDelay = 450;

	/** The re spawn delay. */
	protected long spawnDelay = 2000;

	/** The bound rotation. */
	protected Rotate rotation, boundRotation;

	/** The id of the player. */
	protected int id;

	/** The team. */
	protected TeamEnum team;

	/** The collisions handler. */
	protected CollisionsHandler collisionsHandler;

	/** The game mode. */
	protected GameMode gameMode;

	/** The movement speed. */
	protected double movementSpeed = 2.5;

	/** The game speed. */
	protected double gameSpeed;

	/** The speed power up active. */
	protected boolean speedActive = false;

	/** The speed power up timer. */
	protected long speedTimer;

	/** The speed power up duration. */
	protected long speedDuration = 10000;

	/** The speed power up timer active. */
	protected boolean speedTimerActive = false;

	/** The shield power up active. */
	//If shield is true, player is able to take an extra shot
	protected boolean shieldActive = false;

	/** The nickname. */
	protected String nickname;

	/** The score changed. */
	protected boolean scoreChanged = false;

	/** Input decisions of the players. */
	boolean up, down, left, right, shoot;

	/** Is the player eliminated and is the player invincible */
	boolean eliminated, invincible;

	/** The coll right. */
	boolean collUp, collDown, collLeft, collRight;

	/** The player has shot. */
	boolean hasShot;

	/** The current angle and the last angle. */
	double angle, lastAngle;

	/** The fired pellets. */
	ArrayList<Pellet> firedPellets = new ArrayList<Pellet>();

	/** The shoot and spawn timer. */
	long shootTimer, spawnTimer;

	/** The last X and Y. */
	double lastX, lastY;

	/** The bullet counter. */
	int bulletCounter;

	/** The spawn locations. */
	private Spawn[] spawn;

	/** The bounds for collisions . */
	private Polygon bounds = new Polygon();

	/** The player has flag. */
	private boolean hasFlag;

	/** The shadow. */
	private DropShadow shadow = new DropShadow(16, 0, 0, Color.BLACK);

	/** The shield effect popped. */
	private boolean shieldPopped = false;

	/** The listener for sending game state. */
	private GameUpdateListener listener;

	/**
	 * Create a new player at the set location, and adds the rotation property to the player.
	 *
	 * @param x The x-coordinate of the player with respect to the map
	 * @param y The y-coordinate of the player with respect to the map
	 * @param id The id of the player
	 * @param spawn the spawn locations
	 * @param team the team of the player
	 * @param collisionsHandler the collisions handler
	 * @param image the image of the player
	 * @param game the game mode the player is playing
	 * @param currentFPS the current FPS speed of the simulation on which the player is updating
	 */
	public EssentialPlayer(double x, double y, int id,Spawn[] spawn, TeamEnum team, CollisionsHandler collisionsHandler, Image image, GameMode game, double currentFPS){
		super(image);
		this.angle = 0;
		setLayoutX(x);
		setLayoutY(y);
		setEffect(shadow);
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
		hasShot = false;
		this.collisionsHandler = collisionsHandler;
		createPlayerBounds();
		boundRotation = new Rotate(Math.toDegrees(angle), 0, 0, 0, Rotate.Z_AXIS);
		bounds.getTransforms().add(boundRotation);
		boundRotation.setPivotX(PLAYER_HEAD_X);
		boundRotation.setPivotY(PLAYER_HEAD_Y);
		updatePlayerBounds();
		bulletCounter = 1;
		this.gameSpeed = targetFPS/currentFPS;
		this.movementSpeed = this.movementSpeed * gameSpeed;
		this.spawnDelay = this.spawnDelay * (long) gameSpeed;
		this.shootDelay = this.shootDelay * (long) gameSpeed;
		gameMode = game;

	}

	/**
	 * Tick is called every frame
	 * It updates the player location and angle, and shoots bullets if the shoot button is pressed,
	 * it handles power ups, elimination, respawn, calls the collision methods.
	 */
	public abstract void tick();

	/**
	 * Update position of the player.
	 */
	protected abstract void updatePosition();

	/**
	 * Update shooting of the player.
	 */
	protected abstract void updateShooting();

	/**
	 * Update bullets locations move them in direction.
	 */
	//Updates the location of the bullets
	void updateBullets(){
		for(Pellet firedPellet : firedPellets)
			firedPellet.moveInDirection();
	}

	/**
	 * Clean bullets that are not active.
	 */
	void cleanBullets(){
		if(firedPellets.size() > 0) {
			if (!firedPellets.get(0).isActive()) {
				firedPellets.remove(0);
			}
		}
	}

	/**
	 * Update angle of the player.
	 */
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

	/**
	 * Check when the right time has passed and re spawn the player in the right location.
	 */
	protected void checkSpawn() {
		if(spawnTimer + spawnDelay <= System.currentTimeMillis()){
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

	/**
	 * Check if the player is invicinble and apply an image effect.
	 */
	protected void checkInvincibility() {
		boolean visible = true;
		//Invincible animation
		if(spawnTimer + spawnDelay > System.currentTimeMillis()){
			if(System.currentTimeMillis() >= spawnTimer + spawnDelay /8 && System.currentTimeMillis() < spawnTimer + 2 * spawnDelay /8)
				visible = false;
			if(System.currentTimeMillis() >= spawnTimer + 2* spawnDelay /8 && System.currentTimeMillis() < spawnTimer + 3* spawnDelay /8)
				visible = true;
			if(System.currentTimeMillis() >= spawnTimer + 3* spawnDelay /8 && System.currentTimeMillis() < spawnTimer + 4* spawnDelay /8)
				visible = false;
			if(System.currentTimeMillis() >= spawnTimer + 4* spawnDelay /8 && System.currentTimeMillis() < spawnTimer + 5* spawnDelay /8)
				visible = true;
			if(System.currentTimeMillis() >= spawnTimer + 5* spawnDelay /8 && System.currentTimeMillis() < spawnTimer + 6* spawnDelay /8)
				visible = false;
			if(System.currentTimeMillis() >= spawnTimer + 6* spawnDelay /8 && System.currentTimeMillis() < spawnTimer + 7* spawnDelay /8)
				visible = true;
			if(System.currentTimeMillis() >= spawnTimer + 7* spawnDelay /8 && System.currentTimeMillis() < spawnTimer + 8* spawnDelay /8)
				visible = false;

		} else {
			invincible = false;
			visible = true;
		}
		setVisible(visible);
	}

	/**
	 * Creates the player bounds for collisions, it consists of 5 points around player.
	 */

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

		/**
		 * Update player bounds based on the rotation.
		 */
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

		generateBullet(bulletCounter, bulletX, bulletY, angle);

		bulletCounter ++;
		hasShot = true;
	}

	/**
	 * Generate bullet.
	 *
	 * @param bulletId the bullet id
	 * @param x the x origin of the bullet
	 * @param y the y origin of the bullet
	 * @param angle the angle of the player was aiming
	 */
	public void generateBullet(int bulletId, double x, double y, double angle){
		Pellet pellet = new Pellet(bulletId, x, y, angle, team, gameSpeed);

		if(listener != null){
			listener.onShotBullet(id, bulletCounter, x, y, angle);
		}
		firedPellets.add(pellet);

	}

	/**
	 * The player has been shot, so make the player invisible and in eliminated state.
	 * Update also the score and the spawn timer.
	 */
	public void beenShot() {
		spawnTimer = System.currentTimeMillis();
		eliminated = true;
		setVisible(false);
		updateScore();
	}

	/**
	 * Relocate player with tag.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void relocatePlayerWithTag(double x, double y)
	{

	}

	/**
	 * Handle power ups.
	 */
	void handlePowerUp() {
		if (speedActive && !speedTimerActive) {
			speedTimerActive = true;
			speedTimer = System.currentTimeMillis();
		} else if (speedActive) {
			if (speedTimer < System.currentTimeMillis() - speedDuration || eliminated) {
				setSpeed(false);
				speedTimerActive = false;
			}
		}
	}


	//Getters and setters below this point
	//-----------------------------------------------------------------------------

	/**
	 * Gets the bullets.
	 *
	 * @return the bullets
	 */
	public List<Pellet> getBullets(){
		return this.firedPellets;
	}

	/**
	 * Gets the sync bullets.
	 *
	 * @return the sync bullets
	 */
	public synchronized List<Pellet> getSyncBullets(){
		return this.firedPellets;
	}

	/**
	 * Gets the angle.
	 *
	 * @return the angle
	 */
	public double getAngle(){
		return this.angle;
	}

	/**
	 * Sets the angle.
	 *
	 * @param angle the new angle
	 */
	public void setAngle(double angle){
		this.angle = angle;
	}

	/**
	 * Sets the shoot.
	 *
	 * @param shoot the new shoot
	 */
	public synchronized void setShoot(boolean shoot){
		this.shoot = shoot;
	}

	/**
	 * Checks if is shooting.
	 *
	 * @return true, if is shooting
	 */
	public boolean isShooting(){
		return shoot;
	}

	/**
	 * Gets the team.
	 *
	 * @return the team
	 */
	public TeamEnum getTeam() {
		return team;
	}

	/**
	 * Gets the player id.
	 *
	 * @return the player id
	 */
	public int getPlayerId(){
		return id;
	}

	/**
	 * Checks for flag.
	 *
	 * @return true, if successful
	 */
	public boolean hasFlag()
	{
		return hasFlag;
	}

	/**
	 * Sets the checks for flag.
	 *
	 * @param hasFlag the new checks for flag
	 */
	public void setHasFlag(boolean hasFlag)
	{
		this.hasFlag = hasFlag;
		if(hasFlag)
		{
			shadow.setColor(team == TeamEnum.RED ? Color.RED : Color.BLUE);
			setImage(ImageFactory.getPlayerFlagImage(team));
		}
		else
		{
			shadow.setColor(Color.BLACK);
			setImage(ImageFactory.getPlayerImage(team));
		}
	}

	/**
	 * Checks if is eliminated.
	 *
	 * @return true, if is eliminated
	 */
	public boolean isEliminated(){
		return eliminated;
	}

	/**
	 * Sets the eliminated.
	 *
	 * @param eliminated the new eliminated
	 */
	public void setEliminated(boolean eliminated){
		this.eliminated = eliminated;
	}

	/**
	 * Gets the polygon bounds.
	 *
	 * @return the polygon bounds
	 */
	public Polygon getPolygonBounds() {
		return bounds;
	}

	/**
	 * Gets the coll up.
	 *
	 * @return the coll up
	 */
	public boolean getCollUp() {
		return collUp;
	}

	/**
	 * Sets the coll up.
	 *
	 * @param collUp the new coll up
	 */
	public void setCollUp(boolean collUp) {
		this.collUp = collUp;
	}

	/**
	 * Gets the coll down.
	 *
	 * @return the coll down
	 */
	public boolean getCollDown() {
		return collDown;
	}

	/**
	 * Sets the coll down.
	 *
	 * @param collDown the new coll down
	 */
	public void setCollDown(boolean collDown) {
		this.collDown = collDown;
	}

	/**
	 * Gets the coll left.
	 *
	 * @return the coll left
	 */
	public boolean getCollLeft() {
		return collLeft;
	}

	/**
	 * Sets the coll left.
	 *
	 * @param collLeft the new coll left
	 */
	public void setCollLeft(boolean collLeft) {
		this.collLeft = collLeft;
	}

	/**
	 * Gets the coll right.
	 *
	 * @return the coll right
	 */
	public boolean getCollRight() {
		return collRight;
	}

	/**
	 * Sets the coll right.
	 *
	 * @param collRight the new coll right
	 */
	public void setCollRight(boolean collRight) {
		this.collRight = collRight;
	}

	/**
	 * Gets the colour.
	 *
	 * @return the colour
	 */
	public TeamEnum getColour(){
		return team;
	}

	/**
	 * Sets the my team.
	 *
	 * @param team the new my team
	 */
	public abstract void setMyTeam(Team team);

	/**
	 * Sets the opp team.
	 *
	 * @param team the new opp team
	 */
	public abstract void setOppTeam(Team team);


	/**
	 * Gets the nickname.
	 *
	 * @return the nickname
	 */
	public String getNickname(){
		return nickname;
	}

	/**
	 * Sets the nickname.
	 *
	 * @param n the new nickname
	 */
	public void setNickname(String n){
		nickname = n;
	}

	/**
	 * Gets the collisions handler.
	 *
	 * @return the collisions handler
	 */
	public CollisionsHandler getCollisionsHandler(){
		return collisionsHandler;
	}

	/**
	 * Gets the score changed.
	 *
	 * @return the score changed
	 */
	public boolean getScoreChanged(){
		return scoreChanged;
	}

	/**
	 * Sets the score changed.
	 *
	 * @param b the new score changed
	 */
	public void setScoreChanged(boolean b){
		scoreChanged  = b;
	}

	/**
	 * Gets the shield active.
	 *
	 * @return the shield active
	 */
	public boolean getShieldActive(){
		return this.shieldActive;
	}

	/**
	 * Sets the shield.
	 *
	 * @param active the new shield
	 */
	public void setShield(boolean active)
	{
		if(active)
		{
			shieldActive = true;
			setShieldEffect(true);
		}
		else
		{
			shieldActive = false;
			shieldPopped = true;
			setShieldEffect(false);
		}
	}

	/**
	 * Sets the speed.
	 *
	 * @param active the new speed
	 */
	public void setSpeed(boolean active)
	{
		if(active)
		{
			movementSpeed = 4 * gameSpeed;
			speedActive = true;
		}
		else
		{
			movementSpeed = 2.5 * gameSpeed;
			speedActive = false;
		}
	}

	/**
	 * Update rotation.
	 *
	 * @param angleRotation the angle rotation
	 */
	public abstract void updateRotation(double angleRotation);

	/**
	 * Gets the name tag.
	 *
	 * @return the name tag
	 */
	public Node getNameTag() {
		return null;
	}

	/**
	 * Gets the shield popped.
	 *
	 * @return the shield popped
	 */
	public boolean getShieldPopped(){
		return shieldPopped;
	}

	/**
	 * Sets the shield popped.
	 *
	 * @param b the new shield popped
	 */
	public void setShieldPopped(boolean b){
		shieldPopped = b;
	}

	/**
	 * Gets the up.
	 *
	 * @return the up
	 */
	public synchronized boolean getUp(){
		return up;
	}

	/**
	 * Sets the up.
	 *
	 * @param up the new up
	 */
	public synchronized void setUp(boolean up){
		this.up = up;
	}

	/**
	 * Gets the down.
	 *
	 * @return the down
	 */
	public synchronized boolean getDown(){
		return down;
	}

	/**
	 * Sets the down.
	 *
	 * @param down the new down
	 */
	public synchronized void setDown(boolean down){
		this.down = down;
	}

	/**
	 * Gets the left.
	 *
	 * @return the left
	 */
	public synchronized boolean getLeft(){
		return left;
	}

	/**
	 * Sets the left.
	 *
	 * @param left the new left
	 */
	public synchronized void setLeft(boolean left){
		this.left = left;
	}

	/**
	 * Gets the right.
	 *
	 * @return the right
	 */
	public synchronized boolean getRight(){
		return right;
	}

	/**
	 * Sets the right.
	 *
	 * @param right the new right
	 */
	public synchronized void setRight(boolean right){
		this.right = right;
	}

	/**
	 * Sets the shield effect.
	 *
	 * @param active the new shield effect
	 */
	public void setShieldEffect(boolean active)
	{
		shadow.setInput(active ? new ColorAdjust(0, 0, 0.25, 0) : null);
		if(active)
			shadow.setColor(team == TeamEnum.RED ? Color.RED : Color.BLUE);
		else if(!hasFlag)
			shadow.setColor(Color.BLACK);

		if(!active)
			shieldPopped = true;
	}

	/**
	 * Checks if is speed active.
	 *
	 * @return true, if is speed active
	 */
	public boolean isSpeedActive(){
		return speedActive;
	}

	/**
	 * Sets the collisions handler listener.
	 *
	 * @param listener the new collisions handler listener
	 */
	public void setCollisionsHandlerListener(GameUpdateListener listener){
		this.listener = listener;
	}

	/**
	 * Gets the counter frame.
	 *
	 * @return the counter frame
	 */
	public int getCounterFrame() {
		return 0;
	}
}

