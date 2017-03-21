package physics;

import integrationServer.CollisionHandlerListener;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import rendering.GameObject;
import rendering.ImageFactory;

import java.util.Random;

public class Powerup extends ImageView
{
	private boolean taken = false;
	private PowerupType type;
	private int duration = 15000; //Respawn 15 seconds after being taken
	private GameObject[] locations;
	private CollisionHandlerListener listener;

	public Powerup(PowerupType type, GameObject[] locations)
	{
		super(ImageFactory.getPowerupImage(type));
		setEffect(new DropShadow(8, type == PowerupType.SHIELD ? Color.GREEN : Color.YELLOW));
		this.type = type;
		this.locations = locations;
		resetPosition();
	}

	public boolean isTaken()
	{
		return taken;
	}

	public void setTaken(boolean b)
	{
		taken = b;
	}

	public PowerupType getType()
	{
		return type;
	}

	public void setType(PowerupType type)
	{
		this.type = type;
	}

	public void setListener(CollisionHandlerListener listener)
	{
		this.listener = listener;
	}

	private void resetPosition()
	{
		int randomLocation = (new Random()).nextInt(locations.length);
		relocate(locations[randomLocation].getX() * 64 + 16, locations[randomLocation].getY() * 64 + 16);
		if(listener != null)
			listener.onPowerupRespawn(type, randomLocation);
	}

	public void resetPosition(int index)
	{
		relocate(locations[index].getX() * 64 + 16, locations[index].getY() * 64 + 16);
	}

	void took() {
		setVisible(false);
		new java.util.Timer().schedule(
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	setVisible(true);
		            	setTaken(false);
		                resetPosition();
		            }
		        },
		        duration
		);

	}
}
