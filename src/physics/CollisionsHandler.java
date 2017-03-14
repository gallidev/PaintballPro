package physics;

import java.util.ArrayList;
import java.util.List;

import enums.TeamEnum;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import players.EssentialPlayer;
import rendering.Map;
import serverLogic.Team;

public class CollisionsHandler
{

	private ArrayList<Rectangle> propsWalls;
	private Rectangle spawnAreaRed;
	private Rectangle spawnAreaBlue;
	private ArrayList<EssentialPlayer> redTeam;
	private ArrayList<EssentialPlayer> blueTeam;
	private Flag flag;

	private Team red;
	private Team blue;

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
				if(bullet.getBoundsInParent().intersects(propWall.getBoundsInParent()))
					bullet.disable(propWall);
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
		if(!flag.isCaptured() && p.getPolygonBounds().getBoundsInParent().intersects(flag.getBoundsInParent()) && !p.isEliminated()){
			flag.setCaptured(true);
			flag.setVisible(false);
			p.setHasFlag(true);
		}
		if(p.isEliminated() && p.hasFlag()){
			flag.setLayoutX(p.getLayoutX());
			flag.setLayoutY(p.getLayoutY());
			flag.setCaptured(false);
			flag.setVisible(true);
			p.setHasFlag(false);
		}

		if(p.hasFlag()){
			boolean baseTouched = true;
			switch(p.getTeam())
			{
				case RED: baseTouched = spawnAreaRed.intersects(flag.getBoundsInParent());
					break;
				case BLUE: baseTouched = spawnAreaBlue.intersects(flag.getBoundsInParent());
					break;
				default: break;
			}
			if(baseTouched && !p.isEliminated()){

				flag.setCaptured(false);
				flag.setVisible(true);
				p.setHasFlag(false);
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
					p.beenShot();

					//update score
					if (red.containsPlayer(p))
						blue.incrementScore(1);
					else
						red.incrementScore(1);

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

}
