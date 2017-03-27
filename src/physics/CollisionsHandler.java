package physics;

import enums.PowerupType;
import enums.TeamEnum;
import gui.GUIManager;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import logic.server.CaptureTheFlagMode;
import logic.server.Team;
import networking.game.GameUpdateListener;
import players.EssentialPlayer;
import rendering.Map;

import java.util.ArrayList;
import java.util.List;

import static gui.GUIManager.renderer;

/**
 * This class represents the  Collisions Handler is responsible of checking all the collisions of every game object
 *
 * @author Filippo Galli
 * @author Sivarjuen Ravichandran
 */
public class CollisionsHandler
{

	/** The Constant debug flag */
	private static final boolean debug = false;

	/** Are the collisions running on local or on the server */
	public boolean isLocal = false;

	/** The rectangles of props and walls. */
	private ArrayList<Rectangle> propsWalls;

	/** The spawn area of red Team. */
	private Rectangle spawnAreaRed;

	/** The spawn area of blue Team. */
	private Rectangle spawnAreaBlue;

	/** The players of the red team. */
	private ArrayList<EssentialPlayer> redTeam;

	/** The plaeyrs of the blue team. */
	private ArrayList<EssentialPlayer> blueTeam;

	/** The flag. */
	private Flag flag;

	/** The power ups. */
	private Powerup[] powerups;

	/** The red Team. */
	private Team red;

	/** The blue Team. */
	private Team blue;

	/** The listener of game updates. */
	private GameUpdateListener listener;

	/**
	 * Instantiates a new collisions handler with the map specified.
	 *
	 * @param map the map of the game
	 */
	public CollisionsHandler(Map map)
	{
		propsWalls = map.getPropCollisionBounds();
		propsWalls.addAll(map.getWallCollisionBounds());
		redTeam = new ArrayList<>();
		blueTeam = new ArrayList<>();
		red = new Team(TeamEnum.RED);
		blue = new Team(TeamEnum.BLUE);
		spawnAreaBlue = map.getSpawnCollisionBound(TeamEnum.BLUE);
		spawnAreaRed = map.getSpawnCollisionBound(TeamEnum.RED);
		flag = map.getFlag();
		powerups = map.getPowerups();
	}

	/**
	 * Handle player and its bullet collisions with walls and props
	 *
	 * @param p The Player to check collisions with
	 */
	public void handlePropWallCollision(EssentialPlayer p){
		boolean collUp = false;
		boolean collDown = false;
		boolean collRight = false;
		boolean collLeft = false;
		ArrayList<Double> angles = new ArrayList<>();
		double playerCenterX = p.getLayoutX() + p.getImage().getWidth() / 2;
		double playerCenterY = p.getLayoutY() + p.getImage().getHeight() / 2;

		for(Rectangle propWall : propsWalls)
		{
			double propX = propWall.getX();
			double propY = propWall.getY();

			for(Pellet pellet : p.getBullets())
			{
				if(pellet.getBoundsInParent().intersects(propWall.getBoundsInParent())){
					pellet.disable(propWall);
				}

			}

			//filter out walls and props far away from the player
			if(Math.abs(propX - p.getLayoutX()) > 72 || Math.abs(propY - p.getLayoutY()) > 72)
				continue;

			//it returns a path with the collision with walls
			if(debug) System.out.println("bound player " + p.getPlayerId() + " : " + p.getPolygonBounds().toString());
			Path tmp = (Path) Shape.intersect(p.getPolygonBounds(), propWall);
			if(!tmp.getBoundsInLocal().isEmpty())
			{
				if(debug) System.out.println("Collision of :" +  propWall.toString()+  " and " + p.getPolygonBounds());
				double propWidth = propWall.getWidth();
				double propHeight = propWall.getHeight();

				//find angle between center of player and center of the prop
				//MoveTo moveToValues = (MoveTo) tmp.getElements().get(0);
				double propCenterX = (propX + propWidth / 2);
				double propCenterY = (propY + propHeight / 2);
				double deltax = propCenterX - playerCenterX;
				double deltay = playerCenterY - propCenterY;

				double tempAngle = Math.atan2(deltay, deltax);
				double propAngle = Math.toDegrees(tempAngle);

				angles.add(propAngle);
				if(propAngle < 135 && propAngle >= 45)
				{
					collUp = true;
				}
				if(propAngle < 45 && propAngle >= -45)
				{
					collRight = true;
				}
				if(propAngle < -45 && propAngle >= -135)
				{
					collDown = true;
				}
				if(propAngle < -135 || propAngle >= 135)
				{
					collLeft = true;
				}
			}
		}
		if(!angles.isEmpty())
		{
			double mean = getMeanAngle(angles);
			if(debug) System.out.println("mean: " + mean);
			if(mean < 135 && mean >= 45)
			{
				collUp = true;
			}
			if(mean < 45 && mean >= -45)
			{
				collRight = true;
			}
			if(mean < -45 && mean >= -135)
			{
				collDown = true;
			}
			if(mean < -135 || mean >= 135)
			{
				collLeft = true;
			}
		}
		if(debug) System.out.println("CollisionsHandler " + p.getPlayerId() + " collup :" + collUp + " collDown:" + collDown + " collLeft:" + collLeft + " collRight: " + collRight );

		p.setCollUp(collUp);
		p.setCollDown(collDown);
		p.setCollLeft(collLeft);
		p.setCollRight(collRight);

	}

