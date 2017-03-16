package physics;

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
	private int duration;
	private GameObject[] locations;

	public Powerup()
	{
	}

	public Powerup(PowerupType type, GameObject[] locations)
	{
		super(ImageFactory.getPowerupImage(type));
		setEffect(new DropShadow(8, Color.GREEN));
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
		relocate(locations[randomLocation].getX() * 64 + 32, locations[randomLocation].getY() * 64 + 32);
	}
}
