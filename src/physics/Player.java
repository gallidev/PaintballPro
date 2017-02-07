package physics;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import rendering.*;
import java.util.ArrayList;
import java.util.List;

import enums.Teams;

/**
 *  The player, represented by an ImageView
 */
public class Player extends ImageView{
	

	private Image playerImage;
	private final double playerHeadX = 12.5, playerHeadY = 47.5;
	private double x, y;
	private double mx, my;
	private boolean up, down, left, right, shoot;
	private double angle;
	private ArrayList<Bullet> firedBullets = new ArrayList<Bullet>();
	private boolean controlScheme;
	private Rotate rotation;
	private Renderer scene;
	private Map map;
	private String nickname;
	private long shootTime;
	private static long shootDelay = 500;
	private Teams team;


	/**
	 * Create a new player at the set location, and adds the rotation property to the player
	 * @param x The x-coordinate of the player with respect to the map
	 * @param y The y-coordinate of the player with respect to the map
	 * @param controlScheme True - movement with respect to cursor location, False - movement with respect to global position
	 * @param scene The scene in which the player will be displayed
	 * 
	 */
	public Player(float x, float y, boolean controlScheme, Renderer scene, Teams team){
		this.x = x;
		this.y = y;
		this.mx = x;
		this.my = y;
		this.controlScheme = controlScheme;
		this.scene = scene;
		this.team = team;
		angle = 0.0;
		playerImage = new Image("assets/player.png", 30, 64, true, true);
		setImage(playerImage);
		rotation = new Rotate(Math.toDegrees(angle), 0, 0, 0, Rotate.Z_AXIS);
	    getTransforms().add(rotation);
		rotation.setPivotX(playerHeadX);
		rotation.setPivotY(playerHeadY);
		map = scene.getMap();
	}
	
	/**
	 * Constructor needed for the game logic.
	 * @param x The x coordinate of the player.
	 * @param y the y coordinate of the player.
	 * @param nickname The player's nickname.
	 * 
	 * @ atp575
	 */
	public Player(double x, double y, String nickname) {
		super();
		this.x = x;
		this.y = y;
		this.nickname = nickname;
	}

	/**
	 * Tick is called every frame
	 * It updates the player location and angle, and shoots bullets if the shoot button is pressed
	 */
	public void tick() {
		if(controlScheme){
			if(up){
				y -= 2 * Math.cos(angle);
				x += 2 * Math.sin(angle);
			}			
			
			if(down){
				y += 2 * Math.cos(angle);
				x -= 2 * Math.sin(angle);
			}			
			if(left){
				y -= 2 * Math.cos(angle - Math.PI/2);
				x += 2 * Math.sin(angle - Math.PI/2);
			}			
			if(right){
				y -= 2 * Math.cos(angle + Math.PI/2);
				x += 2 * Math.sin(angle + Math.PI/2);
			}
		}
		else{
			if(up){
				y -= 2;
			}	
			if(down){
				y += 2;
			}			
			if(left){
				x -= 2;
			}			
			if(right){
				x += 2;
			}
		}
		
		if(shoot && shootTime < System.currentTimeMillis() - shootDelay){
			shoot();
			shootTime = System.currentTimeMillis();
		}
		
		//Updates the location of the bullets
		for(int i = 0; i < firedBullets.size(); i++){
			firedBullets.get(i).moveInDirection();
		}
		
		//Calculates the angle the player is facing with respect to the mouse
		Point2D temp = this.localToScene(1.65 * playerHeadX, playerHeadY);
		double x1 = temp.getX();
		double y1 = temp.getY();
		
		double deltax = mx - x1;
		double deltay = y1 - my;
		
		angle = Math.atan2(deltax, deltay);
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
						if(propX >= x + playerImage.getWidth()/2){
							if(propY < y + playerImage.getHeight()) {
								x -= 1; //can't go right
							}
							if(propY + propHeight > y) {
								x -= 1; //can't go right
							}
						}
						if(propX + propWidth/2 < x - playerImage.getWidth()/2){
							if(propY < y + playerImage.getHeight()) {
								x += 1; //can't go left
							}
							if(propY + propHeight > y) {
								x += 1; //can't go left
							}
						}
						if(propY >= (y + playerImage.getHeight()/2)){
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
						double propX = wall.getX();
						double propY = wall.getY();
						double propWidth = wall.getImage().getWidth();
						double propHeight = wall.getImage().getHeight();
						if(propX >= x + playerImage.getWidth()/2){
							if(propY < y + playerImage.getHeight()) {
								x -= 1; //can't go right
							}
							if(propY + propHeight > y) {
								x -= 1; //can't go right
							}
						}
						if(propX + propWidth/2 < x - playerImage.getWidth()/2){
							if(propY < y + playerImage.getHeight()) {
								x += 1; //can't go left
							}
							if(propY + propHeight > y) {
								x += 1; //can't go left
							}
						}
						if(propY >= (y + playerImage.getHeight()/2)){
							y -= 2; //can't go down
						}
						if(propY <= y){
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
		
		double x1 = (83 * playerImage.getWidth()/120) - playerHeadX;
		double y1 = (12 * playerImage.getHeight()/255) - playerHeadY;

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

	/** @author atp575**/
	public void setXCoordinate(double x) {
		this.x = x;
	}

	/** @author atp575**/
	public void setYCoordinate(double y) {
		this.y = y;
	}

}
