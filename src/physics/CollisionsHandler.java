package physics;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.scene.traversal.Hueristic2D;

import enums.TeamEnum;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import players.EssentialPlayer;
import rendering.Map;
import serverLogic.CaptureTheFlagMode;
import serverLogic.Team;

public class CollisionsHandler
{

	private boolean debug = true;

	private ArrayList<Rectangle> propsWalls;
	private Rectangle spawnAreaRed;
	private Rectangle spawnAreaBlue;
	private ArrayList<EssentialPlayer> redTeam;
	private ArrayList<EssentialPlayer> blueTeam;
	private Flag flag;
	private Powerup[] powerups;

	private Team red;
	private Team blue;

	private boolean flagCaptured = false;
	private boolean flagDropped = false;
	private boolean flagRespwaned = false;
	private int playerWithFlagId;

	private double hitWallX;
	private double hitWallY;
	private TeamEnum splashColour;
	private boolean hasHitWall = false;

	private boolean tookPowerup = false;
	private int powerupPlayerId;
	
	public CollisionsHandler(Map map)
	{
		this.propsWalls = map.getRecProps();
		this.propsWalls.addAll(map.getRecWalls());
		this.redTeam = new ArrayList<>();
		this.blueTeam = new ArrayList<>();
		red = new Team(TeamEnum.RED);
		blue = new Team(TeamEnum.BLUE);
		this.spawnAreaBlue = map.getRecSpawn(TeamEnum.BLUE);
		this.spawnAreaRed = map.getRecSpawn(TeamEnum.RED);
		this.flag = map.getFlag();
		this.powerups = map.getPowerups();
	}

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

			for(Bullet bullet : p.getBullets())
			{
				if(bullet.getBoundsInParent().intersects(propWall.getBoundsInParent())){
					bullet.disable(propWall);

					hitWallX = propX;
					hitWallY = propY;
					splashColour = bullet.getColour();
					hasHitWall = true;
				}

			}

			//filter out walls and props far away from the player
			if(Math.abs(propX - p.getLayoutX()) > 72 || Math.abs(propY - p.getLayoutY()) > 72)
				continue;


