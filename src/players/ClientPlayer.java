package players;

import java.util.Random;

import audio.AudioManager;
import enums.GameMode;
import enums.TeamEnum;
import gui.GUIManager;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import physics.CollisionsHandler;
import physics.InputHandler;
import rendering.Spawn;
import serverLogic.Team;

public class ClientPlayer extends EssentialPlayer {

	private InputHandler inputHandler;
	private AudioManager audio;
	private Random rand;
	private Label nameTag;
	double angleRadians;

	public ClientPlayer(double x, double y, int id, Spawn[] spawn, TeamEnum team, GUIManager guiManager,
			CollisionsHandler collisionsHandler, InputHandler inputHandler, Image image, GameMode game, double currentFPS) {
		super(x, y, id, spawn, team, collisionsHandler, image, game, currentFPS);
		this.inputHandler = inputHandler;
		this.audio = guiManager.getAudioManager();
		this.team = team;

		rand = new Random();

		nameTag = new Label("Player");
		nameTag.setStyle("-fx-background-color: rgba(64, 64, 64, 0.75);" +
				"-fx-font-size: 10pt; -fx-text-fill: white");
		nameTag.setPadding(new Insets(5));
		nameTag.relocate(x - 15, y - 32);

	}


	public void tick(){
		// handle the collisions with walls and props before moving the position
		// of the player so to understand if he can move or not in a specific direction
		//collisionsHandler.handlePropWallCollision(this);
		//collisionsHandler.handleFlagCollision(this);
//		if(!eliminated)
//		{
//			//collisionsHandler.handlePowerUpCollision(this);
//			//lastX = getLayoutX();
//			//lastY = getLayoutY();
//			//lastAngle = angle;
//			//updatePosition();
//			//updateShooting();
//			//updateAngle();
//		}
//		else
//		{
//			checkSpawn();
//		}

		updatePlayerBounds();
		//updatePosition();
		updateShooting();
		//updateAngle();
		updateBullets();
		//handlePowerUp();

		//collisionsHandler.handleBulletCollision(this);

//		if(!invincible)
//		{
//
//		}
//		else
//		{
//			checkInvincibility();
//		}
	}

	//Calculates the angle the player is facing with respect to the mouse
	protected void updateAngle()
	{
		Point2D temp = this.localToScene(1.65 * PLAYER_HEAD_X, PLAYER_HEAD_Y);
		double x1 = temp.getX();
		double y1 = temp.getY();

		double deltax = inputHandler.getMouseX() - x1;
		double deltay = y1 - inputHandler.getMouseY();
		angle = Math.atan2(deltax, deltay);

		this.rotation.setAngle(Math.toDegrees(angle));

	}

	protected void updateShooting(){
		if(inputHandler.isShooting() && shootTimer < System.currentTimeMillis() - SHOOT_DELAY){
			shoot();
			shootTimer = System.currentTimeMillis();
		}
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


	@Override
	protected void updatePosition() {

		if(inputHandler.isUp() && !collUp){
			setLayoutY(getLayoutY() - movementSpeed);
		}else if(!inputHandler.isUp() && collUp){
			setLayoutY(getLayoutY() + movementSpeed);
		}
		if(inputHandler.isDown() && !collDown){
			setLayoutY(getLayoutY() + movementSpeed);
		}else if(!inputHandler.isDown() && collDown){
			setLayoutY(getLayoutY() - movementSpeed);
		}
		if(inputHandler.isLeft() && !collLeft) {
			setLayoutX(getLayoutX() - movementSpeed);
		} else if(!inputHandler.isLeft() && collLeft){
			setLayoutX(getLayoutX() + movementSpeed);
		}
		if(inputHandler.isRight() && !collRight){
			setLayoutX(getLayoutX() + movementSpeed);
		}else if (!inputHandler.isRight() && collRight){
			setLayoutX(getLayoutX() - movementSpeed);
		}

	}


	@Override
	public void updateScore() {
		// TODO Auto-generated method stub

	}


	@Override
	public void setMyTeam(Team team) {
		// TODO Auto-generated method stub

	}


	@Override
	public void setOppTeam(Team team) {
		// TODO Auto-generated method stub

	}


	@Override
	public void updateRotation(double angleRotation) {
		// TODO Auto-generated method stub

	}

}
