package players;

import enums.Team;
import javafx.scene.transform.Rotate;
import physics.Bullet;
import rendering.ImageFactory;

import java.util.ArrayList;

public class ClientLocalPlayer extends GeneralPlayer
{
	private Rotate rotation;
	private Team team;

	public ClientLocalPlayer(double x, double y, int id, Team team)
	{
		super(x,y,id, ImageFactory.getPlayerImage(team)); 
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

	public Team getTeam()
	{
		return team;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updatePosition() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateAngle() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateScore() {
		// TODO Auto-generated method stub

	}

}
