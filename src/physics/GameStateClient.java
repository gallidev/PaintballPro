package physics;

public class GameStateClient {

	private long frame;
	private double x;
	private double y;
	private boolean up,down,left,right;

	public GameStateClient(long frame, double x, double y, boolean up, boolean down, boolean left, boolean right) {
		super();
		this.frame = frame;
		this.x = x;
		this.y = y;
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
	}

	public long getFrame() {
		return frame;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public boolean isUp() {
		return up;
	}

	public boolean isDown() {
		return down;
	}

	public boolean isLeft() {
		return left;
	}

	public boolean isRight() {
		return right;
	}


}
