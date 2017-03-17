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
	private int duration = 10000; //SPEED lasts for 10 seconds
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

	public int getDuration(){
		return this.duration;
	}

	void resetPosition()
	{
		int randomLocation = (new Random()).nextInt(locations.length);
		relocate(locations[randomLocation].getX() * 64 + 16, locations[randomLocation].getY() * 64 + 16);
	}
}