	/**
	 * Handle player collisions against enemies' bullets.
	 *
	 * @param p The Player to check collisions with
	 */
	public void handleBulletCollision(EssentialPlayer p)
	{
		switch(p.getTeam())
		{
			case RED:
			{
				checkBulletsAgainstATeam(p, blueTeam);
				break;
			}
			case BLUE:
			{
				checkBulletsAgainstATeam(p, redTeam);
				break;
			}
			default:
				//System.out.println("the player does not have a team");
				break;
		}

	}

	/**
	 * Handle collisions of the player specified with the flag.
	 *
	 * @param p The Player to check collisions with
	 */
	public void handleFlagCollision(EssentialPlayer p){
		if(flag != null){

			//check if the player touches the flag
			if(!flag.isCaptured() &&
					p.getPolygonBounds().getBoundsInParent().intersects(flag.getBoundsInParent()) &&
					!p.isEliminated()){
				if(debug) System.out.println("Caught the flag");
				if (isLocal) GUIManager.audio.playSFX(GUIManager.audio.sfx.flagcollect, (float)1.0);
				flag.setFlagCarrier(p);
				flag.setCaptured(true);
				flag.setVisible(false);
				p.setHasFlag(true);
				if(renderer != null)
					renderer.getHud().toggleFlagStatus(p.getTeam());
				if(listener != null)
					listener.onFlagCaptured(p.getPlayerId());

			}
			//check if the player got shot so leave the flag in the player position
			if(p.isEliminated() && p.hasFlag()){
				if(debug) System.out.println("Dropped the flag");
				flag.relocate(p.getLayoutX(), p.getLayoutY());
				flag.setCaptured(false);
				flag.setVisible(true);
				if(renderer != null)
					renderer.getHud().toggleFlagStatus(p.getTeam());
				if(listener != null)
					listener.onFlagDropped(p.getPlayerId());
				p.setHasFlag(false);

			//check if the player has brought the flag back to his base
			}if(p.hasFlag()){
				boolean baseTouched = false;
				switch(p.getTeam())
				{
					case RED: baseTouched = p.getPolygonBounds().getBoundsInParent().intersects(spawnAreaRed.getBoundsInParent());
						break;
					case BLUE: baseTouched = p.getPolygonBounds().getBoundsInParent().intersects(spawnAreaBlue.getBoundsInParent());
						break;
					default: break;
				}
				if(baseTouched && !p.isEliminated()){
					if(debug) System.out.println("Brought the flag back");
					flag.resetPosition();
					flag.setCaptured(false);
					flag.setVisible(true);
					p.setHasFlag(false);

					if (red.containsPlayer(p))
						red.incrementScore(CaptureTheFlagMode.FLAG_SCORE);
					else
						blue.incrementScore(CaptureTheFlagMode.FLAG_SCORE);
					if(renderer != null)
						renderer.getHud().toggleFlagStatus(p.getTeam());
					if(listener != null)
						listener.onFlagRespawned(p.getPlayerId());
				}
			}
		}
	}

