package players;

import audio.AudioManager;
import enums.Team;
import networking.game.UDPClient;
import physics.Bullet;
import physics.CollisionsHandler;
import rendering.ImageFactory;
import rendering.Map;

import java.util.ArrayList;

/**
 * The player, represented by an ImageView that should be running
 */
public class PhysicsClientPlayer extends GeneralPlayer
{

	//flag for keeping track of scores
	boolean scoreChanged = true;
	private double mx, my;
	private boolean controlScheme;
	private UDPClient sender;
	private ArrayList<ClientLocalPlayer> clientEnemies;

	/**
	 * Create a new player at the set location, and adds the rotation property to the player
	 *
	 * @param x             The x-coordinate of the player with respect to the map
	 * @param y             The y-coordinate of the player with respect to the map
	 * @param controlScheme True - movement with respect to cursor location, False - movement with respect to global position
	 */
	public PhysicsClientPlayer(double x, double y, int id, boolean controlScheme, Map map, AudioManager audio, Team team, UDPClient sender, CollisionsHandler collisionHandler)
	{
		super(x, y, id, map, team, ImageFactory.getPlayerImage(team), audio, collisionHandler);
		this.mx = x;
		this.my = y;
		this.controlScheme = controlScheme;
		angle = 0.0;
		this.sender = sender;
	}

	public PhysicsClientPlayer(double x, double y, int id, Team team, UDPClient sender)
	{
		super(x, y, id, ImageFactory.getPlayerImage(team));
		controlScheme = false;
		this.team = team;
		this.sender = sender;
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
		if(!eliminated)
		{
			lastX = getLayoutX();
			lastY = getLayoutY();
			lastAngle = angle;
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
		if(!(lastX == getLayoutX() && lastY == getLayoutY() && lastAngle == angle)){
			sendServerNewPosition(getLayoutX(), getLayoutY(), angle);
		}
		sendActiveBullets();

		if(!invincible)
		{
			collisionsHandler.handleBulletCollision(this);
		}
		else
		{
			checkInvincibility();
		}
	}

	protected void handleBulletCollision()
	{
		for(GeneralPlayer enemy : clientEnemies)
		{
			for(Bullet bullet : enemy.getBullets())
			{
				if(bullet.isActive() && bounds.intersects(bullet.getBoundsInParent()) && !eliminated)
				{
					audio.playSFX(audio.sfx.splat, (float) 1.0);
					spawnTimer = System.currentTimeMillis();
					eliminated = true;
					updateScore();
					setVisible(false);
					bullet.setActive(false);
					return;
				}
			}
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
		double deltax = mx - (1.65 * playerHeadX);
		double deltay = playerHeadY - my;
		angle = Math.atan2(deltax, deltay);
		rotation.setAngle(Math.toDegrees(angle));
	}

	/**
	 * Lets the server know when a player has moved.
	 *
	 * @author Alexandra Paduraru
	 */
	private void sendServerNewPosition(double x, double y, double angle)
	{
		String msg = "SendToAll:Move:" + id + ":" + x + ":" + y + ":" + angle; //Protocol message for updating a location

		sender.sendMessage(msg);
	}

	/**
	 * Lets the server know when a team has gained an additional point.
	 *
	 * @author Alexandra Paduraru
	 */
	public void updateScore()
	{
		//Protocol: Scored:<team>
		String msg = "Scored:";

		//The current player has been shot, so the point goes to the other team
		if(team == Team.RED)
			msg += "Blue";
		else
			msg += "Red";
		sender.sendMessage(msg);
	}

	private void sendActiveBullets()
	{
		String msg = "SendToAll:Bullet:" + id + ":" + team;
		for(Bullet bullet : firedBullets)
		{
			if(bullet.isActive())
			{
				msg += ":" + bullet.getX() + ":" + bullet.getY() + ":" + bullet.getAngle();
			}
		}
		sender.sendMessage(msg);
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
		Bullet bullet = new Bullet(bulletX, bulletY, bulletAngle, team);
		audio.playSFX(audio.sfx.getRandomPaintball(), (float) 1.0);
		firedBullets.add(bullet);
	}

	@Override
	public void setMX(double mx)
	{
		this.mx = mx;
	}

	@Override
	public void setMY(double my)
	{
		this.my = my;
	}

	public ArrayList<ClientLocalPlayer> getClientEnemies()
	{
		return clientEnemies;
	}

	public void setClientEnemies(ArrayList<ClientLocalPlayer> enemies)
	{
		this.clientEnemies = enemies;
	}
}
