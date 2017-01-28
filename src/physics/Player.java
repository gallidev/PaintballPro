package physics;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;

public class Player {
	
	private Image playerImage;
	private TestGame game;
	public float x, y, mx, my;
	private double angle;
	private List<Bullet> firedBullets = new ArrayList<Bullet>();

	public Player(TestGame game, float x, float y){
		this.x = x;
		this.y = y;
		this.mx = x;
		this.my = y;
		this.game = game;
		angle = 0.0;
		playerImage = new Image("assets/arrow.png", 25, 25, false, false);
	}

	public void tick() {
		if(game.up){
			y -= 1.5 * Math.cos(angle);
			x += 1.5 * Math.sin(angle);
		}			
		
		if(game.down){
			y += 0.8 * Math.cos(angle);
			x -= 0.8 * Math.sin(angle);
		}			
		if(game.left){
			y -= 0.8 * Math.cos(angle - Math.PI/2);
			x += 0.8 * Math.sin(angle - Math.PI/2);
		}			
		if(game.right){
			y -= 0.8 * Math.cos(angle + Math.PI/2);
			x += 0.8 * Math.sin(angle + Math.PI/2);
		}
		if(game.shoot){
			shoot();
		}
		for(int i = 0; i < firedBullets.size(); i++){
			firedBullets.get(i).moveInDirection();
		}
	}
	
	public Image getImage(){
		return this.playerImage;
	}
	
	public void setAngle(double angle){
		this.angle = angle;
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
	
	public double getMX(){
		return game.mx;
	}
	
	public double getMY(){
		return game.my;
	}

}
