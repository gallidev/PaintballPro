package physics;

import java.util.ArrayList;
import java.util.List;

import enums.TeamEnum;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import players.ServerMinimumPlayer;
import rendering.Map;
import serverLogic.Team;

public class CollisionsHandler {


	private ArrayList<Rectangle> propsWalls;
	private ArrayList<ServerMinimumPlayer> redTeam;
	private ArrayList<ServerMinimumPlayer> blueTeam;
	
	private Team red;
	private Team blue;

	public CollisionsHandler(Map map){
		this.propsWalls = map.getRecProps();
	    this.propsWalls.addAll(map.getRecWalls());
	    this.redTeam = new ArrayList<>();
	    this.blueTeam = new ArrayList<>();
	}

	public void handlePropWallCollision(ServerMinimumPlayer p){
		boolean collUp = false;
		boolean collDown = false;
		boolean collRight = false;
		boolean collLeft = false;
		ArrayList<Double> angles = new ArrayList<>();
		double playerCenterX = p.getX() + p.getWidth()/2;
		double playerCenterY = p.getY() + p.getHeight()/2;
		for(Rectangle propWall : propsWalls){
			//it returns a path with the collision with walls
			Path tmp = (Path) Shape.intersect(p.getPolygonBounds(), propWall);
			if(tmp.getBoundsInLocal().isEmpty() == false) {

				//System.out.println("Collisionssss ");
				double propX = propWall.getX();
				double propY = propWall.getY();
				double propWidth = propWall.getWidth();
				double propHeight = propWall.getHeight();

				//find angle between center of player and center of the prop
				//MoveTo moveToValues = (MoveTo) tmp.getElements().get(0);
				double propCenterX = (propX +propWidth/2);
				double propCenterY = (propY + propHeight/2);
				double deltax = propCenterX - playerCenterX;
				double deltay = playerCenterY - propCenterY;

				double tempAngle = Math.atan2(deltay, deltax);
				double propAngle = Math.toDegrees(tempAngle);

				angles.add(propAngle);
				if(propAngle < 135 && propAngle >= 45){
					collUp = true;
				}
				if( propAngle < 45 && propAngle >= -45){
					collRight = true;
				}
				if(propAngle < -45 && propAngle >= -135){
					collDown = true;
				}
				if(propAngle < -135 || propAngle >= 135 ){
					collLeft = true;
				}
			}
			for(Bullet bullet : p.getBullets()){
				if(bullet.getBoundsInParent().intersects(propWall.getBoundsInParent())){
					bullet.setActive(false);
				}
			}
		}
		if(!angles.isEmpty()){
			double mean = getMeanAngle(angles);
			//System.out.println("mean: " + mean);
			if(mean < 135 && mean >= 45){
				collUp = true;
			}
			if( mean < 45 && mean >= -45){
				collRight = true;
			}
			if(mean < -45 && mean >= -135){
				collDown = true;
			}
			if(mean < -135 || mean >= 135 ){
				collLeft = true;
			}
		}
		//System.out.println("CollisionsHandler collup: " + collUp + " collDown:" + collDown + " collLeft:" + collLeft + " collRight: " + collRight );

		p.setCollUp(collUp);
		p.setCollDown(collDown);
		p.setCollLeft(collLeft);
		p.setCollRight(collRight);

	}

	public void handleBulletCollision(ServerMinimumPlayer p)
	{
		switch(p.getTeam()){
		case RED:{
			checkBulletsAgainstATeam(p, blueTeam);
			break;
		}
		case BLUE:{
			checkBulletsAgainstATeam(p, redTeam);
			break;
		}
		default: System.out.println("the player does not have a team");
			break;
		}

	}

	private void checkBulletsAgainstATeam(ServerMinimumPlayer p, ArrayList<ServerMinimumPlayer> opponents){
		for(ServerMinimumPlayer enemy : opponents){

			for(Bullet bullet : enemy.getBullets()){
				if(bullet.isActive() && p.getPolygonBounds().getBoundsInParent().intersects(bullet.getBoundsInParent()) && !p.isEliminated()){
					bullet.setActive(false);
					p.beenShot();
					
					//update score
					if (red.containsPlayer(p))
						red.incrementScore(1);
					else 
						blue.incrementScore(1);
					
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

	public ArrayList<ServerMinimumPlayer> getRedTeam() {
		return redTeam;
	}

	public void setRedTeam(Team red) {
		this.red = red;
		redTeam = red.getMembers();
	}

	public ArrayList<ServerMinimumPlayer> getBlueTeam() {
		return blueTeam;
	}

	public void setBlueTeam(Team blue) {
		this.blue = blue;
		blueTeam = blue.getMembers();
	}

	public void setPlayers(ArrayList<ServerMinimumPlayer> players){
		for(ServerMinimumPlayer p : players)
		{
			if(p.getTeam() == TeamEnum.RED)
				redTeam.add(p);
			else
				blueTeam.add(p);
		}
	}

}
