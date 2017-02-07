package physics;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import logic.GameObject;
import rendering.*;
import java.util.ArrayList;
import java.util.List;
import enums.Teams;

/**
 *  The player, represented by an ImageView
 */
public class Player extends GameObject{
	
	private final double playerHeadX = 12.5, playerHeadY = 47.5;
	private static long shootDelay = 250;
	private double mx, my;
	private boolean up, down, left, right, shoot;
	private double angle;
	private ArrayList<Bullet> firedBullets = new ArrayList<Bullet>();
	private boolean controlScheme;
	private Rotate rotation;
	private Map map;
	private String nickname;
	private long shootTime;
	private Teams team;


	/**
	 * Create a new player at the set location, and adds the rotation property to the player
	 * @param x The x-coordinate of the player with respect to the map
	 * @param y The y-coordinate of the player with respect to the map
	 * @param controlScheme True - movement with respect to cursor location, False - movement with respect to global position
	 * @param scene The scene in which the player will be displayed
	 * 
	 */
	public Player(double x, double y, String nickname, boolean controlScheme, Renderer scene, Teams team, Image image){
		super(x, y, image);
		this.mx = x;
		this.my = y;
		this.controlScheme = controlScheme;
		this.team = team;
		this.nickname = nickname;
		angle = 0.0;
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
	public Player(double x, double y, String nickname, Image image) {
		super(x, y, image);
		this.nickname = nickname;
	}

	/**
	 * Tick is called every frame
	 * It updates the player location and angle, and shoots bullets if the shoot button is pressed
	 */
	@Override
	public void tick() {
		updatePosition();
		updateShooting();
		updateBullets();
		updateAngle();
		handlePropCollision();
		handleWallCollision();	
	}

	private void updatePosition(){
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
		} else {
			if(up) y -= 2;
			if(down) y += 2;			
			if(left) x -= 2;		
			if(right) x += 2;
		}
		
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
		Point2D temp = this.localToScene(1.65 * playerHeadX, playerHeadY);
		double x1 = temp.getX();
		double y1 = temp.getY();
				
		double deltax = mx - x1;
		double deltay = y1 - my;
				
		angle = Math.atan2(deltax, deltay);
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
				if(propX >= x + image.getWidth()/2){
					if(propY < y + image.getHeight() || propY + propHeight > y) {
						x -= 2; //can't go right
					}
				}
				if(propX + propWidth/2 < x - image.getWidth()/2){
					if(propY < y + image.getHeight() || propY + propHeight > y) {
						x += 2; //can't go left
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
	}
	
	private void handleWallCollision(){
		ArrayList<ImageView> walls = map.getWalls();
		for(ImageView wall : walls){
			if(getBoundsInParent().intersects(wall.getBoundsInParent())) {
				double wallX = wall.getX();
				double wallY = wall.getY();
				double wallWidth = wall.getImage().getWidth();
				double wallHeight = wall.getImage().getHeight();
				if(wallX >= x + image.getWidth()/2){
					if(wallY < y + image.getHeight() || wallY + wallHeight > y) {
						x -= 2; //can't go right
						System.out.println("Right");
					}
				}
				if(wallX + wallWidth/2 < x - image.getWidth()/2){
					if(wallY < y + image.getHeight() || wallY + wallHeight > y) {
						x += 2; //can't go left
						System.out.println("Left");
					}
				}
				if(wallY >= (y + image.getHeight()/2)){
					y -= 2; //can't go down
					System.out.println("Down");
				}
				if(wallY <= y){
					y += 2; //can't go up
					System.out.println("Up");
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
