package physics;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Player extends ImageView{
	
	private Image playerImage;
	private double x, y;
	private double mx, my;
	private boolean up, down, left, right, shoot;
	private double angle, lastAngle;
	private List<Bullet> firedBullets = new ArrayList<Bullet>();

	public Player(float x, float y){
		this.x = x;
		this.y = y;
		this.mx = x;
		this.my = y;
		angle = 0.0;
		playerImage = new Image("assets/player.png", 30, 64, true, true);
		setImage(playerImage);
		setSmooth(true);
	}

	public void tick() {
		if(up){
			y -= 1.5 * Math.cos(angle);
			x += 1.5 * Math.sin(angle);
		}			
		
		if(down){
			y += 0.8 * Math.cos(angle);
			x -= 0.8 * Math.sin(angle);
		}			
		if(left){
			y -= 0.8 * Math.cos(angle - Math.PI/2);
			x += 0.8 * Math.sin(angle - Math.PI/2);
		}			
		if(right){
			y -= 0.8 * Math.cos(angle + Math.PI/2);
			x += 0.8 * Math.sin(angle + Math.PI/2);
		}
		if(shoot){
			shoot();
		}
		for(int i = 0; i < firedBullets.size(); i++){
			firedBullets.get(i).moveInDirection();
		}
		
		double deltax = (mx - 10) - x;
		double deltay = y - (my - 10) ;
		double deltaz = Math.sqrt(deltax * deltax + deltay * deltay);
		double angle = lastAngle;
		if (deltaz > 5){
			angle = Math.atan2(deltax, deltay);
		}
		
		setAngle(angle);
		lastAngle = angle;
		
		setRotate(Math.toDegrees(angle));
		setTranslateX(x);
		setTranslateY(y);
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
