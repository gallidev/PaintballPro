package physics;

public class Bullet {
	
	private double angle;
	private float speed = 6f;
	private double x, y;

	public Bullet(double x, double y, double angle) {
		this.x = x;
		this.y = y;
		this.angle = angle;
	}

	public void moveInDirection() {
		y -= speed * Math.cos(angle);
		x += speed * Math.sin(angle);
	}
	
	public double getX(){
		return this.x;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public double getY(){
		return this.y;
	}
	
	public void setY(double y){
		this.y = y;
	}
}