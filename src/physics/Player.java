package physics;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

/**
 *  The player, represented by an ImageView
 */
public class Player extends ImageView{
	
	private Image playerImage;
	private double x, y;
	private double mx, my;
	private boolean up, down, left, right, shoot;
	private double angle;
	private ArrayList<Bullet> firedBullets = new ArrayList<Bullet>();
	private boolean controlScheme;
	private Rotate rotation;
	private Scene scene;

	/**
	 * Create a new player at the set location, and adds the rotation property to the player
	 * @param x The x-coordinate of the player with respect to the map
	 * @param y The y-coordinate of the player with respect to the map
	 * @param controlScheme True - movement with respect to cursor location, False - movement with respect to global position
	 * @param scene The scene in which the player will be displayed
	 */
	public Player(float x, float y, boolean controlScheme, Scene scene){
		this.x = x;
		this.y = y;
		this.mx = x;
		this.my = y;
		this.controlScheme = controlScheme;
		this.scene = scene;
		angle = 0.0;
		playerImage = new Image("assets/player.png", 30, 64, true, true);
		setImage(playerImage);
		setSmooth(true);
		rotation = new Rotate(Math.toDegrees(angle), 0, 0, 0, Rotate.Z_AXIS);
	    getTransforms().add(rotation);
		rotation.setPivotX(playerImage.getWidth()/2);
		rotation.setPivotY(playerImage.getHeight()/2);
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
		
		if(shoot){
			shoot();
		}
		
		//Updates the location of the bullets
		for(int i = 0; i < firedBullets.size(); i++){
			firedBullets.get(i).moveInDirection();
		}
		
		//Calculates the angle the player is facing with respect to the mouse
		double deltax = mx - scene.getWidth()/2;
		double deltay = scene.getHeight()/2 - my;
		angle = Math.atan2(deltax, deltay);
		setAngle(angle);
		rotation.setAngle(Math.toDegrees(angle));
		
		//Moves player in target direction
		setLayoutX(x);
		setLayoutY(y);
	}
	
	/**
	 * Creates a bullet at the player's location that travels in the direction the player is facing.
	 * The bullet is added to the arraylist "firedBullets"
	 * It is called every time the player presses the left mouse button
	 */
	public void shoot(){
		
		double x1 = (83 * playerImage.getWidth()/120) - playerImage.getWidth()/2;
		double y1 = (12 * playerImage.getHeight()/255) - playerImage.getHeight()/2;

		double x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
		double y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);

		double bulletX = x + x2 + playerImage.getWidth()/2;
		double bulletY = y + y2 + playerImage.getHeight()/2;
		
		Bullet bullet = new Bullet(bulletX, bulletY, angle);
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

}
