package physics;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

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

	//temporarily added canvas to draw a line showing players path
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
		this.getTransforms().add(rotation);
		rotation.setPivotX(playerImage.getWidth()/2);
		rotation.setPivotY(playerImage.getHeight()/2);
	}

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
		for(int i = 0; i < firedBullets.size(); i++){
			firedBullets.get(i).moveInDirection();
		}
		
		
		//double deltax = (mx) - (x + playerImage.getWidth()/2);
		//double deltay = (y + playerImage.getHeight()/2) - (my);
		
		double deltax = mx - scene.getWidth()/2;
		double deltay = scene.getHeight()/2 - my;
		angle = Math.atan2(deltax, deltay);
		
		setAngle(angle);
		rotation.setAngle(Math.toDegrees(angle));
		
		setLayoutX(x);
		setLayoutY(y);
	}
	
	public void shoot(){
		double bulletX = x + playerImage.getWidth()/2;
		double bulletY = y + playerImage.getHeight()/2; //185
		
		Bullet bullet = new Bullet(bulletX, bulletY, angle);
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
