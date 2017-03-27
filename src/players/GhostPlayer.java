package players;

import enums.GameMode;
import enums.TeamEnum;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import logic.server.Team;
import networking.game.UDPClient;
import physics.CollisionsHandler;
import rendering.ImageFactory;
import rendering.Spawn;


/**
 * The Class represents every player running on the client simulation except of the Client Player.
 *
 * @author Filippo Galli
 */
public class GhostPlayer extends EssentialPlayer {

	/** The name tag. */
	private Label nameTag;


	/**
	 * Instantiates a new ghost player.
	 *
	 * @param x the x position of the player
	 * @param y the y position of the player
	 * @param id the id of the client player
	 * @param spawn the spawn locations of the player
	 * @param team the team of the player
	 * @param collisionsHandler the collisions handler of the client simulation
	 * @param game the game mode
	 * @param currentFPS the current FPS in which the simulation is running on.
	 */
	public GhostPlayer(double x, double y, int id, Spawn[] spawn, TeamEnum team,
			CollisionsHandler collisionsHandler, GameMode game, double currentFPS) {
		super(x, y, id, spawn, team, collisionsHandler, ImageFactory.getPlayerImage(team), game, currentFPS);
	}


	/* (non-Javadoc)
	 * @see players.EssentialPlayer#tick()
	 */
	@Override
	public void tick() {

		cleanBullets();

		collisionsHandler.handlePropWallCollision(this);
		updateBullets();
		updatePlayerBounds();
		updateAngle();

		collisionsHandler.handleBulletCollision(this);

	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updatePosition()
	 */
	@Override
	protected void updatePosition() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updateShooting()
	 */
	@Override
	protected void updateShooting() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updateAngle()
	 */
	@Override
	protected void updateAngle() {
		this.rotation.setAngle(Math.toDegrees(angle));
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updateScore()
	 */
	@Override
	public void updateScore() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#setMyTeam(logic.server.Team)
	 */
	@Override
	public void setMyTeam(Team team) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#setOppTeam(logic.server.Team)
	 */
	@Override
	public void setOppTeam(Team team) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#updateRotation(double)
	 */
	@Override
	public void updateRotation(double angleRotation) {
		// TODO Auto-generated method stub

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
	 * @see players.EssentialPlayer#relocatePlayerWithTag(double, double)
	 */
	public void relocatePlayerWithTag(double x, double y)
	{
		relocate(x, y);
		nameTag.relocate(x - 15, y - 32);
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#getNickname()
	 */
	public String getNickname(){
		return nickname;
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#setNickname(java.lang.String)
	 */
	public void setNickname(String name){
		nickname = name;
		nameTag = new Label(nickname);
		nameTag.setStyle("-fx-background-color: rgba(64, 64, 64, 0.75);" +
				"-fx-font-size: 10pt; -fx-text-fill: white");
		nameTag.setPadding(new Insets(5));
		nameTag.relocate(getLayoutX() - 15, getLayoutX() - 32);
	}

	/* (non-Javadoc)
	 * @see players.EssentialPlayer#getNameTag()
	 */
	public Node getNameTag(){
		return nameTag;

	}

	/**
	 * Update game speed.
	 */
	public void updateGameSpeed(){
		gameSpeed += gameSpeed * (UDPClient.PINGDELAY/100.0);
	}

}
