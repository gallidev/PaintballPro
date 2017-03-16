package players;

import audio.AudioManager;
import enums.TeamEnum;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import physics.GhostBullet;

import java.util.ArrayList;
/**
 *  The player, represented by an ImageView
 */
public class GhostPlayer extends ImageView {

	public static final double playerHeadX = 12.5, playerHeadY = 47.5;
	private ArrayList<GhostBullet> firedBullets = new ArrayList<GhostBullet>();
	private Rotate rotation;
	private int playerId;
	private TeamEnum team;
	private AudioManager audio;
	private String nickname;
	private Label nameTag;

	public GhostPlayer(double x, double y, int playerId, Image image, AudioManager audio, TeamEnum team) {
		super(image);
		setLayoutX(x);
		setLayoutY(y);
		setEffect(new DropShadow(16, 0, 0, Color.BLACK));

		this.playerId = playerId;
		this.audio = audio;
		rotation = new Rotate(Math.toDegrees(0), 0, 0, 0, Rotate.Z_AXIS);
	    getTransforms().add(rotation);
		rotation.setPivotX(playerHeadX);
		rotation.setPivotY(playerHeadY);
		this.team = team;
	}

	public void beenShot() {
		audio.playSFX(audio.sfx.splat, (float) 1.0);
		setVisible(false);
	}

	public ArrayList<GhostBullet> getFiredBullets() {
		return firedBullets;
	}

	public synchronized void setFiredBullets(ArrayList<GhostBullet> firedBullets) {
		this.firedBullets = firedBullets;
	}

	public synchronized void updateSingleBullet(int bulletId, double x, double y){
		for (int i = 0; i < this.firedBullets.size(); i ++){
			if(this.firedBullets.get(i).getBulletId() == bulletId){
				this.firedBullets.get(i).setX(x);
				this.firedBullets.get(i).setY(y);
				return;
			}
		}
		GhostBullet bullet = new GhostBullet(bulletId, x, y, team);
		this.firedBullets.add(bullet);
	}

	public synchronized Rotate getRotation() {
		return rotation;
	}

	public synchronized void setSyncRotationAngle(double angleDegress) {
		this.rotation.setAngle(angleDegress);
	}

	public AudioManager getAudio() {
		return audio;
	}

	public void setAudio(AudioManager audio){
		this.audio = audio;
	}

	public int getPlayerId(){
		return this.playerId;
	}

	public void setSyncVisible(boolean visible){
		setVisible(visible);
	}

	public void setSyncX(double x){
		setLayoutX(x);
		nameTag.setLayoutX(x - 15);
	}

	public void setSyncY(double y){
		setLayoutY(y);
		nameTag.setLayoutY(y - 32);
	}

	public TeamEnum getTeam(){
		return team;
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

	public Label getNameTag()
	{
		return nameTag;
	}
}

