package players;

import audio.AudioManager;
import enums.GameMode;
import enums.TeamEnum;
import gui.GUIManager;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import logic.server.Team;
import networking.client.ClientInputSender;
import physics.CollisionsHandler;
import physics.GameStateClient;
import physics.InputHandler;
import rendering.Spawn;

import java.util.ArrayList;
import java.util.Random;


/**
 * This class represents the Client player in a multiplayer mode running on the Client simulation.
 *
 *  @author Filippo Galli
 */
public class ClientPlayer extends EssentialPlayer {


	/** The angle in radians. */
	double angleRadians;

	/** The buffer for reconciliation. */
	ArrayList<GameStateClient> bufferReconciliation;

	/** The limit of difference in position. */
	private double limitDifferencePosition = 60;

	/** The input handler. */
	private InputHandler inputHandler;

	/** The name tag. */
	private Label nameTag;

	/** debug mode is on or not */
	private static boolean debug;

	/**
	 * Instantiates a new client player.
	 *
	 * @param x the x position of the client player
	 * @param y the y position of the client player
	 * @param id the id of the client player
	 * @param spawn the spawn locations of the client player
	 * @param team the team of the client player
	 * @param guiManager the gui manager in which the client player is running
	 * @param collisionsHandler the collisions handler of the client simulation
	 * @param inputHandler the input handler
	 * @param image the image of the client player
	 * @param game the game mode
	 * @param currentFPS the current FPS in which the simulation is running on.
	 */
	public ClientPlayer(double x, double y, int id, Spawn[] spawn, TeamEnum team, GUIManager guiManager,
			CollisionsHandler collisionsHandler, InputHandler inputHandler, Image image, GameMode game, double currentFPS) {
		super(x, y, id, spawn, team, collisionsHandler, image, game, currentFPS);
		this.inputHandler = inputHandler;
		this.team = team;

		nameTag = new Label("Player");
		nameTag.setStyle("-fx-background-color: rgba(64, 64, 64, 0.75);" +
				"-fx-font-size: 10pt; -fx-text-fill: white");
		nameTag.setPadding(new Insets(5));
		nameTag.relocate(x - 15, y - 32);

		bufferReconciliation = new ArrayList<>();
	}


