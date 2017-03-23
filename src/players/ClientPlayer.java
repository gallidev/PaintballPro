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
import logic.server.Team;
import networking.game.UDPClient;
import physics.CollisionsHandler;
import physics.InputHandler;
import rendering.Spawn;

public class ClientPlayer extends EssentialPlayer {


	double angleRadians;
	private double limitDifferencePosition = 40;
	private InputHandler inputHandler;
	private AudioManager audio;
	private Random rand;
	private Label nameTag;

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
		cleanBullets();

		// handle the collisions with walls and props before moving the position
		// of the player so to understand if he can move or not in a specific direction
		collisionsHandler.handlePropWallCollision(this);
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

		if(!eliminated)
		{
			updatePosition();
			//updateShooting();
			updateAngle();

		}

		updateBullets();
		updatePlayerBounds();

		handlePowerUp();

		collisionsHandler.handleBulletCollision(this);
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

	public void setAngleRadians(double angleRadians) {
		this.angleRadians = angleRadians;
	}

	public double getAngleDegrees() {
		return Math.toDegrees(angleRadians);
	}

	public void setInputHandler(InputHandler inputHandler){
		 this.inputHandler = inputHandler;
	}

	public boolean shouldIUpdatePosition(double x, double y){
		return ((Math.abs(x - getLayoutX()) + Math.abs(y - getLayoutY())) > limitDifferencePosition);
	}

	public void relocatePlayerWithTag(double x, double y)
	{
		relocate(x, y);
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

	void cleanBullets(){
		if(firedBullets.size() > 0) {
			if (!firedBullets.get(0).isActive()) {
				firedBullets.remove(0);
			}
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

	public void updateGameSpeed(){
		gameSpeed = (1000 + UDPClient.PINGDELAY) / 1000;
	}

}
