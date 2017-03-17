package physics;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import players.EssentialPlayer;
import rendering.GameObject;
import rendering.ImageFactory;
import rendering.ObjectType;

import java.util.Random;

public class Flag extends ImageView
{
	private boolean captured = false;
	private GameObject[] locations;
	private EssentialPlayer flagCarrier;

	public Flag(GameObject[] locations)
	{
		super(ImageFactory.getObjectiveImage(ObjectType.FLAG));
		this.locations = locations;
		resetPosition();
		setEffect(new DropShadow(12, Color.BLACK));
	}

	public boolean isCaptured()
	{
		return captured;
	}

	void setCaptured(boolean b)
	{
		captured = b;
	}

	void resetPosition()
	{
		int randomLocation = (new Random()).nextInt(locations.length);
		relocate(locations[randomLocation].getX() * 64 + 8, locations[randomLocation].getY() * 64 + 8);
	}

	public EssentialPlayer getFlagCarrier()
	{
		return flagCarrier;
	}

	void setFlagCarrier(EssentialPlayer p)
	{
		this.flagCarrier = p;
	}
}
