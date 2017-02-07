package logic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class GameObject extends ImageView {
	protected double x, y;
	protected Image image;
	
	public GameObject(double x, double y, Image image) {
		super(image);
		this.x = x;
		this.y = y;
		this.image = image;
		setLayoutX(x);
		setLayoutY(y);
	}

	public void setXCoord(double x) {
		this.x = x;
		setLayoutX(x);
	}

	public void setYCoord(double y) {
		this.y = y;
		setLayoutY(y);
	}
	
	public double getXCoord(){
		return this.x;
	}
	
	public double getYCoord(){
		return this.y;
	}
	
	public abstract void tick();
		
	
}
