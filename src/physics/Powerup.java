package physics;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import logic.RoundTimer;
import rendering.GameObject;
import rendering.ImageFactory;

import java.util.Random;

public class Powerup extends ImageView
{
	private boolean taken = false;
	private PowerupType type;
	private int duration = 15000; //Respawn 15 seconds after being taken
	private GameObject[] locations;

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

	void resetPosition()
	{
		int randomLocation = (new Random()).nextInt(locations.length);
		relocate(locations[randomLocation].getX() * 64 + 16, locations[randomLocation].getY() * 64 + 16);
	}

	public void took() {
		setVisible(false);
		new java.util.Timer().schedule(
		        new java.util.TimerTask() {
		            @Override
		            public void run() {
		            	setVisible(true);
		                resetPosition();
		            }
		        },
		        duration
		);

	}
}
