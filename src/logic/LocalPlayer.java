package logic;

import enums.TeamEnum;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import physics.Bullet;

import java.util.ArrayList;

import static gui.GUIManager.bluePlayerImage;
import static gui.GUIManager.redPlayerImage;
import static physics.GeneralPlayer.playerHeadX;
import static physics.GeneralPlayer.playerHeadY;

public class LocalPlayer extends ImageView
{
	private int id;
	private final ArrayList<Bullet> firedBullets = new ArrayList<Bullet>();
	private Rotate rotation;
	private TeamEnum team;

	public LocalPlayer(double x, double y, int id, TeamEnum team)
	{
		super(team == TeamEnum.RED ? redPlayerImage : bluePlayerImage);
		setLayoutX(x);
		setLayoutY(y);
		this.id = id;
		rotation = new Rotate(Math.toDegrees(0.0), 0, 0, 0, Rotate.Z_AXIS);
		getTransforms().add(rotation);
		rotation.setPivotX(playerHeadX);
		rotation.setPivotY(playerHeadY);
		this.team = team;
	}

	public void tick(double newX, double newY, double newAngle)
	{
		setLayoutX(newX);
		setLayoutY(newY);
		rotation.setAngle(Math.toDegrees(newAngle));
	}

	public void tickBullets(ArrayList<Bullet> newFiredBullets)
	{
		firedBullets.clear();
		firedBullets.addAll(newFiredBullets);
	}


	public int getPlayerId()
	{
		return id;
	}

	public ArrayList<Bullet> getFiredBullets()
	{
		ArrayList<Bullet> tmp;
		synchronized(firedBullets)
		{
			tmp = new ArrayList<>(firedBullets);
		}
		return tmp;
	}

	public TeamEnum getTeam()
	{
		return team;
	}

}
