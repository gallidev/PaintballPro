package logic;

public abstract class GameObject {
	private int xCoord;
	private int yCoord;
	
	public GameObject(int xCoord, int yCoord) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
	}
	
	public void setLocation(int x, int y){
		xCoord = x;
		yCoord = y;
	}


	public void setXCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	public void setYCoord(int yCoord) {
		this.yCoord = yCoord;
	}
	
	
	
	
}
