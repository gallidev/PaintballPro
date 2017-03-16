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
import rendering.ImageFactory;

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
	private DropShadow shadow = new DropShadow(16, 0, 0, Color.BLACK);

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

	public synchronized void updateSingleBullet(int bulletId, double x, double y){
		for(GhostBullet firedBullet : this.firedBullets)
		{
			if(firedBullet.getBulletId() == bulletId)
			{
				firedBullet.setX(x);
				firedBullet.setY(y);
				return;
			}
		}
		GhostBullet bullet = new GhostBullet(bulletId, x, y, team);
		this.firedBullets.add(bullet);
	}

	public void setRotationAngle(double angle) {
		this.rotation.setAngle(angle);
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

	public void relocatePlayer(double x, double y)
	{
		relocate(x, y);
		nameTag.relocate(x - 15, y - 32);
	}

	public void setFlagStatus(boolean status)
	{
		if(status)
		{
			shadow.setColor(team == TeamEnum.RED ? Color.RED : Color.BLUE);
			setImage(ImageFactory.getPlayerFlagImage(team));
		}
		else
		{
			shadow.setColor(Color.BLACK);
			setImage(ImageFactory.getPlayerImage(team));
		}
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

