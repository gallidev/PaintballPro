package physics;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import players.EssentialPlayer;
import rendering.GameObject;
import rendering.ImageFactory;
import rendering.ObjectType;

import java.util.Random;


/**
 * This class represents a flag object in the Capture the flag game mode
 *
 * @author Artur Komoter
 * @author Filippo Galli
 */
public class Flag extends ImageView
{

	/** The flag is captured or not */
	private boolean captured = false;

	/** The possible locations of the flag. */
	private GameObject[] locations;

	/** The player carrying the flag. */
	private EssentialPlayer flagCarrier;

	/** The index of the flag position. */
	private int index = 0;

	/**
	 * Instantiates a new flag with only the possible locations.
	 *
	 * @param locations the possible locations of the flag
	 */
	public Flag(GameObject[] locations)
	{
		super(ImageFactory.getObjectiveImage(ObjectType.FLAG));
		this.locations = locations;
		setEffect(new DropShadow(12, Color.BLACK));
		resetPosition();
	}

	/**
	 * Instantiates a new flag with the initial index position and the possible locations.
	 *
	 * @param index the initial index position
	 * @param locations the possible locations of the flag
	 */
	public Flag(int index, GameObject[] locations)
	{
		super(ImageFactory.getObjectiveImage(ObjectType.FLAG));
		this.locations = locations;
		setEffect(new DropShadow(12, Color.BLACK));
		this.index = index;
		resetPosition(index);
	}

	/**
	 * Instantiates a new basic flag.
	 */
	public Flag()
	{
		super(ImageFactory.getObjectiveImage(ObjectType.FLAG));
		setEffect(new DropShadow(12, Color.BLACK));
	}

	/**
	 * Checks if is captured.
	 *
	 * @return true, if is captured
	 */
	public boolean isCaptured()
	{
		return captured;
	}

	/**
	 * Sets if it is captured.
	 *
	 * @param b if it is captured
	 */
	void setCaptured(boolean b)
	{
		captured = b;
	}

	/**
	 * Sets the possible locations.
	 *
	 * @param locations the new possible locations
	 */
	public void setLocations(GameObject[] locations){
		this.locations = locations;
	}

	/**
	 * Randomly reset the index position of the flag and relocate it.
	 */
	void resetPosition()
	{
		index = (new Random()).nextInt(locations.length);
		relocate(locations[index].getX() * 64 + 8, locations[index].getY() * 64 + 8);
	}

	/**
	 * Relocate the flag in the specified index position
	 *
	 * @param index the index
	 */
	public void resetPosition(int index)
	{
		relocate(locations[index].getX() * 64 + 8, locations[index].getY() * 64 + 8);
	}

	/**
	 * Gets the flag carrier.
	 *
	 * @return the flag carrier
	 */
	public EssentialPlayer getFlagCarrier()
	{
		return flagCarrier;
	}

	/**
	 * Sets the flag carrier.
	 *
	 * @param p the new flag carrier
	 */
	void setFlagCarrier(EssentialPlayer p)
	{
		this.flagCarrier = p;
	}

	/**
	 * Gets the index position
	 *
	 * @return the index position
	 */
	public int getIndex()
	{
		return index;
	}
}
