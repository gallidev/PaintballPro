package players;
import java.util.ArrayList;
import java.util.List;

import audio.AudioManager;
import enums.TeamEnum;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import logic.GameObject;
import physics.Bullet;
import physics.CollisionsHandlerGeneralPlayer;
import rendering.Map;
import serverLogic.Team;
/**
 *  The player, represented by an ImageView
 */
public class GhostPlayer extends ImageView {

	private ArrayList<Bullet> firedBullets = new ArrayList<Bullet>();
	private Rotate rotation;
	private int playerId;
	private TeamEnum team;
	private AudioManager audio;

	public GhostPlayer(double x, double y, int playerId, Image image, AudioManager audio) {
		super(image);
		setLayoutX(x);
		setLayoutY(y);
		this.playerId = playerId;
		this.audio = audio;
	}

	public void beenShot() {
		audio.playSFX(audio.sfx.splat, (float) 1.0);
		setVisible(false);
	}

	public ArrayList<Bullet> getFiredBullets() {
		return firedBullets;
	}

	public void setFiredBullets(ArrayList<Bullet> firedBullets) {
		this.firedBullets = firedBullets;
	}

	public Rotate getRotation() {
		return rotation;
	}

	public void setRotation(Rotate rotation) {
		this.rotation = rotation;
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
}