	/**
	 * Handle the collisions of a player with power ups.
	 *
	 * @param p The Player to check collisions with
	 */
	public void handlePowerUpCollision(EssentialPlayer p){

		for(Powerup powerup : powerups)
		{
			if(p.getPolygonBounds().getBoundsInParent().intersects(powerup.getBoundsInParent()))
			{
				if(powerup.isVisible())
				{
					powerup.take();
					if (isLocal) GUIManager.audio.playSFX(GUIManager.audio.sfx.pickup, (float)1.0);
					if(powerup.getType() == PowerupType.SHIELD)
					{
						p.setShield(true);
						if(listener != null)
							listener.onPowerupAction(PowerupType.SHIELD, p.getPlayerId());
					}
					else if(powerup.getType() == PowerupType.SPEED)
					{
						p.setSpeed(true);
						if(listener != null)
							listener.onPowerupAction(PowerupType.SPEED, p.getPlayerId());
					}
				}
			}
		}
	}

	/**
	 * Check collisions of enemies bullets with a player
	 *
	 * @param p The Player to check collisions with
	 * @param opponents the enemies against the player
	 */
	private void checkBulletsAgainstATeam(EssentialPlayer p, ArrayList<EssentialPlayer> opponents){
		for(EssentialPlayer enemy : opponents){

			for(Pellet pellet : enemy.getBullets())
			{
				if(pellet.isActive() && p.getPolygonBounds().getBoundsInParent().intersects(pellet.getBoundsInParent()) && !p.isEliminated())
				{
					if(debug) System.out.println("Been shot ");
					pellet.disable();
					if(listener != null){
						listener.onBulletKills(enemy.getPlayerId(), pellet.getPelletId());
					}
					//check if the player has the shield power up
					if(p.getShieldActive()){
						//shield absorbs a bullet
						p.setShield(false);
						if(listener != null)
							listener.onPowerupAction(PowerupType.SHIELD, p.getPlayerId());
					} else {
						//if the player has no shield, the player is eliminated
						p.beenShot();
					}
					return;
				}
			}
		}
	}

	/**
	 * Calculates the mean of a list of angles in degrees.
	 *
	 * @param sample the List of angles
	 * @return the mean angle
	 */
	// source file can be found here https://rosettacode.org/wiki/Averages/Mean_angle#Java
  private double getMeanAngle(List<Double> sample) {

    double x_component = 0.0;
    double y_component = 0.0;
    double avg_d, avg_r;

    for (double angle_d : sample) {
      double angle_r;
      angle_r = Math.toRadians(angle_d);
      x_component += Math.cos(angle_r);
      y_component += Math.sin(angle_r);
    }
    x_component /= sample.size();
    y_component /= sample.size();
    avg_r = Math.atan2(y_component, x_component);
    avg_d = Math.toDegrees(avg_r);

    return avg_d;
  }

	/**
	 * Sets the red team.
	 *
	 * @param red the new red team
	 */
	public void setRedTeam(Team red) {
		this.red = red;
		redTeam = red.getMembers();
	}

	/**
	 * Sets the blue team.
	 *
	 * @param blue the new blue team
	 */
	public void setBlueTeam(Team blue) {
		this.blue = blue;
		blueTeam = blue.getMembers();
	}

	/**
	 * Sets the players.
	 *
	 * @param players the new players
	 */
	public void setPlayers(ArrayList<EssentialPlayer> players){
		for(EssentialPlayer p : players)
		{
			if(p.getTeam() == TeamEnum.RED){
				redTeam.add(p);
				red.addMember(p);
			}
			else{
				blueTeam.add(p);
				blue.addMember(p);
			}

		}
	}

	/**
	 * Sets the listener.
	 *
	 * @param listener the new listener
	 */
	public void setListener(GameUpdateListener listener)
	{
		this.listener = listener;
	}

	/**
	 * Gets the flag.
	 *
	 * @return the flag
	 */
	public Flag getFlag(){
		return flag;
	}

	/**
	 * Gets the speed power up.
	 *
	 * @return the speed power up
	 */
	public Powerup getSpeedPowerup(){
		return powerups[1];
	}

	/**
	 * Gets the shield power up.
	 *
	 * @return the shield power up
	 */
	public Powerup getShieldPowerup(){
		return powerups[0];
	}

}
