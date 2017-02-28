package physics;

import java.util.ArrayList;
import java.util.List;

import enums.TeamEnum;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import players.GeneralPlayer;
import rendering.Map;

public class CollisionsHandler {


	private ArrayList<Rectangle> propsWalls;
	private ArrayList<GeneralPlayer> redTeam;
	private ArrayList<GeneralPlayer> blueTeam;

	public CollisionsHandler(Map map){
		this.propsWalls = map.getRecProps();
	    this.propsWalls.addAll(map.getRecWalls());
	    this.redTeam = new ArrayList<>();
	    this.blueTeam = new ArrayList<>();
	}

	public void handlePropWallCollision(GeneralPlayer p){
		boolean collUp = false;
		boolean collDown = false;
		boolean collRight = false;
		boolean collLeft = false;
		ArrayList<Double> angles = new ArrayList<>();
		double playerCenterX = p.getLayoutX() + p.getImage().getWidth()/2;
		double playerCenterY = p.getLayoutY() + p.getImage().getHeight()/2;
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

	public void handleBulletCollision(GeneralPlayer p)
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

	private void checkBulletsAgainstATeam(GeneralPlayer p, ArrayList<GeneralPlayer> opponents){
		for(GeneralPlayer enemy : opponents){

			for(Bullet bullet : enemy.getBullets()){
				if(bullet.isActive() && p.getPolygonBounds().intersects(bullet.getBoundsInParent()) && !p.isEliminated()){
					bullet.setActive(false);
					p.beenShot();
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

	public ArrayList<GeneralPlayer> getRedTeam() {
		return redTeam;
	}

	public void setRedTeam(ArrayList<GeneralPlayer> redTeam) {
		this.redTeam = redTeam;
	}

	public ArrayList<GeneralPlayer> getBlueTeam() {
		return blueTeam;
	}

	public void setBlueTeam(ArrayList<GeneralPlayer> blueTeam) {
		this.blueTeam = blueTeam;
	}

	public void setPlayers(ArrayList<GeneralPlayer> players){
		for(GeneralPlayer p : players)
		{
			if(p.getTeam() == TeamEnum.RED)
				redTeam.add(p);
			else
				blueTeam.add(p);
		}
	}

}
