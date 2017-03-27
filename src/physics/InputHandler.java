package physics;

import players.EssentialPlayer;

/**
 * This Class controls and handles the inputs from keyboard and mouse.
 *
 * @author Filippo Galli
 */
public class InputHandler {

	/** The keyboard input for up,down,left and right. */
	boolean up, down, left, right;

	/** The mouse click input whether a player has shot or not */
	boolean shoot;

	/** The mouse X and Y position in the scene. */
	int mouseX, mouseY;

	/**
	 * Instantiates a new input handler.
	 */
	public InputHandler(){
		up = false;
		down = false;
		left = false;
		right = false;
		shoot = false;
		mouseX = 0;
		mouseY = 0;
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
	 * Sets the up.
	 *
	 * @param up the new up
	 */
	public void setUp(boolean up) {
		this.up = up;
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
	 * Sets the down.
	 *
	 * @param down the new down
	 */
	public void setDown(boolean down) {
		this.down = down;
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
	 * Sets the left.
	 *
	 * @param left the new left
	 */
	public void setLeft(boolean left) {
		this.left = left;
	}

	/**
	 * Checks if is right.
	 *
	 * @return true, if is right
	 */
	public boolean isRight() {
		return right;
	}

	/**
	 * Sets the right.
	 *
	 * @param right the new right
	 */
	public void setRight(boolean right) {
		this.right = right;
	}

	/**
	 * Checks if is shooting.
	 *
	 * @return true, if is shooting
	 */
	public boolean isShooting() {
		return shoot;
	}

	/**
	 * Sets the shoot.
	 *
	 * @param shoot the new shoot
	 */
	public void setShoot(boolean shoot) {
		this.shoot = shoot;
	}

	/**
	 * Gets the mouse X.
	 *
	 * @return the mouse X
	 */
	public int getMouseX() {
		return mouseX;
	}

	/**
	 * Sets the mouse X.
	 *
	 * @param mouseX the new mouse X
	 */
	public void setMouseX(int mouseX) {
		this.mouseX = mouseX;
	}

	/**
	 * Gets the mouse Y.
	 *
	 * @return the mouse Y
	 */
	public int getMouseY() {
		return mouseY;
	}

	/**
	 * Sets the mouse Y.
	 *
	 * @param mouseY the new mouse Y
	 */
	public void setMouseY(int mouseY) {
		this.mouseY = mouseY;
	}

	/**
	 * Calculates and gets the angle of the player who is aiming to.
	 *
	 * @return the angle of the player who is aiming to
	 */
	public double getAngle(){

		double deltax = mouseX - (1.65 * EssentialPlayer.PLAYER_HEAD_X);
		double deltay = EssentialPlayer.PLAYER_HEAD_Y - mouseY;
		double angle = Math.atan2(deltax, deltay);
		return angle;
	}

}
