package physics;


/**
 * This class represents a Game State of the client in a particular frame,
 * it's used as buffer for player reconciliation in the multi-player.
 *
 * @author Filippo galli
 */
public class GameStateClient {

	/** The frame of the game state. */
	private long frame;

	/** The x value of the client player. */
	private double x;

	/** The y value of the client player. */
	private double y;

	/** The input keyboard values of the client player. */
	private boolean up,down,left,right;

	/**
	 * Instantiates a new game state client with all the relevant information
	 *
	 * @param frame the frame of the game state
	 * @param x the x value of the player
	 * @param y the y value of the player
	 * @param up the up input keyboard value of the player
	 * @param down the down input keyboard value of the player
	 * @param left the left input keyboard value of the player
	 * @param right the right input keyboard value of the player
	 */
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

	/**
	 * Gets the frame.
	 *
	 * @return the frame
	 */
	public long getFrame() {
		return frame;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Checks if is up.
	 *
	 * @return true, if is up
	 */
	public boolean isUp() {
		return up;
	}

	/**
	 * Checks if is down.
	 *
	 * @return true, if is down
	 */
	public boolean isDown() {
		return down;
	}

	/**
	 * Checks if is left.
	 *
	 * @return true, if is left
	 */
	public boolean isLeft() {
		return left;
	}

	/**
	 * Checks if is right.
	 *
	 * @return true, if is right
	 */
	public boolean isRight() {
		return right;
	}


}
