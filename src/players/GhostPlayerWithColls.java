package players;

import java.util.ArrayList;

import audio.AudioManager;
import enums.GameMode;
import enums.TeamEnum;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import physics.Bullet;
import physics.CollisionsHandler;
import rendering.Spawn;
import serverLogic.Team;

public class GhostPlayerWithColls extends EssentialPlayer {

	private Label nameTag;

	public GhostPlayerWithColls(double x, double y, int id, Spawn[] spawn, TeamEnum team,
			CollisionsHandler collisionsHandler, Image image, GameMode game, double currentFPS) {
		super(x, y, id, spawn, team, collisionsHandler, image, game, currentFPS);
	}


	@Override
	public void tick() {

		//collisionsHandler.handlePropWallCollision(this);
		//updateBullets();
		updateAngle();

		//collisionsHandler.handleBulletCollision(this);

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

}
