package physics;
import audio.AudioManager;
import enums.TeamEnum;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import logic.Team;
import networkingClient.ClientReceiver;
import networkingClient.ClientSender;
import rendering.Map;

import static gui.GUIManager.bluePlayerImage;
import static gui.GUIManager.redPlayerImage;

/**
 *  The player, represented by an ImageView that should be running
 */
public class ClientPlayer extends GeneralPlayer{

	private double mx, my;
	private boolean controlScheme;
	private ClientSender sender;
	private AudioManager audio;
	private ClientReceiver receiver;

	//flag for keeping track of scores
	boolean scoreChanged = true;

	/**
	 * Create a new player at the set location, and adds the rotation property to the player
	 * @param x The x-coordinate of the player with respect to the map
	 * @param y The y-coordinate of the player with respect to the map
	 * @param controlScheme True - movement with respect to cursor location, False - movement with respect to global position
	 * @param scene The scene in which the player will be displayed
	 *
	 */
	public ClientPlayer(double x, double y, int id, boolean controlScheme,Map map, AudioManager audio, TeamEnum team, ClientReceiver receiver){
		super(x, y, id, map, team, team == TeamEnum.RED ? redPlayerImage : bluePlayerImage);
		this.audio = audio;
		this.mx = x;
		this.my = y;
		this.controlScheme = controlScheme;
		angle = 0.0;

		if(receiver != null){
			this.receiver = receiver;
			this.sender = receiver.getSender();
		}

	}

	public ClientPlayer(double x, double y, int id, TeamEnum team, ClientReceiver receiver){
		super(x, y, id, team == TeamEnum.RED ? redPlayerImage : bluePlayerImage);
		controlScheme = false;
		this.team = team;
		this.receiver = receiver;
	}


	/**
	 * Tick is called every frame
	 * It updates the player location and angle, and shoots bullets if the shoot button is pressed
	 */
	@Override
	public void tick() {
		// handle the collisions with walls and props before moving the position
		// of the player so to understand if he can move or not in a specific direction
		handlePropWallCollision();
		if(!eliminated){
			updatePosition();
			updateShooting();
			updateAngle();
		} else {
			checkSpawn();
			if (scoreChanged && receiver != null)
				updateScore();
		}
		updatePlayerBounds();
		updateBullets();
		if(receiver != null){
			sendServerNewPosition(getLayoutX(), getLayoutY(), angle);
			sendActiveBullets();
		}

		if(!invincible){
			handleBulletCollision();
		} else {
			checkInvincibility();
		}
	}

	private void updateScore() {
		sendServerNewScore();
		scoreChanged = false;

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			System.err.println("Thread can't sleep" + e);
		}
	}

	@Override
	protected void updatePosition(){
		if(controlScheme){
			if(up){
				setLayoutY(getLayoutY() - movementSpeed * Math.cos(angle));
				setLayoutX(getLayoutX() + movementSpeed * Math.sin(angle));
			}
			if(down){
				setLayoutY(getLayoutY() + movementSpeed * Math.cos(angle));
				setLayoutX(getLayoutX() - movementSpeed * Math.sin(angle));
			}
			if(left){
				setLayoutY(getLayoutY() - movementSpeed * Math.cos(angle - Math.PI/2));
				setLayoutX(getLayoutX() + movementSpeed * Math.sin(angle - Math.PI/2));
			}
			if(right){
				setLayoutY(getLayoutY() - movementSpeed * Math.cos(angle + Math.PI/2));
				setLayoutX(getLayoutX() + movementSpeed * Math.sin(angle + Math.PI/2));
			}
		} else {
			if(up && !collUp) setLayoutY(getLayoutY() - movementSpeed);
			if(down && !collDown) setLayoutY(getLayoutY() + movementSpeed);
			if(left  && !collLeft) setLayoutX(getLayoutX() - movementSpeed);
			if(right  && !collRight) setLayoutX(getLayoutX() + movementSpeed);
		}
	}


	//Calculates the angle the player is facing with respect to the mouse
	@Override
	protected void updateAngle(){
		Point2D temp = this.localToScene(1.65 * playerHeadX, playerHeadY);
		double x1 = temp.getX();
		double y1 = temp.getY();

		double deltax = mx - x1;
		double deltay = y1 - my;
		if(collUp){
			setLayoutY(getLayoutY() + movementSpeed);
		} else if(collDown) {
			setLayoutY(getLayoutY() - movementSpeed);
		} else if(collLeft) {
			setLayoutX(getLayoutX() + movementSpeed);
		} else if(collRight) {
			setLayoutX(getLayoutX() - movementSpeed);
		}
		angle = Math.atan2(deltax, deltay);
		rotation.setAngle(Math.toDegrees(angle));
	}

	/**
	 * Lets the server know when a player has moved.
	 *
	 * @author Alexandra Paduraru
	 */
	private void sendServerNewPosition(double x, double y, double angle){
		String msg = "SendToAll:Move:" + id + ":" + x + ":" + y + ":" + angle; //Protocol message for updating a location

		sender.sendMessage(msg);
	}

	/**
	 * Lets the server know when a team has gained an additional point.
	 *
	 * @author Alexandra Paduraru
	 */
	public void sendServerNewScore(){
		//Protocol: Scored:<team>
		String msg = "Scored:";

		//The current player has been shot, so the point goes to the other team
		if(team == TeamEnum.RED)
			msg += "Blue";
		else
			msg += "Red";

		sender.sendMessage(msg);
	}

	private void sendActiveBullets(){
		String msg = "SendToAll:Bullet:" + id;
		for(Bullet bullet: firedBullets){
			if(bullet.isActive()){
				msg += ":" + bullet.getX() + ":" + bullet.getY() + ":" + bullet.getAngle() + ":" + team;
			}
		}
		sender.sendMessage(msg);
	}


	public void shoot(){

		double x1 = (83 * getImage().getWidth()/120) - playerHeadX;
		double y1 = (12 * getImage().getHeight()/255) - playerHeadY;

		double x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
		double y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);

		double bulletX = getLayoutX() + x2 + playerHeadX;
		double bulletY = getLayoutY() + y2 + playerHeadY;

		Bullet bullet = new Bullet(bulletX, bulletY, angle, team);
		audio.playSFX(audio.sfx.getRandomPaintball(), (float)1.0);
		firedBullets.add(bullet);
	}

	public void setMap(Map m){
		map = m;
		propsWalls = map.getRecProps();
		propsWalls.addAll(map.getRecWalls());
	}



	public double getMX(){
		return this.mx;
	}

	public void setMX(double mx){
		this.mx = mx;
	}

	public double getMY(){
		return this.my;
	}

	public void setMY(double my){
		this.my = my;
	}

	public void setAudio(AudioManager audio)
	{
		this.audio = audio;
	}

}
