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
import physics.GhostBullet;
import rendering.Map;
import serverLogic.Team;
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
	
	
	public GhostPlayer(double x, double y, int playerId, Image image, AudioManager audio) {
		super(image);
		setLayoutX(x);
		setLayoutY(y);
		this.playerId = playerId;
		this.audio = audio;
		rotation = new Rotate(Math.toDegrees(0), 0, 0, 0, Rotate.Z_AXIS);
	    getTransforms().add(rotation);
		rotation.setPivotX(playerHeadX);
		rotation.setPivotY(playerHeadY);
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
		GhostBullet bullet = new GhostBullet(bulletId, x, y, this.team);
		this.firedBullets.add(bullet);
	}

	public synchronized Rotate getRotation() {
		return rotation;
	}

	public synchronized void setRotationAngle(double angleDegress) {
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

	public synchronized void setSyncVisible(boolean visible){
		setVisible(visible);
	}

	public synchronized void setSyncX(double x){
		setLayoutX(x);
	}

	public synchronized void setSyncY(double y){
		setLayoutY(y);
	}

	public TeamEnum getTeam(){
		return team;
	}
	
	public String getNickname(){
		return nickname;
	}
	
	public void  setNickname(String name){
		nickname = name;
	}

}