			//System.out.println("collsionsss maybee");
			//it returns a path with the collision with walls
			//System.out.println("bound player " + p.getPlayerId() + " : " + p.getPolygonBounds().toString());
			Path tmp = (Path) Shape.intersect(p.getPolygonBounds(), propWall);
			if(!tmp.getBoundsInLocal().isEmpty())
			{
				//System.out.println("Collision of :" +  propWall.toString()+  " and " + p.getPolygonBounds());
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
			//System.out.println("mean: " + mean);
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
		//System.out.println("CollisionsHandler " + p.getPlayerId() + " collup :" + collUp + " collDown:" + collDown + " collLeft:" + collLeft + " collRight: " + collRight );

		p.setCollUp(collUp);
		p.setCollDown(collDown);
		p.setCollLeft(collLeft);
		p.setCollRight(collRight);

	}

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
				System.out.println("the player does not have a team");
				break;
		}

	}

	public void handleFlagCollision(EssentialPlayer p){
		if(flag != null){

			//check if the player touches the flag
			if(!flag.isCaptured() &&
					p.getPolygonBounds().getBoundsInParent().intersects(flag.getBoundsInParent()) &&
					!p.isEliminated()){
				if(debug) System.out.println("Caught the flag");
				flag.setFlagCarrier(p);
				flag.setCaptured(true);
				flag.setVisible(false);
				p.setHasFlag(true);

				flagCaptured = true;
				playerWithFlagId = p.getPlayerId();

			}
			//check if the player got shot so leave the flag in the player position
			if(p.isEliminated() && p.hasFlag()){
				if(debug) System.out.println("Dropped the flag");
				flag.setLayoutX(p.getLayoutX());
				flag.setLayoutY(p.getLayoutY());
				flag.setCaptured(false);
				flag.setVisible(true);
				p.setHasFlag(false);

				if (red.containsPlayer(p))
					blue.incrementScore(CaptureTheFlagMode.lostFlagScore);
				else
					red.incrementScore(CaptureTheFlagMode.lostFlagScore);

				flagDropped = true;
				playerWithFlagId = p.getPlayerId();


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
						red.incrementScore(CaptureTheFlagMode.flagScore);
					else
						blue.incrementScore(CaptureTheFlagMode.flagScore);

					flagRespwaned = true;
					playerWithFlagId = p.getPlayerId();
				}
			}
		}
	}

	public void handlePowerUpCollision(EssentialPlayer p){

		for(int i = 0; i < powerups.length; i ++){
			if(p.getPolygonBounds().getBoundsInParent().intersects(powerups[i].getBoundsInParent())){
				if(!powerups[i].isTaken()) {
					powerups[i].setTaken(true);
					powerups[i].took();
					if (powerups[i].getType() == PowerupType.SHIELD) {
						p.giveShield();

					} else if (powerups[i].getType() == PowerupType.SPEED) {
						p.giveSpeed();
					}
				}
				
				powerupPlayerId = p.getPlayerId();
				tookPowerup = true;
			}
		}
	}

	private void checkBulletsAgainstATeam(EssentialPlayer p, ArrayList<EssentialPlayer> opponents){
		for(EssentialPlayer enemy : opponents){

			for(Bullet bullet : enemy.getBullets())
			{
				if(bullet.isActive() && p.getPolygonBounds().getBoundsInParent().intersects(bullet.getBoundsInParent()) && !p.isEliminated())
				{
					bullet.disable();
					//check if the player has the shield power up
					if(p.getShieldActive()){
						//shield absorbs a bullet
						p.removeShield();
					} else {
						//if the player has no shield, the player is eliminated
						p.beenShot();
					}
					return;
				}
			}
		}
	}

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

	public ArrayList<EssentialPlayer> getRedTeam() {
		return redTeam;
	}

	public void setRedTeam(ArrayList<EssentialPlayer> redTeam) {
		this.redTeam = redTeam;;
	}

	public void setRedTeam(Team red) {
		this.red = red;
		redTeam = red.getMembers();
	}

	public ArrayList<EssentialPlayer> getBlueTeam() {
		return blueTeam;
	}

	public void setBlueTeam(ArrayList<EssentialPlayer> blueTeam) {
		this.blueTeam = blueTeam;
	}

	public void setBlueTeam(Team blue) {
		this.blue = blue;
		blueTeam = blue.getMembers();
	}

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

	public Flag getFlag(){
		return flag;
	}

	public boolean isFlagCaptured() {
		return flagCaptured;
	}

	public void setFlagCaptured(boolean flagCaptured) {
		this.flagCaptured = flagCaptured;
	}

	public boolean isFlagDropped() {
		return flagDropped;
	}

	public void setFlagDropped(boolean flagDropped) {
		this.flagDropped = flagDropped;
	}

	public boolean isFlagRespawned() {
		return flagRespwaned;
	}

	public void setRespawned(boolean b) {
		this.flagRespwaned = b;
	}

	public double getHitWallX(){
		return hitWallX;
	}

	public double getHitWallY(){
		return hitWallY;
	}

	public TeamEnum getSplashColour(){
		return splashColour;
	}

	public int getPlayerWithFlagId(){
		return playerWithFlagId;
	}

	public void setWallHit(boolean b){
		hasHitWall = b;
	}

	public boolean isWallHit(){
		return hasHitWall;
	}

	public boolean isTookPowerup() {
		return tookPowerup;
	}

	public void setTookPowerup(boolean tookPowerup) {
		this.tookPowerup = tookPowerup;
	}

	public void setSplashColour(TeamEnum splashColour) {
		this.splashColour = splashColour;
	}
	
	public int getPowerupPlayerId(){
		return powerupPlayerId;
	}

}
