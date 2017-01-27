package physics;

public class Bullet {
	private double angle;
	private float speed = 6f;
	public double x;
	public double y;

	public Bullet(double x, double y, double angle) {
		this.x = x;
		this.y = y;
		this.angle = angle;
	}

	public void moveInDirection() {
		y -= speed * Math.cos(angle);
		x += speed * Math.sin(angle);
	}
}