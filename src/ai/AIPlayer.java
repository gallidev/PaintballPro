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
	private static long shootDelay = 250;
	private boolean up, down, left, right, shoot;
	private double angle;
	private ArrayList<Bullet> firedBullets = new ArrayList<Bullet>();
	private Rotate rotation;
	private Map map;
	private String nickname;
	private long shootTime;
	private Teams team;
	private RandomBehaviour rb;


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
		rb = new RandomBehaviour(this);
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
		rb.tick();
		updateAngle();
		updatePosition();
		updateShooting();
		updateBullets();		
		handlePropCollision();
		handleWallCollision();
		
	}
	
	private void updatePosition(){
		y -= 2 * Math.cos(angle);
		x += 2 * Math.sin(angle);
		
		setLayoutX(x);
		setLayoutY(y);
	}
	
	private void updateShooting(){
		if(shoot && shootTime < System.currentTimeMillis() - shootDelay){
			shoot();
			shootTime = System.currentTimeMillis();
		}
	}
	
	//Updates the location of the bullets
	private void updateBullets(){
		for(int i = 0; i < firedBullets.size(); i++){
			firedBullets.get(i).moveInDirection();
		}
	}
	
	//Calculates the angle the player is facing with respect to the mouse
	private void updateAngle(){
		rotation.setAngle(Math.toDegrees(angle));
	}
	
	private void handlePropCollision(){
		ArrayList<ImageView> props = map.getProps();
		for(ImageView prop : props){
			if(getBoundsInParent().intersects(prop.getBoundsInParent())) {
				double propX = prop.getX();
				double propY = prop.getY();
				double propWidth = prop.getImage().getWidth();
				double propHeight = prop.getImage().getHeight();
				
				//find angle between center of player and center of the prop
				double propCenterX = propX + (propWidth/2);
				double propCenterY = propY + (propHeight/2);
				double playerCenterX = x + image.getWidth()/2;
				double playerCenterY = y + image.getHeight()/2;
				double deltax = propCenterX - playerCenterX;
				double deltay = playerCenterY - propCenterY;
						
				double tempAngle = Math.atan2(deltax, deltay);
				double propAngle = Math.toDegrees(tempAngle);
				
				if(propAngle >= 45 && propAngle <= 135){
					x -= 2; //can't go right
				}
				if(propAngle >= -135 && propAngle <= -45 ){
					x += 2; //can't go left
				}
				if(propAngle > 135 || propAngle < -135){
					y -= 2; //can't go down
				if(propAngle > -45 && propAngle < 45 ){
					y += 2; //can't go up
				}
				angle += Math.toRadians(180);
			}
			for(Bullet bullet : firedBullets){
				if(bullet.getBoundsInParent().intersects(prop.getBoundsInParent())){
					bullet.setActive(false);
				}
			}
			}
		}
	}
	
	private void handleWallCollision(){
		ArrayList<ImageView> walls = map.getWalls();
		for(ImageView wall : walls){
			if(getBoundsInParent().intersects(wall.getBoundsInParent())) {
				double wallX = wall.getX();
				double wallY = wall.getY();
				double wallWidth = wall.getImage().getWidth();
				double wallHeight = wall.getImage().getHeight();
				
				//find angle between center of player and center of wall
				double wallCenterX = wallX + (wallWidth/2);
				double wallCenterY = wallY + (wallHeight/2);
				double playerCenterX = x + image.getWidth()/2;
				double playerCenterY = y + image.getHeight()/2;
				double deltax = wallCenterX - playerCenterX;
				double deltay = playerCenterY - wallCenterY;
						
				double tempAngle = Math.atan2(deltax, deltay);
				double wallAngle = Math.toDegrees(tempAngle);
				
				if(wallAngle >= 45 && wallAngle <= 135){
					x -= 2; //can't go right
				}
				if(wallAngle >= -135 && wallAngle <= -45 ){
					x += 2; //can't go left
				}
				if(wallAngle > 135 || wallAngle < -135){
					y -= 2; //can't go down
				}
				if(wallAngle > -45 && wallAngle < 45 ){
					y += 2; //can't go up
				}
				angle += Math.toRadians(180);
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
