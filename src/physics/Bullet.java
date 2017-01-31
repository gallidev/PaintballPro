package physics;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Bullet extends Circle{
	
	private double angle;
	private float speed = 6f;
	private double x, y;

	public Bullet(double x, double y, double angle) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		setCenterX(x);
		setCenterY(y);
		//setTranslateX(x);
		//setTranslateY(y);
		setRadius(3);
		setFill(Color.RED);
	}

	public void moveInDirection() {
		y -= speed * Math.cos(angle);
		x += speed * Math.sin(angle);
		setCenterX(x);
		setCenterY(y);
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