package ai;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import logic.GameObject;
import physics.Bullet;
import rendering.*;
import java.util.ArrayList;
import java.util.List;
import enums.Teams;


public class AIPlayer extends GameObject{
	
	private final double playerHeadX = 12.5, playerHeadY = 47.5;
	private static long shootDelay = 500;
	private boolean up, down, left, right, shoot;
	private double angle;
	private ArrayList<Bullet> firedBullets = new ArrayList<Bullet>();
	private Rotate rotation;
	private Map map;
	private String nickname;
	private long shootTime;
	private Teams team;


	public AIPlayer(double x, double y, String nickname, Renderer scene, Teams team, Image image){
		super(x, y, image);
		this.team = team;
		this.nickname = nickname;
		angle = Math.toRadians(90);
		rotation = new Rotate(Math.toDegrees(angle), 0, 0, 0, Rotate.Z_AXIS);
	    getTransforms().add(rotation);
		rotation.setPivotX(playerHeadX);
		rotation.setPivotY(playerHeadY);
		map = scene.getMap();
		right = true;
	}
	
	/**
	 * Constructor needed for the game logic.
	 * @param x The x coordinate of the player.
	 * @param y the y coordinate of the player.
	 * @param nickname The player's nickname.
	 * 
	 * @ atp575
	 */
	public AIPlayer(double x, double y, String nickname, Image image) {
		super(x, y, image);
		this.nickname = nickname;
	}

	/**
	 * Tick is called every frame
	 * It updates the player location and angle, and shoots bullets if the shoot button is pressed
	 */
	@Override
	public void tick() {
		if(up){
			y -= 2;
		}	
		if(down){
			y += 2;
		}			
		if(left){
			x -= 2;
			angle = Math.toRadians(-90);
		}			
		if(right){
			x += 2;
			angle = Math.toRadians(90);
		}
		
		if(shoot && shootTime < System.currentTimeMillis() - shootDelay){
			shoot();
			shootTime = System.currentTimeMillis();
		}
		
		//Updates the location of the bullets
		for(int i = 0; i < firedBullets.size(); i++){
			firedBullets.get(i).moveInDirection();
		}
		
		//Calculates the angle the player is facing with respect to the target coordinates	
		rotation.setAngle(Math.toDegrees(angle));
		
		//Moves player in target direction
		setLayoutX(x);
		setLayoutY(y);
		
		//Player collision detection
		
				//Object collision
				ArrayList<ImageView> props = map.getProps();
				for(ImageView prop : props){
					if(getBoundsInParent().intersects(prop.getBoundsInParent())) {
						double propX = prop.getX();
						double propY = prop.getY();
						double propWidth = prop.getImage().getWidth();
						double propHeight = prop.getImage().getHeight();
						if(propX >= x + image.getWidth()/2){
							if(propY < y + image.getHeight()) {
								right = false;
								left = true;
								x -= 1; //can't go right
							}
							if(propY + propHeight > y) {
								right = false;
								left = true;
								x -= 1; //can't go right
							}
						}
						if(propX + propWidth/2 < x - image.getWidth()/2){
							if(propY < y + image.getHeight()) {
								right = true;
								left = false;
								x += 1; //can't go left
							}
							if(propY + propHeight > y) {
								right = true;
								left = false;
								x += 1; //can't go left
							}
						}
						if(propY >= (y + image.getHeight()/2)){
							y -= 2; //can't go down
						}
						if(propY <= y){
							y += 2; //can't go up
						}
					}
					for(Bullet bullet : firedBullets){
						if(bullet.getBoundsInParent().intersects(prop.getBoundsInParent())){
							bullet.setActive(false);
						}
					}
				}
				
				//Wall collision
				ArrayList<ImageView> walls = map.getWalls();
				for(ImageView wall : walls){
					if(getBoundsInParent().intersects(wall.getBoundsInParent())) {
						double wallX = wall.getX();
						double wallY = wall.getY();
						double wallWidth = wall.getImage().getWidth();
						double wallHeight = wall.getImage().getHeight();
						if(wallX >= x + image.getWidth()/2){
							if(wallY < y + image.getHeight()) {
								right = false;
								left = true;
								x -= 1; //can't go right
							}
							if(wallY + wallHeight > y) {
								right = false;
								left = true;
								x -= 1; //can't go right
							}
						}
						if(wallX + wallWidth/2 < x - image.getWidth()/2){
							if(wallY < y + image.getHeight()) {
								right = true;
								left = false;
								x += 1; //can't go left
							}
							if(wallY + wallHeight > y) {
								right = true;
								left = false;
								x += 1; //can't go left
							}
						}
						if(wallY >= (y + image.getHeight()/2)){
							y -= 2; //can't go down
						}
						if(wallY <= y){
							y += 2; //can't go up
						}
					}
				
					for(Bullet bullet : firedBullets){
						if(bullet.getBoundsInParent().intersects(wall.getBoundsInParent())){
							bullet.setActive(false);
						}
					}
				}
		
	}
	
	/**
	 * Creates a bullet at the player's location that travels in the direction the player is facing.
	 * The bullet is added to the arraylist "firedBullets"
	 * It is called every time the player presses the left mouse button
	 */
	public void shoot(){
		
		double x1 = (83 * image.getWidth()/120) - playerHeadX;
		double y1 = (12 * image.getHeight()/255) - playerHeadY;

		double x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
		double y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);

		double bulletX = x + x2 + playerHeadX;
		double bulletY = y + y2 + playerHeadY;
		
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

}
