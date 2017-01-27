package physics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Player implements MouseMotionListener, MouseListener{
	
	private BufferedImage testImage;
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
		testImage = ImageLoader.loadImage("/assets/arrow.png");
		playerImage =  testImage.getScaledInstance(25, 25, Image.SCALE_DEFAULT);
	}

	public void tick() {
		if(game.getKeyManager().up){
			y -= 1.5 * Math.cos(angle);
			x += 1.5 * Math.sin(angle);
		}			
		
		if(game.getKeyManager().down){
			y += 0.8 * Math.cos(angle);
			x -= 0.8 * Math.sin(angle);
		}			
		if(game.getKeyManager().left){
			y -= 0.8 * Math.cos(angle - Math.PI/2);
			x += 0.8 * Math.sin(angle - Math.PI/2);
		}			
		if(game.getKeyManager().right){
			y -= 0.8 * Math.cos(angle + Math.PI/2);
			x += 0.8 * Math.sin(angle + Math.PI/2);
		}
		if(game.getKeyManager().shoot){
			shoot();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		//Shoot
		mx = e.getX();
		my = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
	}
	
	public Image getImage(){
		return this.playerImage;
	}
	
	public void setAngle(double angle){
		this.angle = angle;
	}
	
	public void shoot(){
		Bullet bullet = new Bullet(x + playerImage.getWidth(null)/2, y, angle);
		firedBullets.add(bullet);
	}
	
	public List<Bullet> getBullets(){
		return this.firedBullets;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		shoot();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
