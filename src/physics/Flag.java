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
	private int index = 0;

	public Flag(GameObject[] locations)
	{
		super(ImageFactory.getObjectiveImage(ObjectType.FLAG));
		this.locations = locations;
		setEffect(new DropShadow(12, Color.BLACK));
		resetPosition();
	}

	public Flag(int index, GameObject[] locations)
	{
		super(ImageFactory.getObjectiveImage(ObjectType.FLAG));
		this.locations = locations;
		setEffect(new DropShadow(12, Color.BLACK));
		this.index = index;
		resetPosition(index);
	}

	public Flag()
	{
		super(ImageFactory.getObjectiveImage(ObjectType.FLAG));
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

	public void setLocations(GameObject[] locations){
		this.locations = locations;
	}

	void resetPosition()
	{
		index = (new Random()).nextInt(locations.length);
		relocate(locations[index].getX() * 64 + 8, locations[index].getY() * 64 + 8);
	}

	public void resetPosition(int index)
	{
		relocate(locations[index].getX() * 64 + 8, locations[index].getY() * 64 + 8);
	}

	public EssentialPlayer getFlagCarrier()
	{
		return flagCarrier;
	}

	void setFlagCarrier(EssentialPlayer p)
	{
		this.flagCarrier = p;
	}

	public int getIndex()
	{
		return index;
	}
}
