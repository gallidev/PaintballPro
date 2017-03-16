package players;

import audio.AudioManager;
import enums.TeamEnum;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import physics.InputHandler;

public class ClientPlayer extends GhostPlayer {

	InputHandler inputHandler;
	double angleRadians;
	public final double PLAYER_HEAD_X = 12.5, PLAYER_HEAD_Y = 47.5;

	public ClientPlayer(double x, double y, int playerId, Image image, AudioManager audio, TeamEnum team) {
		super(x, y, playerId, image, audio, team);
	}


	public void tick(){
		updateAngle();
	}

	//Calculates the angle the player is facing with respect to the mouse
	private void updateAngle()
	{
		Point2D temp = this.localToScene(1.65 * PLAYER_HEAD_X, PLAYER_HEAD_Y);
		double x1 = temp.getX();
		double y1 = temp.getY();

		double deltax = inputHandler.getMouseX() - x1;
		double deltay = y1 - inputHandler.getMouseY();
		angleRadians = Math.atan2(deltax, deltay);
	}


	public double getAngleRadians() {
		return angleRadians;
	}

	public double getAngleDegrees() {
		return Math.toDegrees(angleRadians);
	}


	public void setAngleRadians(double angleRadians) {
		this.angleRadians = angleRadians;
	}

	public void setInputHandler(InputHandler inputHandler){
		 this.inputHandler = inputHandler;
	}

}
