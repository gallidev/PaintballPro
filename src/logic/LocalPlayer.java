package logic;

import enums.TeamEnum;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import physics.Bullet;

import java.util.ArrayList;

import static physics.GeneralPlayer.playerHeadX;
import static physics.GeneralPlayer.playerHeadY;

public class LocalPlayer extends ImageView
{
	private int id;
	private double angle;
	private ArrayList<Bullet> firedBullets = new ArrayList<Bullet>();
	private Rotate rotation;

	public LocalPlayer(double x, double y, int id, TeamEnum team)
	{
		super(new Image("assets/player_" + (team == TeamEnum.RED ? "red" : "blue") + ".png", 30, 64, true, true));
		setLayoutX(x);
		setLayoutY(y);
		rotation = new Rotate(Math.toDegrees(angle), 0, 0, 0, Rotate.Z_AXIS);
		getTransforms().add(rotation);
		rotation.setPivotX(playerHeadX);
		rotation.setPivotY(playerHeadY);
	}

	public void tick(double newX, double newY, double newAngle)
	{
		setLayoutX(newX);
		setLayoutY(newY);
		rotation.setAngle(newAngle);
		//firedBullets = newFiredBullets;
	}

	public int getPlayerId()
	{
		return id;
	}

	public ArrayList<Bullet> getFiredBullets()
	{
		return firedBullets;
	}

}
