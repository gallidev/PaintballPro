package players;

import enums.GameMode;
import enums.TeamEnum;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import networking.game.UDPClient;
import physics.CollisionsHandler;
import rendering.ImageFactory;
import rendering.Spawn;
import serverLogic.Team;

public class GhostPlayerWithColls extends EssentialPlayer {

	private Label nameTag;

	public GhostPlayerWithColls(double x, double y, int id, Spawn[] spawn, TeamEnum team,
			CollisionsHandler collisionsHandler, GameMode game, double currentFPS) {
		super(x, y, id, spawn, team, collisionsHandler, ImageFactory.getPlayerImage(team), game, currentFPS);
	}


	@Override
	public void tick() {

		cleanBullets();

		collisionsHandler.handlePropWallCollision(this);
		updateBullets();
		updatePlayerBounds();
		updateAngle();

		collisionsHandler.handleBulletCollision(this);

	}

	@Override
	protected void updatePosition() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateShooting() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateAngle() {
		this.rotation.setAngle(Math.toDegrees(angle));
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

	void cleanBullets(){
		if(firedBullets.size() > 0) {
			if (!firedBullets.get(0).isActive()) {
				firedBullets.remove(0);
			}
		}
	}

	public void relocatePlayerWithTag(double x, double y)
	{
		relocate(x, y);
		nameTag.relocate(x - 15, y - 32);
	}

	public String getNickname(){
		return nickname;
	}

	public void setNickname(String name){
		nickname = name;
		nameTag = new Label(nickname);
		nameTag.setStyle("-fx-background-color: rgba(64, 64, 64, 0.75);" +
				"-fx-font-size: 10pt; -fx-text-fill: white");
		nameTag.setPadding(new Insets(5));
		nameTag.relocate(getLayoutX() - 15, getLayoutX() - 32);
	}

	public Node getNameTag(){
		return nameTag;

	}

	public void updateGameSpeed(){
		gameSpeed += gameSpeed * (UDPClient.PINGDELAY/100.0);
	}

}
