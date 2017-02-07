package logic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class GameObject extends ImageView {
	protected double x, y;
	protected Image image;
	
	public GameObject(double x, double y, Image image) {
		this.x = x;
		this.y = y;
		this.image = image;
	}
	
	public void setLocation(double x, double y){
		this.x = x;
		this.y = y;
	}

	public void setXCoord(double x) {
		this.x = x;
	}

	public void setYCoord(double y) {
		this.y = y;
	}
	
	public double getXCoord(){
		return this.x;
	}
	
	public double getYCoord(){
		return this.y;
	}
	
	public abstract void tick();
	
	
	
	
	
	
}