	/* (non-Javadoc)
	 * @see players.EssentialPlayer#tick()
	 */
	public void tick(){
		cleanBullets();

		// handle the collisions with walls and props before moving the position
		// of the player so to understand if he can move or not in a specific direction
		collisionsHandler.handlePropWallCollision(this);
		if(!eliminated)
		{
			updatePosition();
			updateAngle();
		}
		updateBullets();
		updatePlayerBounds();

		handlePowerUp();

		collisionsHandler.handleBulletCollision(this);
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updateAngle()
	 */
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

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updateShooting()
	 */
	protected void updateShooting(){
		if(inputHandler.isShooting() && shootTimer < System.currentTimeMillis() - shootDelay){
			shoot();
			shootTimer = System.currentTimeMillis();
		}
	}

	/**
	 * Gets the angle in radians.
	 *
	 * @return the angle radians
	 */
	public double getAngleRadians() {
		return angleRadians;
	}

	/**
	 * Sets the angle in radians.
	 *
	 * @param angleRadians the new angle radians
	 */
	public void setAngleRadians(double angleRadians) {
		this.angleRadians = angleRadians;
	}

	/**
	 * Gets the angle in degrees.
	 *
	 * @return the angle degrees
	 */
	public double getAngleDegrees() {
		return Math.toDegrees(angleRadians);
	}

	/**
	 * Sets the input handler.
	 *
	 * @param inputHandler the new input handler
	 */
	public void setInputHandler(InputHandler inputHandler){
		 this.inputHandler = inputHandler;
	}

	/**
	 * Compare the current client position with the one sent by the server,
	 * if the difference is less than limitDifference position then it returns false otherwise true
	 *
	 * @param x the x of the last correct position given by the server
	 * @param y the y of the last correct position given by the server
	 * @return true, if successful
	 */
	public boolean shouldIUpdatePosition(double x, double y){
		return ((Math.abs(x - getLayoutX()) + Math.abs(y - getLayoutY())) > limitDifferencePosition);
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#relocatePlayerWithTag(double, double)
	 */
	public void relocatePlayerWithTag(double x, double y)
	{
		relocate(x, y);
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updatePosition()
	 */
	@Override
	protected void updatePosition() {

		if(up && !collUp){
			setLayoutY(getLayoutY() - movementSpeed);
		}else if(!up && collUp){
			setLayoutY(getLayoutY() + movementSpeed);
		}
		if(down && !collDown){
			setLayoutY(getLayoutY() + movementSpeed);
		}else if(!down && collDown){
			setLayoutY(getLayoutY() - movementSpeed);
		}
		if(left && !collLeft) {
			setLayoutX(getLayoutX() - movementSpeed);
		} else if(!left && collLeft){
			setLayoutX(getLayoutX() + movementSpeed);
		}
		if(right && !collRight){
			setLayoutX(getLayoutX() + movementSpeed);
		}else if (!right && collRight){
			setLayoutX(getLayoutX() - movementSpeed);
		}

	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#cleanBullets()
	 */
	void cleanBullets(){
		if(firedPellets.size() > 0) {
			if (!firedPellets.get(0).isActive()) {
				firedPellets.remove(0);
			}
		}
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updateScore()
	 */
	@Override
	public void updateScore() {
	}


	/* (non-Javadoc)
	 * @see players.EssentialPlayer#setMyTeam(logic.server.Team)
	 */
	@Override
	public void setMyTeam(Team team) {
	}


	/* (non-Javadoc)
	 * @see players.EssentialPlayer#setOppTeam(logic.server.Team)
	 */
	@Override
	public void setOppTeam(Team team) {
	}


	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updateRotation(double)
	 */
	@Override
	public void updateRotation(double angleRotation) {

	}

//	/**
//	 * Update game speed.
//	 */
//	public void updateGameSpeed(){
//		//gameSpeed = (1000 + UDPClient.PINGDELAY) / 1000;
//	}


	/**
	 * Gets the buffer reconciliation.
	 *
	 * @return the buffer reconciliation
	 */
	public ArrayList<GameStateClient> getBufferReconciliation() {
		return bufferReconciliation;
	}


	/**
	 * Sets the buffer reconciliation.
	 *
	 * @param bufferReconciliation the new buffer reconciliation
	 */
	public void setBufferReconciliation(ArrayList<GameStateClient> bufferReconciliation) {
		this.bufferReconciliation = bufferReconciliation;
	}

	/**
	 * Gets the the right buffer index.
	 *
	 * @param counterFrame the counter frame
	 * @return the the right buffer index
	 */
	public synchronized int getTheRightBufferIndex(int counterFrame){
		for(int i = 0; i < bufferReconciliation.size(); i++){
			if(bufferReconciliation.get(i).getFrame() == counterFrame){
				return i;
			}
		}
		return -1;
	}


	/**
	 * Tick position updates the position of the player based on the reconciliation algorithm
	 */
	public void tickPosition(){
		collisionsHandler.handlePropWallCollision(this);
		if(!eliminated)
		{
			updatePosition();
		}

		updatePlayerBounds();
	}


	/**
	 * Simulate update based on the game state client
	 *
	 * @param gameStateClient the game state client
	 */
	public void simulateUpdate(GameStateClient gameStateClient){
		down = gameStateClient.isDown();
		up = gameStateClient.isUp();
		left = gameStateClient.isLeft();
		right = gameStateClient.isRight();
		tickPosition();
	}

	/**
	 * Replay moves for the client reconciliation algorithm
	 *
	 * @param counterFrame the number counter frame
	 * @param x the x position sent by the server
	 * @param y the y position sent by the server
	 */
	public void replayMoves(int counterFrame, double x, double y){
		int index = getTheRightBufferIndex(counterFrame);

		double simulateX = getLayoutX();
		double simulateY = getLayoutY();
		if(index != -1 && index != (bufferReconciliation.size() -1) && (index+1) != bufferReconciliation.size()  &&
				(x != bufferReconciliation.get(index+ 1).getX() || y != bufferReconciliation.get(index+ 1).getY() ) &&
				(getLayoutX() != x || getLayoutY() != y)){

			//boolean lDown = down , lUp = up, lRight = right , lLeft = left;
			double lmovementSpeed = movementSpeed;
			if(debug) System.out.println("old Speed: " + lmovementSpeed + "new speed: " + movementSpeed );
			for(int i = index+1; i < bufferReconciliation.size() - 1; i++){
				setLayoutX(x);
				setLayoutY(y);
				movementSpeed = movementSpeed * ((double) ClientInputSender.step / 10.0);
				simulateUpdate(bufferReconciliation.get(i+1));
				if(debug) System.out.println("saved buffer x: " + bufferReconciliation.get(i).getX() + " saved buffer y: " + bufferReconciliation.get(i).getY() );
				if(debug) System.out.println("update: " + (i - index) + "new x: " + getLayoutX() + " new y: " + getLayoutY() );
			}
			if(debug) System.out.println("n Updates: " +  (bufferReconciliation.size() - 1 - index) );
			if(debug) System.out.println("received x: " + x + " received y: " + y );
			if(debug) System.out.println("resulted x: " + getLayoutX() + " resulted y: " + getLayoutY() );
			if(debug) System.out.println("my x: " + bufferReconciliation.get(index).getX() + " my y: " + bufferReconciliation.get(index).getY() );
			if(debug) System.out.println("simulate x: " + simulateX + " simulate y: " + simulateY );

			movementSpeed = lmovementSpeed;

			for(int i = 0; i < index-1; i++){
				bufferReconciliation.remove(i);
				index--;
			}

		}else{
			if(debug) System.out.println("no counterFrame found or no need to update : " + counterFrame );
		}


	}



}
