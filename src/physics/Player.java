package physics;
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
	private double angle, lastAngle;
	private List<Bullet> firedBullets = new ArrayList<Bullet>();
	private boolean controlScheme;
	private Rotate rotation;

	public Player(float x, float y, boolean controlScheme){
		this.x = x;
		this.y = y;
		this.mx = x;
		this.my = y;
		this.controlScheme = controlScheme;
		angle = 0.0;
		playerImage = new Image("assets/player.png", 30, 64, true, true);
		setImage(playerImage);
		setSmooth(true);
		rotation = new Rotate(Math.toDegrees(angle), x, y, 0, Rotate.Z_AXIS);
		this.getTransforms().add(rotation);
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
		
		double deltax = (mx - 10) - x;
		double deltay = y - (my - 10);
		double deltaz = Math.sqrt(deltax * deltax + deltay * deltay);
		double angle = lastAngle;
		if (deltaz > 5){
			angle = Math.atan2(deltax, deltay);
		}
		angle = Math.atan2(deltax, deltay);
		
		setAngle(angle);
		lastAngle = angle;
		rotation.setPivotX(50 * playerImage.getWidth()/118);
		rotation.setPivotY(185 * playerImage.getHeight()/255);
		rotation.setAngle(Math.toDegrees(angle));
		
		setLayoutX(x);
		setLayoutY(y);
	}
	
	public void shoot(){
		double bulletX = x + (playerImage.getWidth()/2);
		double bulletY = y + (playerImage.getHeight()/2);
		
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
