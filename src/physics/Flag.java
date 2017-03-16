package physics;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import players.EssentialPlayer;
import rendering.GameObject;
import rendering.ImageFactory;
import rendering.ObjectType;

import java.util.ArrayList;
import java.util.Random;

public class Flag extends ImageView
{
	private boolean captured = false;
	private ArrayList<GameObject> locations;
	private EssentialPlayer flagCarrier;

	public Flag()
	{
		super(ImageFactory.getObjectiveImage(ObjectType.FLAG));
		setEffect(new DropShadow(16, Color.BLACK));
	}

	public Flag(ArrayList<GameObject> locations)
	{
		super(ImageFactory.getObjectiveImage(ObjectType.FLAG));
		this.locations = locations;
		resetPosition();
		setEffect(new DropShadow(16, Color.BLACK));
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
		int randomLocation = (new Random()).nextInt(locations.size());
		relocate(locations.get(randomLocation).getX() * 64, locations.get(randomLocation).getY() * 64);
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
